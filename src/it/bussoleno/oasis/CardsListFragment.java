package it.bussoleno.oasis;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

public class CardsListFragment extends ListFragment {

	public static CardsListFragment newInstance(){
		return new CardsListFragment();
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onActivityCreated(savedInstanceState);
    	
    }
    
    @Override
    public void onStart() {
        super.onStart();

    }

}