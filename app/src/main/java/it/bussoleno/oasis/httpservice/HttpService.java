package it.bussoleno.oasis.httpservice;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.bussoleno.oasis.Card;
import it.bussoleno.oasis.httpservice.models.Merchant;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HttpService extends IntentService {

	public static final String REQTYPE = "reqtype";

	public static final int REQLOGIN = 1;
	public static final int LOGIN_OK = 10;
	public static final int LOGIN_KO = 11;

	public static final int REQDETAILS = 2;
	public static final int DETAILS_OK = 20;
	public static final int DETAILS_KO = 21;

	public static final String RESOURCE_URL = "resource_url";
	public static final int REQCONFIRM = 3;
	public static final int CONFIRM_OK = 30;
	public static final int CONFIRM_KO = 31;
	
	private static final String TAG_ID = "id";
	private static final String TAG_DESC = "description";
	private static final String TAG_NAME = "name";
	private static final String TAG_DETAILS = "details";
	private static final String TAG_KIND = "kind";
	private static final String TAG_KIND_FAMILYNAME = "family_name";
	private static final String TAG_KIND_FIRSTNAME = "first_name";
	private static final String TAG_KIND_DESCRIPTION = "description";
	private static final String TAG_KIND_AUTHNUMBER = "authorization_number";
	private static final String TAG_KIND_CONCNUMBER = "concession_number";
	private static final String TAG_KIND_PLACEOWNER = "place_owner";
	private static final String PLACEOWNER_YES = "S";
	private static final String PLACEOWNER_NO = "N";
	private static final String TAG_VALUE = "value";
	private static final String TAG_PAST_PRESENCES = "past_presences";

	private final IBinder mBinder = new MyBinder();
	private ResultReceiver resultReceiver;

	public HttpService() {
		super("HttpService");
	}

	@Override
	public IBinder onBind(Intent arg0) {
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
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {
				resultReceiver.send(LOGIN_OK, null);
			}
			break;
		case REQDETAILS:

			String response;
			
			String resource_url = intent.getStringExtra(RESOURCE_URL);
			System.out.println("resource url is " + resource_url);
			try {

				response = ServerUtilities.get(resource_url, "");
				if (response != null) {

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    final Merchant merchant = gson.fromJson(response, Merchant.class);

                    String tmp = merchant.getValue(Merchant.PLACE_OWNER);
                    boolean isOwner = false;
                    if("S".equals(tmp)) {
                        isOwner = true;
                    }

                    Bundle b = new Bundle();
                    b.putParcelable(Card.TAG, new Card("" + merchant.id, merchant.name, merchant.description,
                            merchant.getValue(Merchant.AUTHORIZATION_NUMBER), merchant.getValue(Merchant.CONCESSION_NUMBER),
                            merchant.past_presences, isOwner));
					resultReceiver.send(DETAILS_OK, b);
				}

			} catch (IOException e) {
                e.printStackTrace();
                resultReceiver.send(DETAILS_KO, new Bundle());
            } catch (NumberFormatException nfe){
				nfe.printStackTrace();
				resultReceiver.send(DETAILS_KO, new Bundle());
			}
			
			break;
		case REQCONFIRM:
			
			Card card = intent.getParcelableExtra(Card.TAG);
			try {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("badge_id", card.mId);
				ServerUtilities.post(Config.ACTION_ADDPRESENCE, params);
				Bundle ret = new Bundle();
				ret.putParcelable(Card.TAG, card);
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
