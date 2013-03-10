package jp.tande.android.comicwatcher.adapters;

import jp.tande.android.comicwatcher.R;
import jp.tande.android.comicwatcher.api.ImageLoader;
import jp.tande.android.comicwatcher.db.BookSeries;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BookSeriesAdapter extends CursorAdapter{
	private ImageLoader loader;
	private Context context;
	private LayoutInflater inflator;
	private int currentTargetViewIndex = -1;
	
	public BookSeriesAdapter(Context ctx, ImageLoader loader){
		super(ctx, null, false);//R.layout.top_list_item);
		this.loader = loader;
		this.context = ctx;
		this.inflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	private Context getContext(){
		return context;
	}
	/*
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if( convertView == null ){
			convertView = inflator.inflate( R.layout.top_list_item, null, false);
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
	*/
	
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		currentTargetViewIndex = arg0;
		return super.getView(arg0, arg1, arg2);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ImageView img = (ImageView) view.findViewById(R.id.img_top_comic_thumb);
		TextView title = (TextView) view.findViewById(R.id.txt_top_comic_title);
		TextView scheduled = (TextView) view.findViewById(R.id.txt_top_scheduled_date);
		TextView notification = (TextView) view.findViewById(R.id.txt_top_notification_num);
		TextView volumes = (TextView) view.findViewById(R.id.txt_top_series_num);
		TextView reserved = (TextView) view.findViewById(R.id.txt_top_reserved);
		TextView notreserved = (TextView) view.findViewById(R.id.txt_top_non_reserved);
		BookSeries bs = BookSeries.fromCursor(cursor);
		
		if( currentTargetViewIndex % 2 == 0){
			view.setBackgroundResource(R.drawable.selector_list_bg_odd);
		}else{
			view.setBackgroundResource(R.drawable.selector_list_bg_even);
		}
		
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
		
		if( ! bs.isLatestOnSale() ){//TODO check sales date
			scheduled.setText(String.format(getContext().getResources().getString(R.string.txt_scheduled_date),
					bs.getLatestSalesDate() ));
			reserved.setVisibility(View.GONE);//TODO check reserved or not
			notreserved.setVisibility(View.VISIBLE);
		}else{
			scheduled.setText(R.string.txt_not_scheduled);
			reserved.setVisibility(View.GONE);
			notreserved.setVisibility(View.GONE);
		}
		
		if( bs.getImageUrl() != null ){
			loader.queue(bs.getImageUrl(), img);
		}else{
			img.setImageBitmap(null);
		}
		
		/*
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
		*/
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return inflator.inflate( R.layout.top_list_item, parent, false);
	}

}
