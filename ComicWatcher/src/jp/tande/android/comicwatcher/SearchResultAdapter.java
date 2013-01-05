package jp.tande.android.comicwatcher;

import jp.tande.android.comicwatcher.api.data.BookSeries;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchResultAdapter extends ArrayAdapter<BookSeries> {

	public SearchResultAdapter(Context ctx){
		super(ctx, android.R.layout.simple_list_item_2);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if( convertView == null ){
			convertView = View.inflate(getContext(), android.R.layout.simple_list_item_2, null);
		}
		TextView v1 = (TextView) convertView.findViewById(android.R.id.text1);
		TextView v2 = (TextView) convertView.findViewById(android.R.id.text2);
		BookSeries bs = getItem(position);
		v1.setText( bs.getTitle() + "/" + bs.getAuthor() );
		v2.setText( bs.getBooks().size() + "冊 / 出版社:" + bs.getPublisher() + " genre:"+bs.getBookGenreId() );

		return convertView;
	}

}
