package jp.tande.android.comicwatcher.api;

import java.io.IOException;
import java.util.ArrayList;

import net.arnx.jsonic.JSONException;

import jp.tande.android.comicwatcher.api.data.BookSearchResponse;
import jp.tande.android.comicwatcher.db.BookInfo;
import jp.tande.android.comicwatcher.db.BookSeries;
import jp.tande.android.comicwatcher.db.DatabaseManager.Contract;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;

public class ListUpdater {
	private static final String TAG = ListUpdater.class.getSimpleName();
	
	private Context context;
	private BooksApi api;
	public ListUpdater(Context context){
		this.context = context;
		api = new BooksApi(new Handler(context.getMainLooper()));
	}
	
	public void update(){
		//CursorLoader loader = new CursorLoader(context);
		//loader.
		Cursor c = null;
		final ArrayList<BookSeries> series = new ArrayList<BookSeries>();
		try{
			c = context.getContentResolver().query(Contract.Follows.ContentUri, null, null, null, null);
			while(c.moveToNext()){
				series.add(BookSeries.fromCursor(c));
			}
		}finally{
			if( c != null )c.close();
		}
		
		Runnable r = new Runnable() {
			@Override
			public void run() {
				for (BookSeries bs : series) {
					try {
						BookSearchResponse res = api.requestTitle(bs.getTitle().replace("/", " "), bs.getAuthor().replace("/", " "), 1);
						for (BookInfo bi : res.getItems()) {
							bi.parseTitle();
							if( bi.isValidIsbn() && bi.getBaseTitle().equals(bs.getTitle()) ){
								Log.d(TAG,"update : title : " + bi.toShortString() );
								BookInfo inDb = getBookInfo(bi.getIsbn());
								if(  inDb == null ){
									Log.d(TAG,"update : found new item : " + bi.toShortString());
									context.getContentResolver().insert(Contract.Follows.buildFollowBookUri(bs.getSeriesId()), bi.toContentValues());
								}
							}else{
								Log.d(TAG,"update : exclude title : " + bi.toShortString() );
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		
		new Thread(r).start();

	}
	
	
	BookInfo getBookInfo(String isbn){
		Cursor c = null;
		try{
			c = context.getContentResolver().query(Contract.Books.ContentUri, null, Contract.Books.Columns.COL_ISBN + "=?", new String[]{isbn}, null);
			if( c.moveToFirst() ){
				return BookInfo.fromCursor(c);
			}
		}finally{
			if( c != null )c.close();
		}
		return null;
	}
}
