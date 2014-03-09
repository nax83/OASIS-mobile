package it.bussoleno.oasis;

import android.os.Parcel;
import android.os.Parcelable;

public class Card implements Parcelable {
	
	public static final String card = "CARD";
	public final String mId;
	public final String mFullname;
	public final String mDesc;
	public final String mAuthNum;
	public final String mConcNum;
	public final boolean mIsOwner;
	public final int mPastPresences;
	
	//it's not final because it's used internally
	public boolean mIsConfirmed = false;
	
	public Card(String id, String fullname, String desc, String auth_num, String conc_num, int pastPresences, boolean isOwner) {
		this.mFullname = fullname;
		this.mDesc = desc;
		this.mId = id;
		this.mIsOwner = isOwner;
		this.mPastPresences = pastPresences;
		this.mAuthNum = auth_num;
		this.mConcNum = conc_num;
	}

	// Parcelling part
	public Card(Parcel in) {
		String[] data = new String[5];
		in.readStringArray(data);
		this.mId = data[0];
		this.mFullname = data[1];
		this.mDesc = data[2];
		this.mAuthNum = data[3];
		this.mConcNum = data[4];
		this.mPastPresences = in.readInt();
		this.mIsOwner = in.readByte() != 0;
		this.mIsConfirmed = in.readByte() != 0;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] { this.mId, this.mFullname,
				this.mDesc, this.mAuthNum, this.mConcNum });
		dest.writeInt(this.mPastPresences);
		dest.writeByte((byte) (this.mIsOwner ? 1 : 0));
		dest.writeByte((byte)(this.mIsConfirmed ? 1 : 0));	
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
