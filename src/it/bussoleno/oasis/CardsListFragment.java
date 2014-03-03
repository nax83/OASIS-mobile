package it.bussoleno.oasis;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;

public class CardsListFragment extends ListFragment {

	private OnStartScanListener listener;
	
	public CardsListFragment(){
		
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
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onActivityCreated(savedInstanceState);
    	getListView().setDividerHeight(0);
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
    
    
}