package jp.tande.android.comicwatcher.adapters;

import jp.tande.android.comicwatcher.api.ImageLoader;
import jp.tande.android.comicwatcher.db.BookInfo;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.support.v4.widget.CursorAdapter;

public class DetailListAdapter extends CursorAdapter {
	@SuppressWarnings("unused")
	private static final String TAG ="DetailListAdapter";
	private ImageLoader loader;
	private DetailListLayoutLoader layoutLoader;
	private int currentTargetViewIndex = -1;
	private View dummyHeader ;
	private int headerHeight = 200;
	
	public DetailListAdapter(Context context, ImageLoader loader) {
		super(context, null, false);//R.layout.detail_list_item);
		this.loader = loader;
		this.layoutLoader = new DetailListLayoutLoader(context);
	}
	
	public View getDummyHeaderView(){
		return dummyHeader;
	}
	
	@Override
	public int getCount() {
		return super.getCount()+1;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if( position == 0 ){
			if( dummyHeader == null ){
				dummyHeader = new View(mContext);
				dummyHeader.setMinimumHeight(headerHeight);
			}
			return dummyHeader;
		}
		if( dummyHeader == convertView )convertView = null;
		currentTargetViewIndex = position-1;
		return super.getView(position-1, convertView, parent);
	}
	
	public void setHeaderHeight(int height){
		headerHeight = height;
		if( dummyHeader != null ){
			dummyHeader.setMinimumHeight(headerHeight);
		}
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		BookInfo bi = BookInfo.fromCursor(cursor);
		layoutLoader.bindView(currentTargetViewIndex, view, context, bi, loader, this);
	}

		
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return layoutLoader.newView(context, parent);
	}

}
