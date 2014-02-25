package it.bussoleno.oasis;

import java.util.ArrayList;
import android.app.Application;

public class MyApplication extends Application {
	
	private ArrayList<Card> checkedInList = new ArrayList<Card>();
	private ArrayList<Card> waitingList = new ArrayList<Card>();
	
	public MyApplication(){
		super();
	}
	
	public void addToList(Card c){
		System.out.println("is owner: " + c.mIsOwner);
		if(c.mIsOwner){
			addToCheckedList(c);
		}else addToWaitList(c);
	}
	
	private void addToWaitList(Card c){
		
		for(int i = 0; i < waitingList.size();i++){
			Card tmp = waitingList.get(i);
			if(tmp.mPastPresences > c.mPastPresences)
				continue;
			else {
				waitingList.add(i, c);
				return;
			}
		}
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
	
	private void addToCheckedList(Card c){
		checkedInList.add(0, c);
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
