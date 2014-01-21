package it.bussoleno.oasis;

import android.os.Parcel;
import android.os.Parcelable;

public class Card implements Parcelable {
	
	public static final String card = "CARD";
	public final String mId;
	public final String mFullname;
	public final String mDesc;
	public final boolean mIsOwner;
	
	public Card(String id, String fullname, String desc, boolean isOwner) {
		this.mFullname = fullname;
		this.mDesc = desc;
		this.mId = id;
		this.mIsOwner = isOwner;
	}

	// Parcelling part
	public Card(Parcel in) {
		String[] data = new String[3];

		in.readStringArray(data);
		this.mId = data[0];
		this.mFullname = data[1];
		this.mDesc = data[2];
		this.mIsOwner = in.readByte() != 0;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] { this.mId, this.mFullname,
				this.mDesc });
		dest.writeByte((byte) (this.mIsOwner ? 1 : 0));
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Card createFromParcel(Parcel in) {
			return new Card(in);
		}

		public Card[] newArray(int size) {
			return new Card[size];
		}
	};

}
