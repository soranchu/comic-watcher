package jp.tande.android.comicwatcher;

import jp.tande.android.comicwatcher.api.ImageLoader;
import jp.tande.android.comicwatcher.db.BookSeries;
import jp.tande.android.comicwatcher.db.DatabaseManager.Contract;
import adapters.BookSeriesAdapter;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class TopListActivity extends FragmentActivity  implements LoaderCallbacks<Cursor>{
	private static final String TAG = TopListActivity.class.getSimpleName();
	
	private ListView listFollows;
	private BookSeriesAdapter followsAdapter;
	private ImageLoader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top);
        
        /*
        BookInfo b = new BookInfo();
        b.setItem(new BookInfo.Item());
        b.setTitle("宇宙兄弟（１）");
        b.setTitle("宇宙兄弟(1)");
        b.setTitle("宇宙兄弟（１２）");
        b.setTitle("宇宙兄弟（１2）");
        b.setTitle("宇宙兄弟 (123)");
        b.setTitle("宇宙兄弟 (１２）");
        b.setTitle("宇宙兄弟（１ ） ");
        b.setTitle("宇宙兄弟 １");
        b.setTitle("宇宙兄弟1");
        b.setTitle("宇宙兄弟 2");
        b.setTitle("宇宙兄弟 Vol. １");
        b.setTitle("宇宙兄弟vol.1");
        b.setTitle("宇宙兄弟vol1");
        b.setTitle("宇宙兄弟 vol2");
        b.setTitle("宇宙兄弟 vol 2");
        b.setTitle("宇宙兄弟 (2巻)");
        b.setTitle("宇宙兄弟 （巻2）");
        b.setTitle("宇宙兄弟 (巻 2 ）");
        b.setTitle("宇宙兄弟 2 限定版");
        b.setTitle("宇宙兄弟 （2 ） 限定版");
        b.setTitle("宇宙兄弟 ( 2 ) 限定版");
        b.setTitle("宇宙3兄弟 ( 2 ) 限定版");
        */
        
        loader = ImageLoader.getInstance();
        
        listFollows = (ListView) findViewById(R.id.list_follows);
        followsAdapter = new BookSeriesAdapter(this, loader);
        listFollows.setAdapter(followsAdapter);
        registerForContextMenu(listFollows);

        listFollows.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				BookSeries bs = BookSeries.fromCursor( (Cursor) followsAdapter.getItem(position) );
				Intent i = new Intent(TopListActivity.this, DetailActivity.class);
				i.putExtra("data", bs);
				startActivity(i);
			}
		});
        
        //DatabaseManager.initializeInstance(this);
        getSupportLoaderManager().initLoader(0, null, this);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
    	BookSeries bs = BookSeries.fromCursor( (Cursor) followsAdapter.getItem(info.position) );
    	menu.setHeaderTitle(bs.getTitle());
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_list_item_context, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	BookSeries bs = BookSeries.fromCursor( (Cursor) followsAdapter.getItem(info.position) );
    	switch( item.getItemId() ){
    		case R.id.series_remove:
    			Log.d(TAG, "deleting series " + bs.getTitle() + " sid:" + bs.getSeriesId());
    			if( bs.getSeriesId() > 0 ){
    				getContentResolver().delete(ContentUris.withAppendedId(Contract.Follows.ContentUri, bs.getSeriesId()), null, null);
    				//TODO: remove follows
    				//DatabaseManager.getInstance().removeBookSeries(bs.getSeriesId());
    				//reloadFollows();
    			}
    			break;
    	}
    	return false;
    }

    /* TODO: reload
    private void reloadFollows(){
    	DatabaseManager db = DatabaseManager.getInstance();
    	followsAdapter.clear();
    	List<BookSeries> series = db.querySeries();
    	for (BookSeries bs : series) {
        	followsAdapter.add(bs);
		}
    }
    */
    
    @Override
    protected void onResume() {
    	//reloadFollows();
    	super.onResume();
    }
    
    @Override
    protected void onDestroy() {
    	loader.shutdown();
    	super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_list, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Log.d(TAG, "menu item selected item:" + item.getItemId() );
    	
    	int id = item.getItemId();
    	switch(id){
    	case R.id.menu_add:
    		startActivity(new Intent(this,AddItemActivity.class));
    		break;
    	case R.id.menu_refresh:
    		break;
    	case R.id.menu_settings:
    		break;
    	}
    	return true;
    }

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Log.d(TAG,"onCreateLoader");
		return new CursorLoader(this, Contract.Follows.ContentUri, null, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
		// TODO Auto-generated method stub
		Log.d(TAG,"onLoadFinished : " + c);
		followsAdapter.swapCursor(c);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		Log.d(TAG,"onLoaderReset");
		followsAdapter.swapCursor(null);
	}
	
	@Override
	protected void onPause() {
		if( isFinishing() ){
			if( loader != null )loader.cancelAll();
		}
		super.onPause();
	}

}
