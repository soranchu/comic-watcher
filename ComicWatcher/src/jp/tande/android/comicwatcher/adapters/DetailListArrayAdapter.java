package jp.tande.android.comicwatcher.adapters;

import jp.tande.android.comicwatcher.R;
import jp.tande.android.comicwatcher.api.ImageLoader;
import jp.tande.android.comicwatcher.db.BookInfo;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class DetailListArrayAdapter extends ArrayAdapter<BookInfo> {
	@SuppressWarnings("unused")
	private static final String TAG = DetailListArrayAdapter.class.getSimpleName();
	private ImageLoader loader;
	private DetailListLayoutLoader layoutLoader;
	
	public DetailListArrayAdapter(Context context, ImageLoader loader) {
		super(context, R.layout.detail_list_item);
		this.loader = loader;
		this.layoutLoader = new DetailListLayoutLoader(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if( convertView == null ){
			convertView = layoutLoader.newView(getContext(), parent);
		}
		BookInfo bi = getItem(position);
		layoutLoader.bindView(position, convertView, getContext(), bi, loader, this);
		return convertView;
	}
	

}
