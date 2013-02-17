package jp.tande.android.comicwatcher.adapters;

import jp.tande.android.comicwatcher.api.ImageLoader;
import jp.tande.android.comicwatcher.db.BookInfo;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.CursorAdapter;

public class DetailListAdapter extends CursorAdapter {
	@SuppressWarnings("unused")
	private static final String TAG ="DetailListAdapter";
	private ImageLoader loader;
	private DetailListLayoutLoader layoutLoader;
	
	public DetailListAdapter(Context context, ImageLoader loader) {
		super(context, null, false);//R.layout.detail_list_item);
		this.loader = loader;
		this.layoutLoader = new DetailListLayoutLoader(context);
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		BookInfo bi = BookInfo.fromCursor(cursor);
		
		layoutLoader.bindView(view, context, bi, loader, this);
	}

		
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return layoutLoader.newView(context, parent);
	}

}
