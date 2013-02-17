package jp.tande.android.comicwatcher.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

public class DatabaseManager extends ContentProvider{
	private static final String TAG = "DatabaseManager";
	private static final String PKG = "jp.tande.android.comicwatcher";
	private static final String BaseSingleMimeType = "vnd.android.cursor.item/";
	private static final String BaseMultiMimeType = "vnd.android.cursor.dir/";

	private static final String AUTHORITY = PKG + ".provider";

	public static class Contract{
		public static class Follows {
			public static final String TABLE = "follows";
			public static final Uri ContentUri = Uri.parse("content://" + AUTHORITY + "/" + TABLE);
			public static final String MimeType = BaseSingleMimeType+  PKG + "." + TABLE;
			public static final String MimeTypes = BaseMultiMimeType + PKG + "." + TABLE;

			public static class Columns implements BaseColumns{
				public static final String COL_TITLE = "title";
				public static final String COL_AUTHOR = "author";
				public static final String COL_PUBLISHER = "publisher";
				public static final String COL_LAST_UPDATE = "last_update";
				public static final String COL_VOLUMES = "volumes";
				public static final String COL_OWNED_COUNT = "owned_count";
				public static final String COL_IMAGE_URL = "image_url";
				public static final String COL_LATEST_SALES_DATE = "latest_sales_date";
				public static final String COL_AVAILABILITY = "availability";
				public static final String COL_TOTAL_NOT_IGNORED_COUNT = "total_count";
			}
		}
		public static class Books{
			public static final String TABLE = "books";
			public static final Uri ContentUri = Uri.parse("content://" + AUTHORITY + "/" + TABLE);
			public static final String MimeType = BaseSingleMimeType+  PKG + "." + TABLE;
			public static final String MimeTypes = BaseMultiMimeType + PKG + "." + TABLE;
			public static class Columns implements BaseColumns{
				public static final String COL_TITLE = "title";
				public static final String COL_TITLE_POST_FIX = "title_post_fix";
				public static final String COL_SUB_TITLE = "sub_title";
				public static final String COL_SERIES = "series";
				public static final String COL_AUTHOR = "author";
				public static final String COL_PUBLISHER = "publisher";
				public static final String COL_ISBN = "isbn";
				public static final String COL_SALES_DATE = "sales_date";
				public static final String COL_URL = "url";
				public static final String COL_AFF_URL = "affiliate_url";
				public static final String COL_IMAGE_URL = "image_url";
				public static final String COL_AVAILABILITY = "availability";
				public static final String COL_LAST_UPDATE = "last_update";
				public static final String COL_VOLUME = "volume";
				public static final String COL_FLAG_HIDE = "hide";
				public static final String COL_FLAG_OWNED = "owned";
			}
		}
		public static class FollowsBooks{
			public static final String TABLE = "follows_books";
			public static final Uri ContentUri = Uri.parse("content://" + AUTHORITY + "/" + TABLE);
			public static final String MimeType = BaseSingleMimeType+  PKG + "." + TABLE;
			public static final String MimeTypes = BaseMultiMimeType + PKG + "." + TABLE;
			public static class Columns implements BaseColumns{
				public static final String COL_FOLLOWS_ID = "follows_id";
				public static final String COL_BOOKS_ID = "books_id";
			}
		}
		
         
	}
	
	private static final int MATCH_FOLLOWS = 1;
	private static final int MATCH_FOLLOWS_ID = 2;
	private static final int MATCH_BOOKS = 3;
	private static final int MATCH_BOOKS_ID = 4;
	private static final int MATCH_FOLLOWS_BOOKS = 5;
	private static final int MATCH_FOLLOWS_BOOKS_ID = 6;
	
