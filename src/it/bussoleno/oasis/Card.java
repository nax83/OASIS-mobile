package it.bussoleno.oasis;

public class Card {
	public final String mId;
	public final String mFullname;
	public final String mDesc;

	public Card(String id, String fullname, String desc) {
		this.mFullname = fullname;
		this.mDesc = desc;
		this.mId = id;
	}
}
