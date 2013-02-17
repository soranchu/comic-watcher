package jp.tande.android.comicwatcher.api;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import jp.tande.android.comicwatcher.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoader {
	private static final String TAG="ImageLoader";
	
	private final class Loader implements Runnable {
		private final Task task;

		private Loader(Task task) {
			this.task = task;
		}

		@Override
		public void run() {
			URL u;
			try {
				u = new URL(task.url);
				Log.d(TAG, "loading image : " + task.url);
				final Bitmap bmp = BitmapFactory.decodeStream(u.openStream());
				handler.post(new Runnable() {
					@Override
					public void run() {
						ImageCache.Value v = new ImageCache.Value(bmp);
						v.targetViews.addAll(task.targetViews);
						synchronized (cache) {
							cache.put(task.url, v);
						}
						synchronized (tasks) {
							tasks.remove(task);
							for (WeakReference<ImageView> wl : task.targetViews) {
								ImageView l = wl.get();
								if( l != null ){
									Log.d(TAG,"run : notify load finished " +task.url );
									//l.onLoadFinished(task.url, bmp);
									if( task.url.equals(l.getTag(R.id.tag_imageview_url))){
										l.setImageBitmap(bmp);
									}
								}
							}
						}
					}
				});
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			private CopyOnWriteArrayList<WeakReference<ImageView>> targetViews 
				= new CopyOnWriteArrayList<WeakReference<ImageView>>();
			public Value(Bitmap bmp){
				this.bmp = bmp;
			}
		}
		
		public interface CacheExpiredListener{
		}
		public ImageCache(int maxSize){
			super(maxSize, 0.75f, true);
			this.maxSize = maxSize;
		}
		
		@Override
		protected boolean removeEldestEntry(Entry<String, Value> eldest) {
			if ( size() > maxSize ){
				Value v = eldest.getValue();
				for (WeakReference<ImageView> l : v.targetViews) {
					ImageView imageView = l.get();
					if( imageView != null ) {
						Log.d(TAG,"removeEldestEntry : notify cache expired " + eldest.getKey() );
						imageView.setImageBitmap(null);
						//listener.onExpired(eldest.getKey(), v.bmp);
					}
				}
				v.bmp.recycle();
				return true;
			}
			return false;
		}
	}

	private class Task{
		public Task(ImageView targetView, String url) {
			this.targetViews  = new CopyOnWriteArrayList<WeakReference<ImageView>>();
			targetViews.add(new WeakReference<ImageView>(targetView) );
			this.url = url;
		}
		CopyOnWriteArrayList<WeakReference<ImageView>> targetViews;
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
		synchronized (cache) {
			ImageCache.Value cacheVal = cache.get(url);
			if( cacheVal != null ){
				cacheVal.targetViews.add(new WeakReference<ImageView>(targetView));
				//Log.d(TAG,"queue : cache hit " + url);
				//l.onLoadFinished( url, cacheVal.bmp );
				targetView.setImageBitmap(cacheVal.bmp);
				return;
			}
		}
		
		synchronized (tasks) {
			Task task = tasks.get(url);
			if( task == null ){
				task = new Task(targetView, url);
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
		synchronized (pendingTasks) {
			for (Future<?> f : pendingTasks) {
				f.cancel(true);
			}
			pendingTasks.clear();
		}
	}
}
