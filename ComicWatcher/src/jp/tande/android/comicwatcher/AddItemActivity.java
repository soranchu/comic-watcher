package jp.tande.android.comicwatcher;

import java.util.List;

import jp.tande.android.comicwatcher.api.BooksApi;
import jp.tande.android.comicwatcher.api.BooksApi.SearchResultListener;
import jp.tande.android.comicwatcher.db.BookSeries;
import adapters.AutoCompleteKeywordAdapter;
import adapters.BookSeriesSearchResultAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class AddItemActivity extends Activity {
	private static final String TAG="AddItemActivity";
	private AutoCompleteTextView comicTitleText;
	private AutoCompleteKeywordAdapter autoCompleteAdapter;
	private ListView searchResult;
	private BookSeriesSearchResultAdapter searchResultAdapter;
	LinearLayout topBorder;
	LinearLayout bottomBorder;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);
        
        searchResultAdapter = new BookSeriesSearchResultAdapter(this);
        searchResult = (ListView) findViewById(R.id.list_search_result);
        searchResult.setAdapter(searchResultAdapter);

        topBorder = (LinearLayout) findViewById(R.id.list_border_top);
        bottomBorder = (LinearLayout) findViewById(R.id.list_border_bottom);
        topBorder.setVisibility(View.INVISIBLE);
        bottomBorder.setVisibility(View.INVISIBLE);
        searchResult.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				BookSeries bs = (BookSeries) searchResult.getItemAtPosition(arg2);
				Log.d(TAG,"selected " + bs.getTitle());
				Intent i = new Intent(AddItemActivity.this, DetailActivity.class);
				i.putExtra("data", bs);//TODO
				startActivity(i);
			}
        	
        });
        
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
		if( string == null || string.trim().isEmpty() )return;
		
    	InputMethodManager imm = (InputMethodManager)getSystemService(
    	      Context.INPUT_METHOD_SERVICE);
    	imm.hideSoftInputFromWindow( comicTitleText.getWindowToken(), 0);
    	
		BooksApi api = new BooksApi(new Handler());

		searchResultAdapter.clear();
        topBorder.setVisibility(View.INVISIBLE);
        bottomBorder.setVisibility(View.INVISIBLE);

		api.searchTitle(string, new SearchResultListener() {
			
			@Override
			public void onFinished(List<BookSeries> infos) {
				// TODO Auto-generated method stub
				//Log.d(TAG, "result:"+ infos);
				searchResultAdapter.clear();
				for (BookSeries bs : infos) {
					searchResultAdapter.add(bs);
				}
				if( searchResultAdapter.getCount() > 0 ){
			        topBorder.setVisibility(View.VISIBLE);
			        bottomBorder.setVisibility(View.VISIBLE);
				}else{
			        topBorder.setVisibility(View.INVISIBLE);
			        bottomBorder.setVisibility(View.INVISIBLE);
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
    @Override
    protected void onPause() {
		if( isFinishing() ){
			//loader.cancelAll();
		}
    	super.onPause();
    }
}
