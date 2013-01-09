package jp.tande.android.comicwatcher.api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

public class ImageLoader {
	private static final String TAG="ImageLoader";
	public interface ImageLoaderListener{
		void onLoadFinished(Bitmap bmp);
	}
	
	private ExecutorService executor = Executors.newFixedThreadPool(2);
	private Handler handler;
	
	private static ImageLoader instance;
	
	public static void initInstance(Handler handler){
		instance = new ImageLoader(handler);
	}
	
	public static ImageLoader getInstance(){
		return instance;
	}
	
	private ImageLoader(Handler handler) {

		this.handler = handler;
	}
	
	public void init(){
		
	}
	
	public void shutdown(){
		executor.shutdown();
	}
	public void queue(final String url, final ImageLoaderListener l){
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				URL u;
				try {
					u = new URL(url);
					Log.d(TAG, "loading image : " + url);
					final Bitmap bmp = BitmapFactory.decodeStream(u.openStream());
					if( l != null ){
						handler.post(new Runnable() {
							@Override
							public void run() {
								l.onLoadFinished(bmp);
							}
						});
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		executor.submit(r);
	}
}
