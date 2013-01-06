package jp.tande.android.comicwatcher;

import java.util.List;

import jp.tande.android.comicwatcher.db.BookInfo;
import jp.tande.android.comicwatcher.db.BookSeries;
import jp.tande.android.comicwatcher.db.DatabaseManager;
import adapters.BookSeriesAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class TopListActivity extends Activity {
	private static final String TAG ="TopListActivity";
	
	private ListView listFollows;
	private BookSeriesAdapter followsAdapter;
	
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
        
        listFollows = (ListView) findViewById(R.id.list_follows);
        followsAdapter = new BookSeriesAdapter(this);
        listFollows.setAdapter(followsAdapter);
        
        DatabaseManager.initializeInstance(this);
    }
    
    @Override
    protected void onResume() {
    	DatabaseManager db = DatabaseManager.getInstance();
    	followsAdapter.clear();
    	List<BookSeries> series = db.querySeries();
    	for (BookSeries bs : series) {
        	followsAdapter.add(bs);
		}
    	super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_list, menu);
        return true;
    }
    
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	Log.d(TAG, "menu item selected fid:"+featureId + " item:" + item.getItemId() );
    	
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
