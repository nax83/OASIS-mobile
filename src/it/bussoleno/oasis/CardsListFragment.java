package it.bussoleno.oasis;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;

public class CardsListFragment extends ListFragment {

	public CardsListFragment(){
		
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onActivityCreated(savedInstanceState);
    	getListView().setDividerHeight(0);
    	View emptyView = getActivity().getLayoutInflater().inflate(R.layout.emptylist, null);
    	getListView().setEmptyView(emptyView);
        ((ViewGroup) getListView().getParent()).addView(emptyView);
    }

    @Override
    public void onStart() {
        super.onStart();

    }
    
}