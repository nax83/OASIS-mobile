package it.bussoleno.oasis;

import java.util.ArrayList;

import android.app.Application;

public class MyApplication extends Application {
	
	private ArrayList<Card> checkedInList = new ArrayList<Card>();
	private ArrayList<Card> waitingList = new ArrayList<Card>();
	
	public MyApplication(){
		super();
		checkedInList.add(new Card("0", "FirstName LastName", "this is a desc"));
	}
	
	public void addToWaitList(Card c){
		waitingList.add(c);
	}
	
	public Card removeFromWaitList(int index){
		return waitingList.remove(index);
	}
	
	public Card getCardFromWaitList(int index){
		return waitingList.get(index);
	}
	
	public int getWaitListSize(){
		return waitingList.size();
	}
	
	public void addToCheckedListAt(int index, Card c){
		checkedInList.add(index, c);
	}
	
	public Card removeFromCheckedList(int index){
		return checkedInList.remove(index);
	}
	
	public Card getCardFromCheckedList(int index){
		return checkedInList.get(index);
	}
	
	public int getCheckedInListSize(){
		return checkedInList.size();
	}
	
}
