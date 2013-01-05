package jp.tande.android.comicwatcher;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class AutoCompleteKeywordAdapter extends BaseAdapter implements
		Filterable {
	private static final String TAG="AutoCompleteKeywordAdapter";
	
	private ArrayList<String> filteredList;
	private ArrayList<String> sourceList;
	private Context context;
	
	public AutoCompleteKeywordAdapter(Context context) {
		super();
		this.context = context;
		sourceList = new ArrayList<String>();
		filteredList = new ArrayList<String>(sourceList);
	}
	
	public void addItem(String item){
		if( ! sourceList.contains(item) ){
			sourceList.add(item);
		}
	}
	
	@Override
	public int getCount() {
		return filteredList.size();
	}

	@Override
	public Object getItem(int position) {
		return filteredList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if( convertView == null ){
			convertView = View.inflate(context, android.R.layout.simple_spinner_dropdown_item, null);
					//context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
		tv.setText((CharSequence) getItem(position));
		return convertView;
	}

	@Override
	public Filter getFilter() {
		Filter filter = new Filter(){
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				Log.d(TAG, "perform filter");
				FilterResults fr = new FilterResults();
				ArrayList<String> list = new ArrayList<String>();
				if( constraint == null ){
					list.addAll(sourceList);
				}else{
					for (String s : sourceList) {
						if( s.contains(constraint.toString()) ){
							list.add(s);
						}
					}
				}
				fr.values = list;
				fr.count = list.size();
				Log.d(TAG, "perform filter results:"+list);
				return fr;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				filteredList = (ArrayList<String>) results.values;
				notifyDataSetChanged();
			}
			
		};
		return filter;
	}

}
