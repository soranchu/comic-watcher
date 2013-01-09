package adapters;

import jp.tande.android.comicwatcher.R;
import jp.tande.android.comicwatcher.api.ImageLoader;
import jp.tande.android.comicwatcher.db.BookInfo;
import jp.tande.android.comicwatcher.db.BookSeries;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BookSeriesAdapter extends ArrayAdapter<BookSeries> {
	private ImageLoader loader;
	public BookSeriesAdapter(Context ctx, ImageLoader loader){
		super(ctx, R.layout.top_list_item);
		this.loader = loader;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if( convertView == null ){
			convertView = View.inflate(getContext(), R.layout.top_list_item, null);
		}
		ImageView img = (ImageView) convertView.findViewById(R.id.img_top_comic_thumb);
		TextView title = (TextView) convertView.findViewById(R.id.txt_top_comic_title);
		TextView scheduled = (TextView) convertView.findViewById(R.id.txt_top_scheduled_date);
		TextView notification = (TextView) convertView.findViewById(R.id.txt_top_notification_num);
		TextView volumes = (TextView) convertView.findViewById(R.id.txt_top_series_num);
		TextView reserved = (TextView) convertView.findViewById(R.id.txt_top_reserved);
		TextView notreserved = (TextView) convertView.findViewById(R.id.txt_top_non_reserved);
		BookSeries bs = getItem(position);
		title.setText( bs.getTitle() );
		//v2.setText( bs.getBooks().size() + "冊 / " + bs.getAuthor() + " / " + bs.getPublisher() );
		volumes.setText(String.format(getContext().getResources().getString(R.string.txt_series_num),
				bs.getLatestVolume()));

		int total = bs.getTotalCount();
		int owned = bs.getOwnedCount();
		if( total > owned ){
			notification.setVisibility(View.VISIBLE);
			notification.setText( String.valueOf(total - owned) );
		}else{
			notification.setVisibility(View.GONE);
		}
		
		final BookInfo bi = bs.getLatest();
		if( bi == null || bi.isOnSale() ){
			scheduled.setText(R.string.txt_not_scheduled);
			reserved.setVisibility(View.GONE);
			notreserved.setVisibility(View.GONE);
			img.setImageBitmap(null);
		}else{
			scheduled.setText(String.format(getContext().getResources().getString(R.string.txt_scheduled_date),
					bi.getVolume() ));
			reserved.setVisibility(View.GONE);
			notreserved.setVisibility(View.VISIBLE);
			
			if( bi.getThumb() != null && ! bi.getThumb().isRecycled() ){
				img.setImageBitmap(bi.getThumb());
			}else if( !bi.isThumbRequested() && bi.getMediumImageUrl() != null ){
				img.setImageBitmap(null);
				bi.setThumbRequested(true);
				loader.queue(bi.getMediumImageUrl(), new ImageLoader.ImageLoaderListener() {
					
					@Override
					public void onLoadFinished(Bitmap bmp) {
						bi.setThumb(bmp);
						//imgThumb.setImageBitmap(bmp);
						notifyDataSetChanged();
					}
				});
			}else{
				img.setImageBitmap(null);
			}

		}
		
		return convertView;
	}

}
