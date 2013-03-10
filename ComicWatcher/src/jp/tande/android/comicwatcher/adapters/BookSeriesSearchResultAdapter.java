package jp.tande.android.comicwatcher.adapters;

import jp.tande.android.comicwatcher.R;
import jp.tande.android.comicwatcher.db.BookSeries;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BookSeriesSearchResultAdapter extends ArrayAdapter<BookSeries> {

	public BookSeriesSearchResultAdapter(Context ctx){
		super(ctx, R.layout.add_list_item);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if( convertView == null ){
			convertView = View.inflate(getContext(), R.layout.add_list_item, null);
		}
		if( position % 2 == 0){
			convertView.setBackgroundResource(R.drawable.selector_list_bg_odd);
		}else{
			convertView.setBackgroundResource(R.drawable.selector_list_bg_even);
		}

		TextView v1 = (TextView) convertView.findViewById(R.id.txt_add_list_item_title);
		TextView v2 = (TextView) convertView.findViewById(R.id.txt_add_list_item_sub);
		BookSeries bs = getItem(position);
		v1.setText( bs.getTitle() );
		v2.setText( bs.getBooks().size() + "冊 / " + bs.getAuthor() + " / " + bs.getPublisher() );

		return convertView;
	}

}
