package jp.tande.android.comicwatcher.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.tande.android.comicwatcher.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoader {
	private static final String TAG="ImageLoader";
	private Pattern pattern = Pattern.compile(".*\\/(.*\\.(?:jpg|png|gif)).*");
	private String cachePath;
	private byte[] buffer = new byte[4096];

	private final class Loader implements Runnable {
		private final Task task;

		private Loader(Task task) {
			this.task = task;
		}

		@Override
		public void run() {
			URL u;
			Bitmap bmp = null;

			try {
				u = new URL(task.url);

				//http://thumbnail.image.rakuten.co.jp/@0_mall/book/cabinet/0471/04715420.jpg?_ex=120x120
				File f = createCacheFile(cachePath, u);
				if( f != null ){
					try{
						if( ! f.exists() ){
							Log.d(TAG, "loading image : " + task.url);
							InputStream is = u.openStream();
							writeStreamToFile(is, f);
							is.close();
							//bmp = BitmapFactory.decodeStream(u.openStream());
						}else{
							Log.d(TAG,"loading from local : " + f);
						}
						bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
					}catch(IOException e){
						//TODO save to cache error
					}
				}else{
					Log.w(TAG,"run : cache file path parse error! load from network. " + task.url );
					Log.d(TAG, "loading image : " + task.url);
					try {
						bmp = BitmapFactory.decodeStream(u.openStream());
					} catch (IOException e) {
						// TODO decode stream error
					}
				}
			} catch (MalformedURLException e) {
				// TODO invalid url
				e.printStackTrace();
			}finally{
				setBitmapToViews(task, bmp);
			}

		}
		
		private File createCacheFile(String basePath, URL u){
			Matcher m = pattern.matcher(u.getPath());
			if( m.matches() ){
				Log.d(TAG,"cache path : " + basePath + m.group(1));
				return new File(basePath + m.group(1));
			}
			return null;
		}
		
		private void writeStreamToFile(InputStream is, File out) throws IOException{
			FileOutputStream fos = new FileOutputStream(out);
			int readBytes = 0;
			synchronized (buffer) {
				while ( (readBytes = is.read(buffer)) != -1 ){
					fos.write(buffer, 0, readBytes);
				}
			}
			fos.close();
		}
		
		private void setBitmapToViews(final Task task, final Bitmap bmp){
			handler.post(new Runnable() {
				@Override
				public void run() {
					ImageCache.Value v = new ImageCache.Value( bmp );
					synchronized (tasks) {
						for (Iterator<WeakReference<ImageView>> itr = task.targetViews.iterator(); itr.hasNext();) {
							ImageView imgView = itr.next().get();
							if( imgView != null && task.url.equals(imgView.getTag(R.id.tag_imageview_url)) ){
								Log.d(TAG,"run : notify load finished " +task.url );
								imgView.setImageBitmap( bmp );
							}else{
								itr.remove();
							}
						}
						v.targetViews.addAll(task.targetViews);
						cache.put(task.url, v);
						tasks.remove(task);
					}
				}
			});
		}
	}

	/*
	public interface ImageLoaderListener{
		void onLoadFinished(String url, Bitmap bmp);
		void onExpired(String url, Bitmap bmp);
	}
	*/
	
	private ExecutorService executor = Executors.newFixedThreadPool(2);
	private Handler handler;
	
	
	public static class ImageCache extends LinkedHashMap<String, ImageCache.Value> {
		private static final long serialVersionUID = 265254089315207090L;
		private int maxSize;
		
		public static class Value{
			private Bitmap bmp;
			private ArrayList<WeakReference<ImageView>> targetViews 
				= new ArrayList<WeakReference<ImageView>>();
			public Value(Bitmap bmp){
				this.bmp = bmp;
			}
		}
		
		public ImageCache(int maxSize){
			super(maxSize, 0.75f, true);
			this.maxSize = maxSize;
		}
		
		@Override
		protected boolean removeEldestEntry(Entry<String, Value> eldest) {
			if ( size() > maxSize ){
				Value v = eldest.getValue();
				boolean using = false;
				for (Iterator<WeakReference<ImageView>> itr = v.targetViews.iterator(); itr.hasNext();) {
					ImageView imageView = itr.next().get();
					if( imageView != null && eldest.getKey().equals(imageView.getTag(R.id.tag_imageview_url)) ){
						using = true;
						break;
					}else{
						itr.remove();
					}
				}
				if( ! using ){
					Log.d(TAG,"removeEldestEntry : " + eldest.getKey());
					if(v.bmp !=null)v.bmp.recycle();
					return true;
				}else{
					Log.d(TAG,"removeEldestEntry cancel count : " + size() + " "+eldest.getKey());
					get(eldest.getKey());//touch value to change order
					//TODO remove other item to keep maxsize
				}
			}
			return false;
		}
	}

	private class Task{
		public Task(ImageView targetView, String url) {
			this.targetViews  = new ArrayList<WeakReference<ImageView>>();
			targetViews.add(new WeakReference<ImageView>(targetView) );
			this.url = url;
		}
		ArrayList<WeakReference<ImageView>> targetViews;
		String url;
	}
	
	private LinkedHashMap<String, Task> tasks = new LinkedHashMap<String, Task>();
	private ImageCache cache = new ImageCache(100);
	private ArrayList< Future<?>> pendingTasks = new ArrayList<Future<?>>();
	
	private static ImageLoader instance;
	
	
	public synchronized static ImageLoader getInstance(){
		if( instance == null ){
			instance = new ImageLoader();
		}
		if( instance.executor.isShutdown() ){
			//TODO care about shutdown and restart
			instance.executor = Executors.newFixedThreadPool(2);
		}
		return instance;
	}
	
	private ImageLoader() {

		this.handler = new Handler();
	}
	
	public void init(){
		
	}
	
	public void shutdown(){
		Log.d(TAG,"shutdown");
		executor.shutdown();
		Log.d(TAG,"shutdown finished");
	}
	public synchronized void queue(final String url, final ImageView targetView){
		targetView.setTag(R.id.tag_imageview_url, url);
		
		if( cachePath == null ){
			cachePath = targetView.getContext().getCacheDir().getAbsolutePath() + "/images/";
			new File(cachePath).mkdirs();
		}
		
		synchronized (tasks) {
			ImageCache.Value cacheVal = cache.get(url);
			if( cacheVal != null ){
				cacheVal.targetViews.add(new WeakReference<ImageView>(targetView));
				//Log.d(TAG,"queue : cache hit " + url);
				//l.onLoadFinished( url, cacheVal.bmp );
				targetView.setImageBitmap(cacheVal.bmp);
				return;
			}

			
			Task task = tasks.get(url);
			if( task == null ){
				task = new Task(targetView, url);
				tasks.put(url, task);
				
				Runnable r = new Loader(task);
				Log.d(TAG,"queue : submit request " + url);
				pendingTasks.add( executor.submit(r) );
			}else{
				Log.d(TAG,"queue : added listener to posted request " + url);
				task.targetViews.add(new WeakReference<ImageView>(targetView));
				return;
			}
		}
		
	}
	
	public synchronized void cancelAll(){
		synchronized (tasks) {
			tasks.clear();
			synchronized (pendingTasks) {
				for (Future<?> f : pendingTasks) {
					f.cancel(true);
				}
				pendingTasks.clear();
			}
		}
	}
}
