package it.bussoleno.oasis.httpservice;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.bussoleno.oasis.Card;
import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;

public class HttpService extends IntentService {

	public static final String REQTYPE = "reqtype";

	public static final int REQLOGIN = 1;
	public static final int LOGIN_OK = 10;
	public static final int LOGIN_KO = 11;

	public static final int REQDETAILS = 2;
	public static final int DETAILS_OK = 20;
	public static final int DETAILS_KO = 21;

	public static final String RESOURCE_URL = "resource_url";
	private static final String TAG_ID = "id";
	private static final String TAG_DESC = "description";
	private static final String TAG_NAME = "name";
	private static final String TAG_DETAILS = "details";
	private static final String TAG_KIND = "kind";
	private static final String TAG_KIND_FAMILYNAME = "family_name";
	private static final String TAG_KIND_FIRSTNAME = "first_name";
	private static final String TAG_KIND_DESCRIPTION = "description";
	private static final String TAG_VALUE = "value";

	public static final int REQCONFIRM = 3;
	public static final int CONFIRM_OK = 30;
	public static final int CONFIRM_KO = 31;

	private final IBinder mBinder = new MyBinder();
	private ResultReceiver resultReceiver;

	public HttpService() {
		super("HttpService");
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	public class MyBinder extends Binder {
		HttpService getService() {
			return HttpService.this;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// do your intent modification here
		// if(intentIsDuplicate(intent))

		// make sure you call the super class to ensure everything performs as
		// expected
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		int req_type = intent.getIntExtra(REQTYPE, -1);
		resultReceiver = intent.getParcelableExtra("receiver");

		switch (req_type) {
		case REQLOGIN:
			try {
				ServerUtilities.getJSON(Config.ACTION_LOGIN, "");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				resultReceiver.send(LOGIN_OK, null);
			}
			break;
		case REQDETAILS:

			JSONObject response = null;
			String fullname="";
			String first = "";
			String last = "";
			String desc = "";
			String id = "";
			String resource_url = intent.getStringExtra(RESOURCE_URL);
			System.out.println("resource url is " + resource_url);
			try {
				response = ServerUtilities.getJSON(resource_url, "");
				if (response != null) {
					
					JSONArray details;
					id = response.getString(TAG_ID);
					details = response.getJSONArray(TAG_DETAILS);
					for (int i = 0; i < details.length(); i++) {
						JSONObject tmp = (JSONObject) details.get(i);
						String kind = tmp.getString(TAG_KIND);
						if (TAG_KIND_FAMILYNAME.equals(kind))
							first = tmp.getString(TAG_VALUE);
						if (TAG_KIND_FIRSTNAME.equals(kind))
							last = tmp.getString(TAG_VALUE);
						if (TAG_KIND_DESCRIPTION.equals(kind))
							desc = tmp.getString(TAG_VALUE);
					}
					if ("".equals(first) || "".equals(last) || "".equals(desc)) {
						// TODO: handle error
						System.out.println("Error parsing first last");
					}

					first = ("" + first.charAt(0)).toUpperCase()
							+ first.substring(1, first.length());
					last = ("" + last.charAt(0)).toUpperCase()
							+ last.substring(1, last.length());
					fullname = first + " " + last;
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Bundle b = new Bundle();
			b.putParcelable("card", new Card(id, fullname, desc));
			resultReceiver.send(DETAILS_OK, b);
			break;
		case REQCONFIRM:
			
			Card card = intent.getParcelableExtra("card");
			try {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("badge_id", card.mId);
				ServerUtilities.post(Config.ACTION_ADDPRESENCE, params);
				Bundle ret = new Bundle();
				ret.putParcelable("card", card);
				resultReceiver.send(CONFIRM_OK, ret);
			} catch (IOException e) {
				e.printStackTrace();
			}

			
			break;
		default:
			return;

		}

	}

}
