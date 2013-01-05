package jp.tande.android.comicwatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jp.tande.android.comicwatcher.api.BooksApi;
import jp.tande.android.comicwatcher.api.BooksApi.SearchResultListener;
import jp.tande.android.comicwatcher.api.data.BookInfo;
import jp.tande.android.comicwatcher.api.data.BookSeries;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.TextView.OnEditorActionListener;

public class AddItemActivity extends Activity {
	private static final String TAG="AddItemActivity";
	private AutoCompleteTextView comicTitleText;
	private AutoCompleteKeywordAdapter autoCompleteAdapter;
	private ListView searchResult;
	private SearchResultAdapter searchResultAdapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);
        
        searchResultAdapter = new SearchResultAdapter(this);
        searchResult = (ListView) findViewById(R.id.list_search_result);
        searchResult.setAdapter(searchResultAdapter);
        
        autoCompleteAdapter = new AutoCompleteKeywordAdapter(this);
        comicTitleText = (AutoCompleteTextView) findViewById(R.id.text_title_search);
        comicTitleText.setAdapter(autoCompleteAdapter);
        comicTitleText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        		Log.d(TAG, "action v:" + comicTitleText.getText());
        		autoCompleteAdapter.addItem(comicTitleText.getText().toString());
        		execSearch(comicTitleText.getText().toString());
        		return true;
			}
		});
        comicTitleText.setOnItemSelectedListener(new OnItemSelectedListener() {
        	@Override
        	public void onItemSelected(AdapterView<?> arg0, View arg1,
        			int arg2, long arg3) {
        		Log.d(TAG, "selected 1:"+ arg2 + " 2:" + arg3 + "v:" + comicTitleText.getText());
        		execSearch(comicTitleText.getText().toString());
        	}
        	@Override
        	public void onNothingSelected(AdapterView<?> arg0) {
        		
        	}
        });
        
        comicTitleText.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
        		Log.d(TAG, "click 1:"+ arg2 + " 2:" + arg3 + "v:" + comicTitleText.getText());
        		execSearch(comicTitleText.getText().toString());
			}
		});
    }

    protected void execSearch(String string) {
		// TODO Auto-generated method stub
		BooksApi api = new BooksApi(new Handler());
		searchResultAdapter.clear();
		api.searchTitle(string, new SearchResultListener() {
			
			@Override
			public void onFinished(List<BookSeries> infos) {
				// TODO Auto-generated method stub
				Log.d(TAG, "result:"+ infos);
				searchResultAdapter.clear();
				for (BookSeries bs : infos) {
					searchResultAdapter.add(bs);
				}
			}
			
			@Override
			public void onError(String message, Throwable t) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.top_list, menu);
        return true;
    }
    
}