	private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(AUTHORITY, Contract.Follows.TABLE, MATCH_FOLLOWS);
        sUriMatcher.addURI(AUTHORITY, Contract.Follows.TABLE + "/#", MATCH_FOLLOWS_ID);
        sUriMatcher.addURI(AUTHORITY, Contract.Books.TABLE, MATCH_BOOKS);
        sUriMatcher.addURI(AUTHORITY, Contract.Books.TABLE + "/#", MATCH_BOOKS_ID);
        sUriMatcher.addURI(AUTHORITY, Contract.Follows.TABLE + "/#/"+ Contract.Books.TABLE, MATCH_FOLLOWS_BOOKS);
        sUriMatcher.addURI(AUTHORITY, Contract.Follows.TABLE + "/#/"+ Contract.Books.TABLE + "/#", MATCH_FOLLOWS_BOOKS_ID);
    }

	private class DatabaseHelper extends SQLiteOpenHelper {
		@SuppressWarnings("hiding")
		private static final String TAG = "DatabaseHelper";
		
		private static final String DB_NAME = "database.db";
		private static final int VERSION = 6;

		
		public DatabaseHelper(Context context) {
			super(context, DB_NAME, null, VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d(TAG, "creating tables...");
			db.execSQL(
					"create table " + Contract.Follows.TABLE +" ("
					+ Contract.Follows.Columns._ID + " integer primary key autoincrement not null, "
					+ Contract.Follows.Columns.COL_TITLE + " text not null, "
					+ Contract.Follows.Columns.COL_AUTHOR + " text, "
					+ Contract.Follows.Columns.COL_PUBLISHER + " text, "
					+ Contract.Follows.Columns.COL_LAST_UPDATE + " long not null, "
					+ Contract.Follows.Columns.COL_VOLUMES + " integer,"
					+ Contract.Follows.Columns.COL_OWNED_COUNT + " integer default 0," 
					+ Contract.Follows.Columns.COL_IMAGE_URL + " text, "
					+ Contract.Follows.Columns.COL_LATEST_SALES_DATE + " text, "
					+ Contract.Follows.Columns.COL_AVAILABILITY + " integer default 0, "
					+ Contract.Follows.Columns.COL_TOTAL_NOT_IGNORED_COUNT + " integer default 0)" );
			db.execSQL(
					"create table " + Contract.Books.TABLE +" ("	
					+ Contract.Books.Columns._ID + " integer primary key autoincrement not null, "
					+ Contract.Books.Columns.COL_TITLE + " text not null, "
					+ Contract.Books.Columns.COL_SUB_TITLE + " text, "
					+ Contract.Books.Columns.COL_SERIES + " text, "
					+ Contract.Books.Columns.COL_AUTHOR + " text, "
					+ Contract.Books.Columns.COL_PUBLISHER + " text, "
					+ Contract.Books.Columns.COL_ISBN + " integer unique not null, "
					+ Contract.Books.Columns.COL_SALES_DATE + " text, "
					+ Contract.Books.Columns.COL_URL + " text, "
					+ Contract.Books.Columns.COL_AFF_URL + " text, "
					+ Contract.Books.Columns.COL_AVAILABILITY + " integer default 0, "
					+ Contract.Books.Columns.COL_IMAGE_URL + " text, "
					+ Contract.Books.Columns.COL_FLAG_HIDE + " integer default 0, "
					+ Contract.Books.Columns.COL_FLAG_OWNED + " integer default 0, "
					+ Contract.Books.Columns.COL_LAST_UPDATE + " long not null, "
					+ Contract.Books.Columns.COL_VOLUME + " integer )" );
			db.execSQL(
					"create table " + Contract.FollowsBooks.TABLE +" ("	
					+ Contract.FollowsBooks.Columns._ID + " integer primary key autoincrement not null, "
					+ Contract.FollowsBooks.Columns.COL_FOLLOWS_ID + " integer not null, "
					+ Contract.FollowsBooks.Columns.COL_BOOKS_ID + " integer not null )");
					
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d(TAG, "upgrading tables from " + oldVersion + " to " + newVersion);
			
			Log.d(TAG, "dropping tables...");
			db.execSQL("drop table if exists " + Contract.Follows.TABLE );
			db.execSQL("drop table if exists " + Contract.Books.TABLE );
			db.execSQL("drop table if exists " + Contract.FollowsBooks.TABLE );
			
			onCreate(db);
		}
	}
	
	private DatabaseHelper helper;
	
	@Override
	public boolean onCreate() {
		this.helper = new DatabaseHelper(getContext());
		return true;
	}
	
	private int matchUri(Uri uri){
		int match = sUriMatcher.match(uri);
		if( match == -1 ){
			throw new UnsupportedOperationException("unknown uri :" +uri.toString());
		}
		return match;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int match = matchUri(uri);
		switch(match){
		case MATCH_FOLLOWS:
		case MATCH_FOLLOWS_ID:
			return insertFollows(uri, values);
/*		case MATCH_BOOKS:
		case MATCH_BOOKS_ID:
			return insertBooks(uri, values);*/
		case MATCH_FOLLOWS_BOOKS:
		case MATCH_FOLLOWS_BOOKS_ID:
			return insertBooks(uri, values);
		}
		throw new UnsupportedOperationException("unknown uri :" +uri.toString());
	}
	
	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		int match = matchUri(uri);
		switch(match){
		case MATCH_FOLLOWS:
		case MATCH_FOLLOWS_ID:
			return super.bulkInsert(uri, values);
/*		case MATCH_BOOKS:
		case MATCH_BOOKS_ID:
			return insertBooks(uri, values);*/
		case MATCH_FOLLOWS_BOOKS:
		case MATCH_FOLLOWS_BOOKS_ID:
			return insertBooks(uri, values);
		}
		throw new UnsupportedOperationException("unknown uri :" +uri.toString());
	}
	
	private int insertBooks(Uri uri, ContentValues[] values) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.beginTransaction();
		int count = 0;
		try{
			for (ContentValues v : values) {
				insertBooks(uri, v, db);
				count++;
			}
			updateFollowsLatest(uri.getPathSegments().get(1), db);
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
		return count;
	}

	private Uri insertBooks(Uri uri, ContentValues v) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.beginTransaction();
		Uri ret = null;
		try{
			ret = insertBooks(uri, v, db);
			updateFollowsLatest(uri.getPathSegments().get(1), db);
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
		return ret;
	}
	
	private Uri insertBooks(Uri uri, ContentValues v, SQLiteDatabase db){

		Log.d(TAG,"inserting into " +Contract.Books.TABLE + " values:" + v.toString());
		long booksId = db.replace(Contract.Books.TABLE, null, v);
		getContext().getContentResolver().notifyChange(ContentUris.withAppendedId(Contract.Books.ContentUri, booksId), null);
		
		ContentValues fb = new ContentValues();
		fb.put(Contract.FollowsBooks.Columns.COL_FOLLOWS_ID, uri.getPathSegments().get(1));
		fb.put(Contract.FollowsBooks.Columns.COL_BOOKS_ID, booksId);
		
		Log.d(TAG,"inserting into " +Contract.FollowsBooks.TABLE + " values:" + fb.toString());
		db.insert(Contract.FollowsBooks.TABLE, null, fb);
		Uri ret = ContentUris.withAppendedId(uri, booksId);
		getContext().getContentResolver().notifyChange(ret, null);
		
		//TODO update follows row:latestVolume,latestSalesDate and notifyChanges()
			
		return ret;
	}
	
	private void updateFollowsLatest(String followsId, SQLiteDatabase db){
		String query = "select " +
					"B." +Contract.Books.Columns.COL_VOLUME + 
					", B." + Contract.Books.Columns.COL_SALES_DATE +
					", B." + Contract.Books.Columns.COL_IMAGE_URL +
					", B." + Contract.Books.Columns.COL_AVAILABILITY +
				" from " + Contract.Books.TABLE + " as B " +
				" left join " + Contract.FollowsBooks.TABLE + " as FB " +
						" on B."+Contract.Books.Columns._ID + "= FB." + Contract.FollowsBooks.Columns.COL_BOOKS_ID +
				" where FB." + Contract.FollowsBooks.Columns.COL_FOLLOWS_ID + " = ?" +
				" order by B." + Contract.Books.Columns.COL_VOLUME + " DESC" +
				" limit 1";
		String queryOwned = "select sum(B.owned) AS owned, count(1) - sum(B.hide) AS row_count" +
				" from " + Contract.Books.TABLE + " as B " +
				" left join " + Contract.FollowsBooks.TABLE + " as FB " +
						" on B."+Contract.Books.Columns._ID + "= FB." + Contract.FollowsBooks.Columns.COL_BOOKS_ID +
				" where FB." + Contract.FollowsBooks.Columns.COL_FOLLOWS_ID + " = ?";
		
		Cursor latestCursor = db.rawQuery(query, new String[]{followsId} );
		Cursor ownedCursor = db.rawQuery(queryOwned, new String[]{followsId} );
		Log.d(TAG,"updateFollowsLatest : " + query + " ret:" + latestCursor.getCount());
		if ( latestCursor.moveToFirst() && ownedCursor.moveToFirst() ){
			ContentValues v = new ContentValues();
			v.put(Contract.Follows.Columns.COL_LATEST_SALES_DATE, latestCursor.getString(latestCursor.getColumnIndex(Contract.Books.Columns.COL_SALES_DATE)));
			v.put(Contract.Follows.Columns.COL_VOLUMES, latestCursor.getInt(latestCursor.getColumnIndex(Contract.Books.Columns.COL_VOLUME)));
			v.put(Contract.Follows.Columns.COL_AVAILABILITY, latestCursor.getInt(latestCursor.getColumnIndex(Contract.Books.Columns.COL_AVAILABILITY)));
			v.put(Contract.Follows.Columns.COL_IMAGE_URL, latestCursor.getString(latestCursor.getColumnIndex(Contract.Books.Columns.COL_IMAGE_URL)));
			v.put(Contract.Follows.Columns.COL_OWNED_COUNT, ownedCursor.getInt(ownedCursor.getColumnIndex("owned")));
			v.put(Contract.Follows.Columns.COL_TOTAL_NOT_IGNORED_COUNT, ownedCursor.getInt(ownedCursor.getColumnIndex("row_count")));
			
			db.update(Contract.Follows.TABLE, v, BaseColumns._ID + " = ?" , new String[]{followsId});
			Uri returnUri = ContentUris.withAppendedId(Contract.Follows.ContentUri, Long.parseLong(followsId));
			getContext().getContentResolver().notifyChange(returnUri, null);
		}
	}

	private Uri insertFollows(Uri uri, ContentValues v) {
		SQLiteDatabase db = helper.getWritableDatabase();

		db.beginTransaction();
		long ret = 0;
		try{
			Log.d(TAG,"inserting into " +Contract.Follows.TABLE + " values:" + v.toString());
			ret = db.replace(Contract.Follows.TABLE, null, v);
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
		
	
		Uri returnUri = ContentUris.withAppendedId(Contract.Follows.ContentUri, ret);
		getContext().getContentResolver().notifyChange(returnUri, null);
		return returnUri;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		int match = matchUri(uri);
		switch(match){
		case MATCH_FOLLOWS:
		case MATCH_FOLLOWS_ID:
			return queryFollows(uri, projection, selection, selectionArgs, sortOrder);
		case MATCH_BOOKS:
		case MATCH_BOOKS_ID:
			return queryBooks(uri, projection, selection, selectionArgs, sortOrder);
		case MATCH_FOLLOWS_BOOKS:
		case MATCH_FOLLOWS_BOOKS_ID:
			return queryFollowBooks(uri, projection, selection, selectionArgs, sortOrder);
		}
		throw new UnsupportedOperationException("not supported query uri:" + uri.toString() );
	}
	
	private String buildSelection(Uri uri, String baseSelection){
		if( uri.getPathSegments().size() > 1 ){
			if( baseSelection != null ){
				return BaseColumns._ID + " = ? AND (" + baseSelection +")";
			}else{
				return BaseColumns._ID + " = ?";
			}
		}
		return baseSelection;
	}
	
	private String[] buildSelectionArgs(Uri uri, String[] baseSelectionArgs){
		if( uri.getPathSegments().size() > 1 ){
			if( baseSelectionArgs != null ){
				String[] args = new String[baseSelectionArgs.length+1];
				args[0] = uri.getPathSegments().get(1);
				System.arraycopy(baseSelectionArgs, 0, args, 1, baseSelectionArgs.length);
				return args;
			}else{
				return new String[]{uri.getPathSegments().get(1)};
			}
		}
		return baseSelectionArgs;
	}
	
	private Cursor queryFollowBooks(Uri uri, String[] projection,
			String selection, String[] selectionArgs, String sortOrder) {

		//TODO ignore projection, selection and sort
		String followsId = uri.getPathSegments().get(1);
		
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = null;
		c = db.rawQuery("select *" +
//				" B." + COL_ID + " as " + COL_ID + "," +
				" from " + Contract.Books.TABLE + " as B " +
				" left join " + Contract.FollowsBooks.TABLE + " as FB " +
						" on B."+Contract.Books.Columns._ID +  "= FB." + Contract.FollowsBooks.Columns.COL_BOOKS_ID +
				" where FB." + Contract.FollowsBooks.Columns.COL_FOLLOWS_ID + " = ?" +
						" AND B." +Contract.Books.Columns.COL_FLAG_HIDE + " = ?"
				, new String[]{followsId,"0"} );
		Log.d(TAG,"books count:" + c.getCount());
		Log.d(TAG,"books cols:" + Arrays.deepToString(c.getColumnNames()));
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	private Cursor queryBooks(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = null;
		c = db.query(Contract.Books.TABLE, projection, buildSelection(uri, selection), buildSelectionArgs(uri, selectionArgs), 
				/*groupBy*/null, /*having*/null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	private Cursor queryFollows(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = null;
		c = db.query(Contract.Follows.TABLE, projection, buildSelection(uri, selection), buildSelectionArgs(uri, selectionArgs), 
				/*groupBy*/null, /*having*/null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int match = matchUri(uri);
		switch(match){
		//case MATCH_FOLLOWS:
		case MATCH_FOLLOWS_ID:
			return updateFollows(uri, values, selection, selectionArgs);
		//case MATCH_BOOKS:
		case MATCH_BOOKS_ID:
		case MATCH_FOLLOWS_BOOKS_ID:
			return updateBooks(uri, values, selection, selectionArgs);
		case MATCH_FOLLOWS_BOOKS:
			//return updateFollowBooks(uri, values, selection, selectionArgs);
		}
		throw new UnsupportedOperationException("not supported update uri:" + uri.toString() );
	}
	/*
	private int updateFollowBooks(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = helper.getWritableDatabase();
		
		String followId = uri.getPathSegments().get(1);
		Cursor c = db.query(Contract.FollowsBooks.TABLE, null, 
				Contract.FollowsBooks.Columns.COL_FOLLOWS_ID + "= ?", 
				new String[]{followId}, null, null, null);
		int count = 0;
		while( c.moveToNext() ){
			count += db.update(Contract.Books.TABLE, values, appendSelectionClause(selection, BaseColumns._ID + " = ?"), 
					appendSelectionArgs(selectionArgs, String.valueOf( c.getInt(c.getColumnIndex(BaseColumns._ID))) ) );
		}
		Log.d(TAG,"updateFollowBooks : " + uri + " v:" + values + " ret:"+count);
		if( count > 0 ){
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return count;
	}
*/
	private int updateBooks(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int count;
		count = db.update(Contract.Books.TABLE, values, buildSelection(uri, selection), buildSelectionArgs(uri, selectionArgs) );
		Log.d(TAG,"updateBooks : " + uri + " v:" + values + " ret:"+count);
		if( count > 0 ){
			getContext().getContentResolver().notifyChange(uri, null);
			String bookId = uri.getPathSegments().get(1);
			Cursor c = db.query(Contract.FollowsBooks.TABLE, null, 
					Contract.FollowsBooks.Columns.COL_BOOKS_ID + "= ?", 
					new String[]{bookId}, null, null, null);
			while( c.moveToNext() ){
				updateFollowsLatest( String.valueOf(c.getInt(c.getColumnIndex(Contract.FollowsBooks.Columns.COL_FOLLOWS_ID))), db);
			}
		}
		return count;
	}

	private int updateFollows(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int count;
		count = db.update(Contract.Follows.TABLE, values, buildSelection(uri, selection), buildSelectionArgs(uri, selectionArgs) );
		Log.d(TAG,"updateFollows : " + uri + " v:" + values + " ret:"+count);
		if( count > 0 ){
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return count;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int match = matchUri(uri);
		switch(match){
		case MATCH_FOLLOWS:
		case MATCH_FOLLOWS_ID:
			return deleteFollows(uri, selection, selectionArgs);
		/*case MATCH_BOOKS:*/
		case MATCH_BOOKS_ID:
			return deleteBooks(uri, selection, selectionArgs);
		case MATCH_FOLLOWS_BOOKS:
		case MATCH_FOLLOWS_BOOKS_ID:
			return deleteFollowBooks(uri, selection, selectionArgs);
		}
		throw new UnsupportedOperationException("not supported delete uri:" + uri.toString() );
	}
	private int deleteFollowBooks(Uri uri, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
		
	}

	private int deleteBooks(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = helper.getWritableDatabase();
		List<String> paths = uri.getPathSegments();
		if( paths.size() > 1 ){
			db.delete(Contract.Books.TABLE, 
					appendSelectionClause(selection, BaseColumns._ID + "=?"), 
					appendSelectionArgs(selectionArgs, paths.get(1)) );
			Cursor c = db.query(Contract.FollowsBooks.TABLE, new String[]{Contract.FollowsBooks.Columns.COL_FOLLOWS_ID}, 
					Contract.FollowsBooks.Columns.COL_BOOKS_ID, new String[]{paths.get(1)}, null, null, null);
			List<Long> follows = new ArrayList<Long>();
			while( c.moveToNext() ){
				follows.add(c.getLong(0));
			}
			db.delete(Contract.FollowsBooks.TABLE, Contract.FollowsBooks.Columns.COL_BOOKS_ID, new String[]{paths.get(1)});
			for (Long l : follows) {
				getContext().getContentResolver().notifyChange(ContentUris.withAppendedId(Contract.Follows.ContentUri, l), null);

			}
			getContext().getContentResolver().notifyChange(uri,null);
			return 1;
		}else{
			throw new UnsupportedOperationException("not supported uri:" + uri.toString() );
		}
	}

	private int deleteFollows(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = helper.getWritableDatabase();
		List<String> paths = uri.getPathSegments();
		int changed;
		if( paths.size() > 1 ){
			changed = db.delete(Contract.Follows.TABLE, 
					appendSelectionClause(selection, BaseColumns._ID + "=?"), 
					appendSelectionArgs(selectionArgs, paths.get(1)) );
		}else{
			changed = db.delete(Contract.Follows.TABLE, selection, selectionArgs);
		}
		if( changed > 0 ){
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return changed;
	}

	private String appendSelectionClause(String selection, String append){
		if( selection == null || selection.isEmpty()){
			return append;
		}
		return selection + " AND (" + append + ")";
	}
	
	private String[] appendSelectionArgs(String[] selectionArgs, String append){
		ArrayList<String> args = new ArrayList<String>();
		if( selectionArgs == null || selectionArgs.length == 0 ){
			return new String[]{append};
		}
		args.addAll(Arrays.asList(selectionArgs) );
		args.add(append);
		return args.toArray(new String[]{});
	}
	
	@Override
	public String getType(Uri uri) {
		int match = matchUri(uri);
		switch(match){
		case MATCH_FOLLOWS:
			return Contract.Follows.MimeTypes;
		case MATCH_FOLLOWS_ID:
			return Contract.Follows.MimeType;
		case MATCH_BOOKS:
			return Contract.Books.MimeTypes;
		case MATCH_BOOKS_ID:
			return Contract.Books.MimeType;
		case MATCH_FOLLOWS_BOOKS:
			return Contract.FollowsBooks.MimeTypes;
		case MATCH_FOLLOWS_BOOKS_ID:
			return Contract.FollowsBooks.MimeType;
		}
		return null;
	}

	@Deprecated
	public long addBookSeries(BookSeries bs){
		SQLiteDatabase db = helper.getWritableDatabase();
		boolean update = false;
		ContentValues v = new ContentValues();
		if( bs.getSeriesId() > 0 ){
			v.put(Contract.Follows.Columns._ID, bs.getSeriesId());
			update = true;
		}
		v.put(Contract.Follows.Columns.COL_TITLE, bs.getTitle());
		v.put(Contract.Follows.Columns.COL_AUTHOR, bs.getAuthor());
		v.put(Contract.Follows.Columns.COL_PUBLISHER, bs.getPublisher());
		v.put(Contract.Follows.Columns.COL_LAST_UPDATE, new Date().getTime());
		v.put(Contract.Follows.Columns.COL_VOLUMES, bs.getBooks().size());
		v.put(Contract.Follows.Columns.COL_OWNED_COUNT, bs.getOwnedCount());
		v.put(Contract.Follows.Columns.COL_TOTAL_NOT_IGNORED_COUNT, bs.getTotalCount());
		db.beginTransaction();
		long ret = 0;
		try{
			Log.d(TAG,"inserting into " +Contract.Follows.TABLE + " values:" + v.toString());
			ret = db.replace(Contract.Follows.TABLE, null, v);
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
	
	@Deprecated
	public void removeBookSeries(long seriesId){
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete(Contract.Follows.TABLE, BaseColumns._ID + "=?", new String[]{ String.valueOf(seriesId) });
	}

	@Deprecated
	private void addBookAsSeries(SQLiteDatabase db, BookInfo bi, long seriesId, boolean isUpdate) {
		ContentValues v = new ContentValues();
		if( bi.getBookId() > 0 ){
			v.put(Contract.Books.Columns._ID, bi.getBookId());
		}else{
			isUpdate = false;
		}
		
		v.put(Contract.Books.Columns.COL_TITLE, bi.getTitle());
		v.put(Contract.Books.Columns.COL_SUB_TITLE, bi.getSubTitle());
		v.put(Contract.Books.Columns.COL_SERIES, bi.getSeriesName());
		v.put(Contract.Books.Columns.COL_AUTHOR, bi.getAuthor());
		v.put(Contract.Books.Columns.COL_PUBLISHER, bi.getPublisherName());
		v.put(Contract.Books.Columns.COL_ISBN, bi.getIsbn());
		v.put(Contract.Books.Columns.COL_SALES_DATE, bi.getSalesDate());
		v.put(Contract.Books.Columns.COL_URL, bi.getItemUrl());
		v.put(Contract.Books.Columns.COL_AFF_URL, bi.getAffiliateUrl());
		v.put(Contract.Books.Columns.COL_IMAGE_URL, bi.getMediumImageUrl());
		v.put(Contract.Books.Columns.COL_AVAILABILITY, bi.getAvailability());
		v.put(Contract.Books.Columns.COL_FLAG_HIDE, bi.isHidden());
		v.put(Contract.Books.Columns.COL_FLAG_OWNED, bi.isOwned());
		v.put(Contract.Books.Columns.COL_LAST_UPDATE, new Date().getTime());
		v.put(Contract.Books.Columns.COL_VOLUME, bi.getVolume());
		Log.d(TAG,"inserting into " +Contract.Books.TABLE + " values:" + v.toString());
		long booksId = db.replace(Contract.Books.TABLE, null, v);
		bi.setBookId(booksId);
		v.clear();
		v.put(Contract.FollowsBooks.Columns.COL_FOLLOWS_ID, seriesId);
		v.put(Contract.FollowsBooks.Columns.COL_BOOKS_ID, booksId);
		
		if( ! isUpdate ){
			Log.d(TAG,"inserting into " +Contract.FollowsBooks.TABLE + " values:" + v.toString());
			db.insert(Contract.FollowsBooks.TABLE, null, v);
		}
	}
	
	@Deprecated
	public List<BookSeries> querySeries(){
		List<BookSeries> series = new ArrayList<BookSeries>();
		
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = null;
		try{
			c = db.query(Contract.Follows.TABLE, /*cols*/null, 
					/*selection*/null, /*selectionArgs*/null, 
					/*groupBy*/null, /*having*/null, /*orderBy*/null);
			//c.moveToFirst();
			while( c.moveToNext() ){
				BookSeries bs = new BookSeries(
						c.getLong(c.getColumnIndex(Contract.Follows.Columns._ID)),
						c.getString(c.getColumnIndex(Contract.Follows.Columns.COL_TITLE)),
						c.getString(c.getColumnIndex(Contract.Follows.Columns.COL_PUBLISHER)),
						c.getString(c.getColumnIndex(Contract.Follows.Columns.COL_AUTHOR)),
						c.getInt(c.getColumnIndex(Contract.Follows.Columns.COL_OWNED_COUNT)),
						c.getInt(c.getColumnIndex(Contract.Follows.Columns.COL_TOTAL_NOT_IGNORED_COUNT))
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

	
	@Deprecated
	public void populateSeries(BookSeries bs) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = null;
		try{
			c = db.rawQuery("select *" +
//					" B." + COL_ID + " as " + COL_ID + "," +
					" from " + Contract.Books.TABLE + " as B " +
					" left join " + Contract.FollowsBooks.TABLE + " as FB " +
							" on B."+Contract.Books.Columns._ID +  "= FB." + Contract.FollowsBooks.Columns.COL_BOOKS_ID +
					" where FB." + Contract.FollowsBooks.Columns.COL_FOLLOWS_ID + " = ?"
					, new String[]{""+bs.getSeriesId()} );
			Log.d(TAG,"books count:" + c.getCount());
			Log.d(TAG,"books cols:" + Arrays.deepToString(c.getColumnNames()));
			
			while( c.moveToNext() ){
				BookInfo bi = new BookInfo();
				bi.setItem(new BookInfo.Item());
				bi.setBookId(c.getLong(c.getColumnIndex(Contract.Books.Columns._ID)));
				bi.setTitle(c.getString(c.getColumnIndex(Contract.Books.Columns.COL_TITLE)));
				bi.setSubTitle(c.getString(c.getColumnIndex(Contract.Books.Columns.COL_SUB_TITLE)));
				bi.setSeriesName(c.getString(c.getColumnIndex(Contract.Books.Columns.COL_SERIES)));
				bi.setAuthor(c.getString(c.getColumnIndex(Contract.Books.Columns.COL_AUTHOR)));
				bi.setPublisherName(c.getString(c.getColumnIndex(Contract.Books.Columns.COL_PUBLISHER)));
				bi.setIsbn(c.getString(c.getColumnIndex(Contract.Books.Columns.COL_ISBN)));
				bi.setSalesDate(c.getString(c.getColumnIndex(Contract.Books.Columns.COL_SALES_DATE)));
				bi.setItemUrl(c.getString(c.getColumnIndex(Contract.Books.Columns.COL_URL)));
				bi.setAffiliateUrl(c.getString(c.getColumnIndex(Contract.Books.Columns.COL_AFF_URL)));
				bi.setMediumImageUrl(c.getString(c.getColumnIndex(Contract.Books.Columns.COL_IMAGE_URL)));
				bi.setAvailability(c.getString(c.getColumnIndex(Contract.Books.Columns.COL_AVAILABILITY)));
				bi.setHidden(1==c.getInt(c.getColumnIndex(Contract.Books.Columns.COL_FLAG_HIDE)));
				bi.setOwned(1==c.getInt(c.getColumnIndex(Contract.Books.Columns.COL_FLAG_OWNED)));
				//bi.set(c.getString(c.getColumnIndex(Contract.Books.Columns.COL_LAST_UPDATE)));
				bi.setVolume(c.getInt(c.getColumnIndex(Contract.Books.Columns.COL_VOLUME)));

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
