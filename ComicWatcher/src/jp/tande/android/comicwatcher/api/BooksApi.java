package jp.tande.android.comicwatcher.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONException;

import jp.tande.android.comicwatcher.api.credentials.Credential;
import jp.tande.android.comicwatcher.api.data.BookSearchResponse;
import jp.tande.android.comicwatcher.db.BookInfo;
import jp.tande.android.comicwatcher.db.BookSeries;

import android.os.Handler;
import android.util.Log;

public class BooksApi {
	public interface SearchResultListener{
		void onFinished(List<BookSeries> series);
		void onError(String message, Throwable t);
	}
	
	private static final String TAG="BooksApi";
	
	private static final String APP_ID = Credential.APP_ID;
	
	private static final int MAX_PRICE = 1000;
	
	private static final String SERVICE_URL = "https://app.rakuten.co.jp/services/api/BooksBook/Search/20121128";
	private static final String PARAM_APP_ID = "applicationId";
	private static final String PARAM_AFF_ID = "affiliateId";
	private static final String PARAM_TITLE = "title";
	private static final String PARAM_AUTHOR = "author";
	private static final String PARAM_PUBLISHER = "publisherName";
	private static final String PARAM_HITS = "hits";
	private static final String PARAM_PAGE = "page";
	private static final String PARAM_OUT_OF_STOCK = "outOfStockFlag";
	private static final String VALUE_HIDE_OUT_OF_STOCK = "0";
	private static final String VALUE_SHOW_OUT_OF_STOCK = "1";
	private static final String PARAM_GENRE = "booksGenreId";
	private static final String VALUE_GENRE_COMIC = "001001";//TODO: fix genre to 001001
	private static final String PARAM_SIZE = "size";
	private static final String VALUE_SIZE_COMIC = "9";
	private static final String PARAM_SORT = "sort";
	private static final String VALUE_SORT_STD = "standard";
	private static final String VALUE_SORT_REL_ASC = "-releaseDate";
	private static final String VALUE_SORT_REL_DESC = "+releaseDate";
	private static final String PARAM_CARRIER = "carrier";
	private static final String PARAM_ELEMENTS = "elements";

	private Handler handler;
	
	public BooksApi(Handler handler){
		this.handler = handler;
	}
	
	
	private BookSearchResponse requestTitle(String title, int page) throws JSONException, IOException{
		TreeMap<String, String> params = new TreeMap<String, String>();
		params.put(PARAM_APP_ID, APP_ID);
		params.put(PARAM_TITLE, title);
		params.put(PARAM_PAGE, Integer.toString(page) );
		params.put(PARAM_GENRE, VALUE_GENRE_COMIC);
		params.put(PARAM_OUT_OF_STOCK, VALUE_SHOW_OUT_OF_STOCK);
		params.put(PARAM_SORT, VALUE_SORT_REL_ASC);
		params.put(PARAM_SIZE, VALUE_SIZE_COMIC);//only comic
		URL url;

		url = buildUrl(SERVICE_URL, params);
		Log.d(TAG,"url:" + url);
		
		InputStream is = null;
		is = url.openStream();
		return JSON.decode(is,BookSearchResponse.class);
	}
	
	public void searchTitle(final String title, final SearchResultListener listener){
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				List<BookInfo> books = null;
				BookSearchResponse res = null;
				int paging = 1;
				do{
					res = null;
					try {
						res = requestTitle(title, paging);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break;
					}
					if( res != null ){
						if( books == null ){
							books = res.getItems();
						}else{
							books.addAll(res.getItems());
						}
						Log.d(TAG,"load " + res.getLast() + " of " + res.getCount());
					}
					paging++;
				}while( paging <= 5 && res != null && res.getPageCount() > res.getPage() );
				final List<BookSeries> series = new ArrayList<BookSeries>();
				if( books != null ){
					for (BookInfo bi : books) {
						if( !bi.isValidIsbn() || /*bi.getListPrice() > MAX_PRICE ||*/ !bi.genreContainsOnly(VALUE_GENRE_COMIC) ){
							Log.d(TAG,"Invalid book: " + bi.getTitle() + " " + bi.getIsbn() + " " + bi.getBooksGenreId());
							continue;
						}
						bi.parseTitle();
						BookSeries se = null;
						for (BookSeries bs : series) {
							if(bs.match(bi)){
								se = bs;
							}
						}
						if( se == null ){
							se = new BookSeries(bi);
							series.add(se);
						}else{
							se.addBook(bi);
						}
					}
				}
				if( listener != null ){
					Log.d(TAG,"result:"+series.size());
					handler.post(new Runnable() {
						@Override
						public void run() {
							listener.onFinished(series);
						}
					});
				}
			}
		}; 
		
		new Thread(r).start();
	}

	private static URL buildUrl(String serviceUrl, TreeMap<String, String> params) throws MalformedURLException {
		if( params == null || params.isEmpty() ){
			return new URL(serviceUrl);
		}
		StringBuilder sb =new StringBuilder(serviceUrl);
		
		sb.append("?");
		for (Iterator<String> itr = params.keySet().iterator(); itr.hasNext();) {
			String p = itr.next();
			try {
				sb.append(p).append("=");
				sb.append(URLEncoder.encode(params.get(p), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if( itr.hasNext() ){
				sb.append("&");
			}
		}
		return new URL(sb.toString());
	}
}
