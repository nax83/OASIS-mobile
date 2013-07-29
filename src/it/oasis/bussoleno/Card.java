package it.oasis.bussoleno;

import android.graphics.Bitmap;

public class Card {

	public final String mFirst;
	public final String mLast;
	public final Bitmap mAvatar;

	public Card(String first, String last, Bitmap avatar) {
		this.mFirst = first;
		this.mLast = last;
		this.mAvatar = avatar;
	}
}
