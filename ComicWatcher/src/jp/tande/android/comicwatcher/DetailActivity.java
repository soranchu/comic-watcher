package jp.tande.android.comicwatcher;

import jp.tande.android.comicwatcher.api.ImageLoader;
import jp.tande.android.comicwatcher.api.data.BookInfo;
import jp.tande.android.comicwatcher.api.data.BookSeries;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DetailActivity extends Activity {
	private static final String TAG ="DetailActivity";
	
	private TextView txtTitle;
	private TextView txtAuthor;
	private TextView txtSeries;
	private ImageView imgThumb;
	private ListView listDetail;
	private Button btnReserve;

	private BookSeries bookSeries;
	private ImageLoader loader = new ImageLoader(new Handler());
	private DetailListAdapter detailListAdapter;
	
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
        
        detailListAdapter = new DetailListAdapter(this, loader);
        
        listDetail.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				BookInfo bi = (BookInfo) listDetail.getItemAtPosition(arg2);
				String url = bi.getItemUrl();
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url) );
				startActivity(i);
			}

        });
        listDetail.setAdapter(detailListAdapter);
        
        Intent i = getIntent();
        bookSeries = (BookSeries) i.getExtras().getSerializable("data");
        txtTitle.setText(bookSeries.getTitle());
        txtAuthor.setText(bookSeries.getAuthor());
        txtSeries.setText(bookSeries.getPublisher());//TODO change to series
        if( bookSeries.getLatest() != null ){
        	String url = bookSeries.getLatest().getLargeImageUrl();
        	if( url != null ){
        		loader.queue(url, new ImageLoader.ImageLoaderListener() {
					
					@Override
					public void onLoadFinished(Bitmap bmp) {
						imgThumb.setImageBitmap(bmp);
					}
				});
        	}
        }
        
        for (BookInfo bi : bookSeries.getBooks()) {
			 detailListAdapter.add(bi);
		}
        
    }
    /*
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
    }*/

}
