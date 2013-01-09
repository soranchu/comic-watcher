package jp.tande.android.comicwatcher;

import java.util.List;

import jp.tande.android.comicwatcher.api.ImageLoader;
import jp.tande.android.comicwatcher.db.BookInfo;
import jp.tande.android.comicwatcher.db.BookSeries;
import jp.tande.android.comicwatcher.db.DatabaseManager;
import adapters.BookSeriesAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class TopListActivity extends Activity {
	private static final String TAG ="TopListActivity";
	
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
        
        ImageLoader.initInstance(new Handler());
        loader = ImageLoader.getInstance();
        
        listFollows = (ListView) findViewById(R.id.list_follows);
        followsAdapter = new BookSeriesAdapter(this, loader);
        listFollows.setAdapter(followsAdapter);
        registerForContextMenu(listFollows);

        listFollows.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				BookSeries bs = followsAdapter.getItem(position);
				Intent i = new Intent(TopListActivity.this, DetailActivity.class);
				i.putExtra("data", bs);
				startActivity(i);
			}
		});
        
        DatabaseManager.initializeInstance(this);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
    	BookSeries bs = followsAdapter.getItem(info.position);
    	menu.setHeaderTitle(bs.getTitle());
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_list_item_context, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	BookSeries bs = followsAdapter.getItem(info.position);
    	switch( item.getItemId() ){
    		case R.id.series_remove:
    			Log.d(TAG, "deleting series " + bs.getTitle() + " sid:" + bs.getSeriesId());
    			if( bs.getSeriesId() > 0 ){
    				DatabaseManager.getInstance().removeBookSeries(bs.getSeriesId());
    				reloadFollows();
    			}
    			break;
    	}
    	return false;
    }

    private void reloadFollows(){
    	DatabaseManager db = DatabaseManager.getInstance();
    	followsAdapter.clear();
    	List<BookSeries> series = db.querySeries();
    	for (BookSeries bs : series) {
        	followsAdapter.add(bs);
		}
    }
    
    @Override
    protected void onResume() {
    	reloadFollows();
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
}
