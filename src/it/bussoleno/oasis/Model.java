package it.bussoleno.oasis;

import java.util.ArrayList;
import java.util.Observable;

import android.util.Log;

public class Model extends Observable{

	private static final String TAG = "model";
	
	private ArrayList<Card> checkedInList = new ArrayList<Card>();
	private ArrayList<Card> waitingList = new ArrayList<Card>();
	
	public void addToList(Card c) {
		System.out.println("is owner: " + c.mIsOwner);
		if (c.mIsOwner) {
			addToCheckedList(c);
		} else
			addToWaitList(c);
	}
	
	private void addToWaitList(Card c) {
		int index = -1;
		for (int i = 0; i < waitingList.size(); i++) {
			Card tmp = waitingList.get(i);
			//if a card is already in the list, discard it
			if(tmp.mId.equals(c.mId)){
				Log.d(TAG, c.mFullname + "already in wait list");
				return;
			}
			//calculate the position of the new card in the list by its past presences
			//the for loop cannot be interrupted because we have to check for duplicates
			if (tmp.mPastPresences > c.mPastPresences)
				continue;
			else {
				//check if position has already been found
				if(index == -1){
					index = i;
				}
			}
		}
		if(index == -1){
			waitingList.add(c);
		}else {
			waitingList.add(index, c); 
		}
		setChanged();
		notifyObservers();
	}
	
	private void addToCheckedList(Card c) {
		if (checkedInList.indexOf(c) == -1) {
			c.mIsConfirmed = true;
			checkedInList.add(0, c);
//			Toast.makeText(this, c.mFullname + " aggiunto alla lista",
//					Toast.LENGTH_LONG).show();
		} else {
			Log.d(TAG, c.mFullname + "already checked in");
		}
		setChanged();
		notifyObservers();
	}
	
	public Card findCardById(String id){
		for (int i = 0; i < waitingList.size(); i++) {
			Card tmp = waitingList.get(i);
			if(tmp.mId.equals(id)){
				return tmp;
			}
		}
		return null;
	}
	

	public void confirmCard(Card c) {
		int pos = -1;
		for (int i = 0; i < waitingList.size(); i++) {
			Card tmp = waitingList.get(i);
			if(tmp.mId.equals(c.mId)){
				pos = i;
				break;
			}
		}
		if(pos != -1){
			c.mIsConfirmed = true;
			waitingList.set(pos, c);
			setChanged();
			notifyObservers();
		}
	}
	
	public boolean isCardPresent(Card c){
		for (int i = 0; i < waitingList.size(); i++) {
			Card tmp = waitingList.get(i);
			//if a card is already in the list, discard it
			Log.d(TAG, tmp.mId + " " + c.mId);
			if(tmp.mId.equals(c.mId)){
				Log.d(TAG, c.mFullname + "already in wait list");
				return true;
			}
		}
		
		for (int i = 0; i < checkedInList.size(); i++) {
			Card tmp = checkedInList.get(i);
			//if a card is already in the list, discard it
			Log.d(TAG, tmp.mId + " " + c.mId);
			if(tmp.mId.equals(c.mId)){
				Log.d(TAG, c.mFullname + "already in wait list");
				return true;
			}
		}
		Log.d(TAG, c.mFullname + " doesn't exist");
		return false;
	}
	
	public Card getCardFromCheckedList(int index) {
		if(index < 0 || index >= checkedInList.size())
			return null;
		return checkedInList.get(index);
	}

	public int getCheckedInListSize() {
		return checkedInList.size();
	}

	public ArrayList<Card> getCheckedInList() {
		return checkedInList;
	}
	
	public Card getCardFromWaitList(int index){
		if(index < 0 || index >= waitingList.size())
			return null;
		return waitingList.get(index);
	}
	
	public Card removeFromWaitList(int index) {
		return waitingList.remove(index);
	}

	public int getWaitListSize() {
		return waitingList.size();
	}
	
	public ArrayList<Card> getWaitingList() {
		return waitingList;
	}

}
