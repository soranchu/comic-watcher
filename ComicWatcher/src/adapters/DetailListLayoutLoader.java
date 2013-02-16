package adapters;

import java.util.HashMap;
import java.util.Map;

import jp.tande.android.comicwatcher.R;
import jp.tande.android.comicwatcher.api.ImageLoader;
import jp.tande.android.comicwatcher.db.BookInfo;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class DetailListLayoutLoader {
	private LayoutInflater inflater;
	private Map<String, Boolean> ownedFlags = new HashMap<String, Boolean>();

	public DetailListLayoutLoader(Context context){
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	private boolean isItemOwned(BookInfo bi){
		//TODO load from db
		Boolean b = ownedFlags.get(bi.getIsbn());
		if( b != null ){
			return b;
		}
		return false;
	}
	
	private void setItemOwned(BookInfo bi, boolean owned){
		//TODO store db
		ownedFlags.put(bi.getIsbn(), owned);
	}
	

	public void bindView(View view, Context context, final BookInfo bi, ImageLoader loader, final BaseAdapter adapter) {

		TextView txtVolume = (TextView) view.findViewById(R.id.txt_detail_series_num);
		TextView txtRelease = (TextView) view.findViewById(R.id.txt_detail_scheduled_date);
		CheckBox chkOwn = (CheckBox) view.findViewById(R.id.checkBox_haved);
		CheckBox chkReserved = (CheckBox) view.findViewById(R.id.checkBox_reserved);
		final ImageView imgThumb = (ImageView) view.findViewById(R.id.img_detailitem_comic_thumb);
		//final BookInfo bi = BookInfo.fromCursor(cursor);

		txtVolume.setText(context.getString(R.string.txt_series_num, bi.getVolume() ));
		if( ! bi.isOnSale() ){
			if( bi.getTitlePostFix() != null ){
				txtRelease.setText(bi.getTitlePostFix() + "\n" + context.getString(R.string.txt_scheduled_date, bi.getSalesDate()) );
			}else{
				txtRelease.setText(context.getString(R.string.txt_scheduled_date, bi.getSalesDate()) );
			}
			if( chkOwn.getVisibility() != View.GONE ){
				chkOwn.setVisibility(View.GONE);
				chkReserved.setVisibility(View.VISIBLE);
				view.requestLayout();
			}
		}else{
			if( bi.getTitlePostFix() != null ){
				txtRelease.setText(bi.getTitlePostFix() + "\n" + context.getString(R.string.txt_release_date, bi.getSalesDate()));
			}else{
				txtRelease.setText( context.getString(R.string.txt_release_date, bi.getSalesDate()));
			}
			if( chkOwn.getVisibility() != View.VISIBLE ){
				chkOwn.setVisibility(View.VISIBLE);
				chkReserved.setVisibility(View.GONE);
				view.requestLayout();
			}
		}
		boolean owned = isItemOwned(bi);
		chkOwn.setOnCheckedChangeListener(null);
		chkReserved.setOnCheckedChangeListener(null);
		chkOwn.setChecked(owned);
		chkReserved.setChecked(owned);
		chkOwn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				setItemOwned(bi, isChecked);
			}
		});
		chkReserved.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				setItemOwned(bi, isChecked);
			}
		});
		imgThumb.setImageBitmap(null);
		loader.queue(bi.getMediumImageUrl(), imgThumb);
	}

	public View newView(Context context, ViewGroup parent) {
		return inflater.inflate( R.layout.detail_list_item, parent, false);
	}

}