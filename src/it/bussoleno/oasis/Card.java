package it.bussoleno.oasis;

import android.os.Parcel;
import android.os.Parcelable;

public class Card implements Parcelable {
	public final String mId;
	public final String mFullname;
	public final String mDesc;

	public Card(String id, String fullname, String desc) {
		this.mFullname = fullname;
		this.mDesc = desc;
		this.mId = id;
	}

	// Parcelling part
	public Card(Parcel in) {
		String[] data = new String[3];

		in.readStringArray(data);
		this.mId = data[0];
		this.mFullname = data[1];
		this.mDesc = data[2];
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
