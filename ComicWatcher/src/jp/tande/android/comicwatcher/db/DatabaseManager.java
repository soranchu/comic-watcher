package jp.tande.android.comicwatcher.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class DatabaseManager {
	private static final String TAG = "DatabaseManager";

	public static final String TABLE_FOLLOWS = "follows";
	public static final String COL_ID = "_id";
	public static final String COL_TITLE = "title";
	public static final String COL_AUTHOR = "author";
	public static final String COL_PUBLISHER = "publisher";
	public static final String COL_LAST_UPDATE = "last_update";
	public static final String COL_VOLUMES = "volumes";
	public static final String COL_OWNED_COUNT = "owned_count";
	public static final String COL_TOTAL_NOT_IGNORED_COUNT = "total_count";

	public static final String TABLE_BOOKS = "books";
	//title
	public static final String COL_TITLE_POST_FIX = "title_post_fix";
	public static final String COL_SUB_TITLE = "sub_title";
	public static final String COL_SERIES = "series";
	//author
	//publisher
	public static final String COL_ISBN = "isbn";
	public static final String COL_SALES_DATE = "sales_date";
	public static final String COL_URL = "url";
	public static final String COL_AFF_URL = "affiliate_url";
	public static final String COL_IMAGE_URL = "image_url";
	public static final String COL_AVAILABILITY = "availability";
	//last_update
	public static final String COL_VOLUME = "volume";
	public static final String COL_FLAG_HIDE = "hide";
	public static final String COL_FLAG_OWNED = "owned";

	public static final String TABLE_FOLLOWS_BOOKS = "follows_books";
	public static final String COL_FOLLOWS_ID = "follows_id";
	public static final String COL_BOOKS_ID = "books_id";

	private class DatabaseHelper extends SQLiteOpenHelper {
		private static final String TAG = "DatabaseHelper";
		
		private static final String DB_NAME = "database.db";
		private static final int VERSION = 4;

		
		public DatabaseHelper(Context context) {
			super(context, DB_NAME, null, VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d(TAG, "creating tables...");
			db.execSQL(
					"create table " + TABLE_FOLLOWS +" ("
					+ COL_ID + " integer primary key autoincrement not null, "
					+ COL_TITLE + " text not null, "
					+ COL_AUTHOR + " text, "
					+ COL_PUBLISHER + " text, "
					+ COL_LAST_UPDATE + " long not null, "
					+ COL_VOLUMES + " integer,"
					+ COL_OWNED_COUNT + " integer default 0," 
					+ COL_TOTAL_NOT_IGNORED_COUNT + " integer default 0)" );
			db.execSQL(
					"create table " + TABLE_BOOKS +" ("	
					+ COL_ID + " integer primary key autoincrement not null, "
					+ COL_TITLE + " text not null, "
					+ COL_SUB_TITLE + " text, "
					+ COL_SERIES + " text, "
					+ COL_AUTHOR + " text, "
					+ COL_PUBLISHER + " text, "
					+ COL_ISBN + " integer unique not null, "
					+ COL_SALES_DATE + " text, "
					+ COL_URL + " text, "
					+ COL_AFF_URL + " text, "
					+ COL_AVAILABILITY + " integer default 0, "
					+ COL_IMAGE_URL + " text, "
					+ COL_FLAG_HIDE + " integer default 0, "
					+ COL_FLAG_OWNED + " integer default 0, "
					+ COL_LAST_UPDATE + " long not null, "
					+ COL_VOLUME + " integer )" );
			db.execSQL(
					"create table " + TABLE_FOLLOWS_BOOKS +" ("	
					+ COL_ID + " integer primary key autoincrement not null, "
					+ COL_FOLLOWS_ID + " integer not null, "
					+ COL_BOOKS_ID + " integer not null )");
					
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d(TAG, "upgrading tables from " + oldVersion + " to " + newVersion);
			
			Log.d(TAG, "dropping tables...");
			db.execSQL("drop table if exists " + TABLE_FOLLOWS );
			db.execSQL("drop table if exists " + TABLE_BOOKS );
			db.execSQL("drop table if exists " + TABLE_FOLLOWS_BOOKS );
			
			onCreate(db);

		}

	}

	
	private DatabaseHelper helper;
	private Context context;
	
	private static DatabaseManager instance = null;
	public static DatabaseManager getInstance(){
		return instance;
	}
	public static void initializeInstance(Context context){
		if( instance == null ){
			instance = new DatabaseManager(context);
		}else{
			Log.w(TAG, "DatabaseManager already initialized!");
		}
	}
	
	private DatabaseManager(Context context){
		this.context = context;
		this.helper = new DatabaseHelper(context);
	}
	
	public long addBookSeries(BookSeries bs){
		SQLiteDatabase db = helper.getWritableDatabase();
		boolean update = false;
		ContentValues v = new ContentValues();
		if( bs.getSeriesId() > 0 ){
			v.put(COL_ID, bs.getSeriesId());
			update = true;
		}
		v.put(COL_TITLE, bs.getTitle());
		v.put(COL_AUTHOR, bs.getAuthor());
		v.put(COL_PUBLISHER, bs.getPublisher());
		v.put(COL_LAST_UPDATE, new Date().getTime());
		v.put(COL_VOLUMES, bs.getBooks().size());
		v.put(COL_OWNED_COUNT, bs.getOwnedCount());
		v.put(COL_TOTAL_NOT_IGNORED_COUNT, bs.getTotalCount());
		db.beginTransaction();
		long ret = 0;
		try{
			Log.d(TAG,"inserting into " +TABLE_FOLLOWS + " values:" + v.toString());
			ret = db.replace(TABLE_FOLLOWS, null, v);
			if( bs.getBooks() != null ){
				for (BookInfo bi : bs.getBooks()) {
					addBookAsSeries(db, bi,ret, update);
				}
			}
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
		bs.setSeriesId(ret);
		return ret;
	}
	
	public void removeBookSeries(long seriesId){
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete(TABLE_FOLLOWS, BaseColumns._ID + "=?", new String[]{ String.valueOf(seriesId) });
	}

	private void addBookAsSeries(SQLiteDatabase db, BookInfo bi, long seriesId, boolean isUpdate) {
		ContentValues v = new ContentValues();
		if( bi.getBookId() > 0 ){
			v.put(COL_ID, bi.getBookId());
		}else{
			isUpdate = false;
		}
		
		v.put(COL_TITLE, bi.getTitle());
		v.put(COL_SUB_TITLE, bi.getSubTitle());
		v.put(COL_SERIES, bi.getSeriesName());
		v.put(COL_AUTHOR, bi.getAuthor());
		v.put(COL_PUBLISHER, bi.getPublisherName());
		v.put(COL_ISBN, bi.getIsbn());
		v.put(COL_SALES_DATE, bi.getSalesDate());
		v.put(COL_URL, bi.getItemUrl());
		v.put(COL_AFF_URL, bi.getAffiliateUrl());
		v.put(COL_IMAGE_URL, bi.getMediumImageUrl());
		v.put(COL_AVAILABILITY, bi.getAvailability());
		v.put(COL_FLAG_HIDE, bi.isHidden());
		v.put(COL_FLAG_OWNED, bi.isOwned());
		v.put(COL_LAST_UPDATE, new Date().getTime());
		v.put(COL_VOLUME, bi.getVolume());
		Log.d(TAG,"inserting into " +TABLE_BOOKS + " values:" + v.toString());
		long booksId = db.replace(TABLE_BOOKS, null, v);
		bi.setBookId(booksId);
		v.clear();
		v.put(COL_FOLLOWS_ID, seriesId);
		v.put(COL_BOOKS_ID, booksId);
		
		if( ! isUpdate ){
			Log.d(TAG,"inserting into " +TABLE_FOLLOWS_BOOKS + " values:" + v.toString());
			db.insert(TABLE_FOLLOWS_BOOKS, null, v);
		}
	}
	
	public List<BookSeries> querySeries(){
		List<BookSeries> series = new ArrayList<BookSeries>();
		
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = null;
		try{
			c = db.query(TABLE_FOLLOWS, /*cols*/null, 
					/*selection*/null, /*selectionArgs*/null, 
					/*groupBy*/null, /*having*/null, /*orderBy*/null);
			//c.moveToFirst();
			while( c.moveToNext() ){
				BookSeries bs = new BookSeries(
						c.getLong(c.getColumnIndex(COL_ID)),
						c.getString(c.getColumnIndex(COL_TITLE)),
						c.getString(c.getColumnIndex(COL_PUBLISHER)),
						c.getString(c.getColumnIndex(COL_AUTHOR)),
						c.getInt(c.getColumnIndex(COL_OWNED_COUNT)),
						c.getInt(c.getColumnIndex(COL_TOTAL_NOT_IGNORED_COUNT))
						);
				series.add(bs);
			}
		}finally{
			if( c!= null ){
				c.close();
			}
		}
		return series;
	}
	public void populateSeries(BookSeries bs) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = null;
		try{
			c = db.rawQuery("select *" +
//					" B." + COL_ID + " as " + COL_ID + "," +
					" from " + TABLE_BOOKS + " as B " +
					" left join " + TABLE_FOLLOWS_BOOKS + " as FB on B."+COL_ID +  "= FB." + COL_BOOKS_ID +
					" where FB." + COL_FOLLOWS_ID + " = ?"
					, new String[]{""+bs.getSeriesId()} );
			Log.d(TAG,"books count:" + c.getCount());
			Log.d(TAG,"books cols:" + Arrays.deepToString(c.getColumnNames()));
			
			while( c.moveToNext() ){
				BookInfo bi = new BookInfo();
				bi.setItem(new BookInfo.Item());
				bi.setBookId(c.getLong(c.getColumnIndex(COL_ID)));
				bi.setTitle(c.getString(c.getColumnIndex(COL_TITLE)));
				bi.setSubTitle(c.getString(c.getColumnIndex(COL_SUB_TITLE)));
				bi.setSeriesName(c.getString(c.getColumnIndex(COL_SERIES)));
				bi.setAuthor(c.getString(c.getColumnIndex(COL_AUTHOR)));
				bi.setPublisherName(c.getString(c.getColumnIndex(COL_PUBLISHER)));
				bi.setIsbn(c.getString(c.getColumnIndex(COL_ISBN)));
				bi.setSalesDate(c.getString(c.getColumnIndex(COL_SALES_DATE)));
				bi.setItemUrl(c.getString(c.getColumnIndex(COL_URL)));
				bi.setAffiliateUrl(c.getString(c.getColumnIndex(COL_AFF_URL)));
				bi.setMediumImageUrl(c.getString(c.getColumnIndex(COL_IMAGE_URL)));
				bi.setAvailability(c.getString(c.getColumnIndex(COL_AVAILABILITY)));
				bi.setHidden(1==c.getInt(c.getColumnIndex(COL_FLAG_HIDE)));
				bi.setOwned(1==c.getInt(c.getColumnIndex(COL_FLAG_OWNED)));
				//bi.set(c.getString(c.getColumnIndex(COL_LAST_UPDATE)));
				bi.setVolume(c.getInt(c.getColumnIndex(COL_VOLUME)));

				bs.addBook(bi);
			}
		}finally{
			if( c!= null ){
				c.close();
			}
		}
		return;
	}
	
}
