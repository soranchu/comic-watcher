package jp.tande.android.comicwatcher;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class TopListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_list, menu);
        return true;
    }
    
}
