package it.bussoleno.oasis;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class CardsListFragment extends ListFragment {

	private OnStartScanListener listener;
	private OnCardClickListener clickListener;
	public CardsListFragment(){
		
	}
	
	public interface OnCardClickListener {
		public void onCardClicked(String id);
	}
	
	public interface OnStartScanListener {
        public void onStartScanSelected();
    }
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public void onAttach(Activity activity) {
    	// TODO Auto-generated method stub
    	super.onAttach(activity);
    	try{
    		listener = (OnStartScanListener)activity;
    	}catch (ClassCastException e) {
			throw new RuntimeException("Parent Activity must implement OnStartScanListener interface");
		}
    	try{
    		clickListener = (OnCardClickListener)activity;
    	}catch (ClassCastException e) {
			throw new RuntimeException("Parent Activity must implement OnCardClickListener interface");
		}
    	
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onActivityCreated(savedInstanceState);
    	getListView().setDividerHeight(0);
    	getListView().setSelector(android.R.color.transparent);
    	View emptyView = getActivity().getLayoutInflater().inflate(R.layout.emptylist, null);
    	final View img = emptyView.findViewById(R.id.clipboard);
    	img.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listener.onStartScanSelected();
			}
		});
    	getListView().setEmptyView(emptyView);
        ((ViewGroup) getListView().getParent()).addView(emptyView);
        
    }

    @Override
    public void onStart() {
        super.onStart();

    }
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
    	// TODO Auto-generated method stub
    	super.onListItemClick(l, v, position, id);
    	Object obj = v.getTag();
    	if(obj instanceof CardsAdapter.ViewHolder){
    		Log.d("TEST", "Confermati");
    	}else if(obj instanceof WaitingCardsAdapter.ViewHolder){
    		WaitingCardsAdapter.ViewHolder vh = (WaitingCardsAdapter.ViewHolder) obj;
    		clickListener.onCardClicked(vh.id);
    	}
    }
    
}