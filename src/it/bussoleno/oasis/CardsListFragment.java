package it.bussoleno.oasis;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

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
    }

    @Override
    public void onStart() {
        super.onStart();

    }
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
    	// TODO Auto-generated method stub
    	super.onListItemClick(l, v, position, id);
    	
    }
    
    

}