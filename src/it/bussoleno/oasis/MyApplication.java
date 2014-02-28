package it.bussoleno.oasis;

import java.util.ArrayList;
import android.app.Application;
import android.util.Log;
import android.widget.Toast;

public class MyApplication extends Application {

	private static final String TAG = "MyApplication";

	private ArrayList<Card> checkedInList = new ArrayList<Card>();
	private ArrayList<Card> waitingList = new ArrayList<Card>();

	public MyApplication() {
		super();
	}

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
			if(tmp.mId == c.mId){
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
		Toast.makeText(this, c.mFullname + " aggiunto in lista d'attesa",
				Toast.LENGTH_LONG).show();
	}

	public Card removeFromWaitList(int index) {
		return waitingList.remove(index);
	}

	public Card getCardFromWaitList(int index) {
		return waitingList.get(index);
	}

	public int getWaitListSize() {
		return waitingList.size();
	}

	public ArrayList<Card> getWaitingList() {
		return waitingList;
	}

	private void addToCheckedList(Card c) {
		if (checkedInList.indexOf(c) == -1) {
			checkedInList.add(0, c);
			Toast.makeText(this, c.mFullname + " aggiunto alla lista",
					Toast.LENGTH_LONG).show();
		} else {
			Log.d(TAG, c.mFullname + "already checked in");
		}
	}

	public Card removeFromCheckedList(int index) {
		return checkedInList.remove(index);
	}

	public Card getCardFromCheckedList(int index) {
		return checkedInList.get(index);
	}

	public int getCheckedInListSize() {
		return checkedInList.size();
	}

	public ArrayList<Card> getCheckedInList() {
		return checkedInList;
	}

}
