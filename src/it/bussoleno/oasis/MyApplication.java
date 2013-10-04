package it.bussoleno.oasis;

import java.util.ArrayList;

import android.app.Application;

public class MyApplication extends Application {
	
	public ArrayList<Card> cards = new ArrayList<Card>();
	
	public MyApplication(){
		super();
		cards.add(new Card("0", "FirstName LastName", "this is a desc"));
	}
	
}
