package it.bussoleno.oasis;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CardsAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
    private MyApplication mContext;
    
    
    public CardsAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mContext = (MyApplication)context.getApplicationContext();
    }

    public int getCount() {
        return mContext.getCheckedInListSize();
    }

    /**
     * Since the data comes from an array, just returning the index is
     * sufficent to get at the data. If we were using a more complex data
     * structure, we would return whatever object represents one row in the
     * list.
     *
     * @see android.widget.ListAdapter#getItem(int)
     */
    public Object getItem(int position) {
        return mContext.getCardFromCheckedList(position);
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
        // A ViewHolder keeps references to children views to avoid unneccessary calls
        // to findViewById() on each row.
        ViewHolder holder;

        // When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.card_item, null);

            // Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
            holder = new ViewHolder();
            holder.desc = (TextView) convertView.findViewById(R.id.desc);
            holder.fullName = (TextView) convertView.findViewById(R.id.fullname);

            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }

        // Bind the data efficiently with the holder.
        holder.desc.setText(mContext.getCardFromCheckedList(position).mDesc);
        holder.fullName.setText(mContext.getCardFromCheckedList(position).mFullname);

        return convertView;
    }

    static class ViewHolder {
        TextView fullName;
        TextView desc;
    }
}