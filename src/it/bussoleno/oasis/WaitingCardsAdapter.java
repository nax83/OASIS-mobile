package it.bussoleno.oasis;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WaitingCardsAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Activity mContext;
    private ArrayList<Card> mList;
    
    public WaitingCardsAdapter(Activity context, ArrayList<Card> list) {
        mInflater = LayoutInflater.from(context);
        mList = list;
        mContext = context;
    }

    public int getCount() {
    	return mList.size();
    }

    public Object getItem(int position) {
    	return mList.get(position);
    }
    
    public long getItemId(int position) {
        return position;
    }

    /**
     * Make a view to hold each row.
     *
     * @see android.widget.ListAdapter#getView(int, android.view.View,
     *      android.view.ViewGroup)
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.card_item, null);

            holder = new ViewHolder();
            holder.desc = (TextView) convertView.findViewById(R.id.desc);
            holder.fullName = (TextView) convertView.findViewById(R.id.fullname);
            holder.confirmed = (TextView) convertView.findViewById(R.id.confirmed);
            holder.waiting = (TextView) convertView.findViewById(R.id.waiting);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

		holder.desc.setText(mList.get(position).mDesc);
		holder.fullName.setText(mList.get(position).mFullname);
		holder.id = mList.get(position).mId;
		holder.isConfirmed = mList.get(position).mIsConfirmed;
		if(holder.isConfirmed){
			holder.confirmed.setVisibility(View.VISIBLE);
			holder.waiting.setVisibility(View.GONE);
		}else{
			holder.confirmed.setVisibility(View.GONE);
			holder.waiting.setVisibility(View.VISIBLE);
		}
		
    	return convertView;
    }

    static class ViewHolder {
        TextView fullName;
        TextView desc;
        TextView waiting;
        TextView confirmed;
        boolean isConfirmed;
        String id;
    }
}