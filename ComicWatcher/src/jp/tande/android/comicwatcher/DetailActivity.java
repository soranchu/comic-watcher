package jp.tande.android.comicwatcher;

import jp.tande.android.comicwatcher.adapters.DetailListAdapter;
import jp.tande.android.comicwatcher.adapters.DetailListArrayAdapter;
import jp.tande.android.comicwatcher.api.ImageLoader;
import jp.tande.android.comicwatcher.db.BookInfo;
import jp.tande.android.comicwatcher.db.BookSeries;
import jp.tande.android.comicwatcher.db.DatabaseManager.Contract;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DetailActivity extends FragmentActivity implements LoaderCallbacks<Cursor> {
	private static final String TAG ="DetailActivity";
	
	private TextView txtTitle;
	private TextView txtAuthor;
	private TextView txtSeries;
	private ImageView imgThumb;
	private ListView listDetail;
	private Button btnReserve;

	private BookSeries bookSeries;
	private ImageLoader loader;
	private DetailListAdapter detailListAdapter;
	private DetailListArrayAdapter detailListArrayAdapter;
	
	/**
	 * preview mode: showing search result ( not from database )
	 */
	private boolean isPreviewMode = true;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_top);
        txtTitle = (TextView) findViewById(R.id.txt_detail_comicTitle);
        txtAuthor = (TextView) findViewById(R.id.txt_detail_auther);
        txtSeries = (TextView) findViewById(R.id.txt_detail_publisher);
        imgThumb = (ImageView) findViewById(R.id.img_detail_comic_thumb);
        listDetail = (ListView) findViewById(R.id.list_detail);
        btnReserve= (Button) findViewById(R.id.btn_reserve);

        loader = ImageLoader.getInstance();
        
        /*
        listDetail.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				BookInfo bi = (BookInfo) listDetail.getItemAtPosition(arg2);
				String url = bi.getItemUrl();
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url) );
				startActivity(i);
			}
        });*/
        //listDetail.setDrawSelectorOnTop(true);
        registerForContextMenu(listDetail);
        
        Intent i = getIntent();
        bookSeries = (BookSeries) i.getExtras().getSerializable("data");
        txtTitle.setText(bookSeries.getTitle());
        txtAuthor.setText(bookSeries.getAuthor());
        txtSeries.setText(bookSeries.getPublisher());//TODO change to series
        Log.d(TAG,"onCreate : " + bookSeries);
        
        if( ! bookSeries.isPoplulated() ){
        	Log.d(TAG,"onCreate : using DetailListAdapter");
        	isPreviewMode = false;
            detailListAdapter = new DetailListAdapter(this, loader);
            listDetail.setAdapter(detailListAdapter);
            getSupportLoaderManager().initLoader(0, null, this);
        }else{
        	Log.d(TAG,"onCreate : using DetailListArrayAdapter");
        	isPreviewMode = true;
        	detailListArrayAdapter = new DetailListArrayAdapter(this, loader);
            listDetail.setAdapter(detailListArrayAdapter);
            
            for (BookInfo bi : bookSeries.getBooks()) {
    			 detailListArrayAdapter.add(bi);
    		}
        }

        if( bookSeries.getImageUrl() != null ){
        	String url = bookSeries.getImageUrl();
       		loader.queue(url, imgThumb );

        	if( bookSeries.isLatestOnSale() ){
        		btnReserve.setVisibility(View.INVISIBLE);
        	}else{
        		btnReserve.setText(String.format(getResources().getText(R.string.btn_reserve).toString(),bookSeries.getLatestVolume()));
        	}
        }

    }
    
    private BookInfo getItem(int position){
    	if( detailListAdapter != null ){
    		return BookInfo.fromCursor((Cursor) detailListAdapter.getItem(position));
    	}else{
    		return detailListArrayAdapter.getItem(position);
    	}
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	//super.onCreateContextMenu(menu, v, menuInfo);
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
    	BookInfo bi = getItem(info.position);
    	menu.setHeaderTitle(bi.getTitle());
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_list_item_context, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	BookInfo bi = getItem(info.position);
    	switch( item.getItemId() ){
    		case R.id.menu_item_purchase:
    			startPurchasePageBrowseActivity(bi);
    			return true;
    		case R.id.menu_remove:
    			removeBook(bi);
    			break;
    	}
    	return false;
    }
    
    private void removeBook(BookInfo bi) {
		bi.setOwned(false);
		bi.setHidden(true);
		if( bi.getBookId() > 0 ){
			ContentValues v = new ContentValues();
			v.put(Contract.Books.Columns.COL_FLAG_OWNED, 0);
			v.put(Contract.Books.Columns.COL_FLAG_HIDE, 1);
			getContentResolver().update(ContentUris.withAppendedId(Contract.Books.ContentUri, bi.getBookId()), v, null, null);
		}else{
			detailListArrayAdapter.remove(bi);
		}
	}

	private void startPurchasePageBrowseActivity(BookInfo bi){
		String url = bi.getItemUrl();
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url) );
		startActivity(i);
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail_list, menu);
        
        if( !isPreviewMode ){
        	menu.findItem(R.id.menu_follow).setVisible(false);
        }
        return true;
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Log.d(TAG, "menu item selected item:" + item.getItemId() );
    	
    	ContentValues v = bookSeries.toContentValues();
    	ContentValues[] books = bookSeries.booksToContentValues();
    	
    	//DatabaseManager db = DatabaseManager.getInstance();
    	int id = item.getItemId();
    	switch(id){
    	case R.id.menu_follow:
        	Uri ret = getContentResolver().insert(Contract.Follows.ContentUri, v);
        	Uri booksUri = ret.buildUpon().appendPath(Contract.Books.TABLE).build();
        	Log.d(TAG,"onOptionsItemSelected : bulk inserting :" + booksUri);
        	getContentResolver().bulkInsert(booksUri, books);
    		setResult(RESULT_OK);
    		finish();
    		break;
    	case R.id.menu_unfollow:
    		getContentResolver().delete(ContentUris.withAppendedId(Contract.Follows.ContentUri,bookSeries.getSeriesId() ), null, null);
    		setResult(RESULT_OK);
    		finish();
    		break;
    	}
    	return true;
    }

    @Override
    protected void onDestroy() {
    	super.onDestroy();
    }

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Uri uri = ContentUris.withAppendedId(Contract.Follows.ContentUri, bookSeries.getSeriesId())
				.buildUpon().appendPath(Contract.Books.TABLE).build();
		Log.d(TAG,"onCreateLoader : " + uri);
		return new CursorLoader(this, uri, null, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor c) {
		detailListAdapter.swapCursor(c);
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		detailListAdapter.swapCursor(null);
		
	}
	
	@Override
	protected void onPause() {
		if( isFinishing() ){
			if( loader != null )loader.cancelAll();
		}
		super.onPause();
	}
}
