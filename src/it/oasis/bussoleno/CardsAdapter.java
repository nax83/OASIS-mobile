package it.oasis.bussoleno;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CardsAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
    private Bitmap mIcon;

    private ArrayList<Card> cards;
    
    public CardsAdapter(Context context, ArrayList<Card> aaa) {
        // Cache the LayoutInflate to avoid asking for a new one each time.
        mInflater = LayoutInflater.from(context);
        cards = aaa;
        // Icons bound to the rows.
        //mIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.bkg_card);
    }

    public int getCount() {
        return cards.size();
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
        return cards.get(position);
    }

    /**
     * Use the array index as a unique id.
     *
     * @see android.widget.ListAdapter#getItemId(int)
     */
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
            holder.first = (TextView) convertView.findViewById(R.id.first);
            holder.last = (TextView) convertView.findViewById(R.id.last);
            //holder.icon = (ImageView) convertView.findViewById(R.id.icon);

            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }

        // Bind the data efficiently with the holder.
        holder.first.setText(cards.get(position).mFirst);
        //holder.icon.setImageBitmap((position & 1) == 1 ? mIcon1 : mIcon2);

        return convertView;
    }

    static class ViewHolder {
        TextView last;
        TextView first;
        ImageView icon;
    }
}