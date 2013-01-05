package jp.tande.android.comicwatcher;

import jp.tande.android.comicwatcher.api.ImageLoader;
import jp.tande.android.comicwatcher.api.data.BookInfo;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailListAdapter extends ArrayAdapter<BookInfo> {
	private ImageLoader loader;
	
	public DetailListAdapter(Context context, ImageLoader loader) {
		super(context, R.layout.detail_list_item);
		this.loader = loader;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if( convertView == null ){
			convertView = View.inflate(getContext(), R.layout.detail_list_item, null);
		}
		TextView txtVolume = (TextView) convertView.findViewById(R.id.txt_detail_series_num);
		TextView txtRelease = (TextView) convertView.findViewById(R.id.txt_detail_scheduled_date);
		CheckBox chkOwn = (CheckBox) convertView.findViewById(R.id.checkBox_haved);
		CheckBox chkReserved = (CheckBox) convertView.findViewById(R.id.checkBox_reserved);
		final ImageView imgThumb = (ImageView) convertView.findViewById(R.id.img_detailitem_comic_thumb);
		final BookInfo bi = getItem(position);

		txtVolume.setText(String.format(getContext().getResources().getText(R.string.txt_series_num).toString(), bi.getVolume()) );
		if( "5".equals(bi.getAvailability()) ){
			if( bi.getTitlePostFix() != null ){
				txtRelease.setText(bi.getTitlePostFix() + "\n" + String.format(getContext().getResources().getText(R.string.txt_scheduled_date).toString(), bi.getSalesDate()));
			}else{
				txtRelease.setText(String.format(getContext().getResources().getText(R.string.txt_scheduled_date).toString(), bi.getSalesDate()));
			}
			if( chkOwn.getVisibility() != View.GONE ){
				chkOwn.setVisibility(View.GONE);
				chkReserved.setVisibility(View.VISIBLE);
				convertView.requestLayout();
			}
		}else{
			if( bi.getTitlePostFix() != null ){
				txtRelease.setText(bi.getTitlePostFix() + "\n" + String.format(getContext().getResources().getText(R.string.txt_release_date).toString(), bi.getSalesDate()));
			}else{
				txtRelease.setText(String.format(getContext().getResources().getText(R.string.txt_release_date).toString(), bi.getSalesDate()));
			}
			if( chkOwn.getVisibility() != View.VISIBLE ){
				chkOwn.setVisibility(View.VISIBLE);
				chkReserved.setVisibility(View.GONE);
				convertView.requestLayout();
			}
		}
		if( bi.getThumb() != null && ! bi.getThumb().isRecycled() ){
			imgThumb.setImageBitmap(bi.getThumb());
		}else if( !bi.isThumbRequested() ){
			imgThumb.setImageBitmap(null);
			bi.setThumbRequested(true);
			loader.queue(bi.getLargeImageUrl(), new ImageLoader.ImageLoaderListener() {
				
				@Override
				public void onLoadFinished(Bitmap bmp) {
					bi.setThumb(bmp);
					imgThumb.setImageBitmap(bmp);
					notifyDataSetChanged();
				}
			});
		}
		return convertView;
	}

}
