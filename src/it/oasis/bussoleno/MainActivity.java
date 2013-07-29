package it.oasis.bussoleno;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

public class MainActivity extends FragmentActivity {

	private static final String ACTION_LOGIN = "http://www.google.it";
	private static final String ACTION_GETRESOURCE = "http://www.google.it";

	private static final String TAG_ID = "id";
	private static final String TAG_DESC = "description";
	private static final String TAG_NAME = "name";
	private static final String TAG_DETAILS = "details";
	private static final String TAG_KIND = "kind";
	private static final String TAG_VALUE = "value";

	private static final String TAG_FIRST = "first";
	private static final String TAG_LAST = "last";
	
	private static final String CARDS_FRAGMENT = "cards_fragment";
	Dialog login;
	Dialog confirm;

	CardsListFragment listFragment = null;

	LoginTask mLoginTask = null;
	//public static final int REQUEST_CODE = 0x0bf7c0de;
	public static final int REQUEST_CODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
		ArrayList<HashMap<String, String>> tmp = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> tmp2 = new HashMap<String, String>(); 
		tmp2.put("first", "antonio");
		tmp2.put("last", "bianchi");
		tmp.add(tmp2);
		tmp2 = new HashMap<String, String>(); 
		tmp2.put("first", "mario");
		tmp2.put("last", "rossi");
		tmp.add(tmp2);
		tmp2 = new HashMap<String, String>(); 
		tmp2.put("first", "pinco");
		tmp2.put("last", "pallo");
		tmp.add(tmp2);
		tmp2 = new HashMap<String, String>(); 
		tmp2.put("first", "antonio");
		tmp2.put("last", "bianchi");
		tmp.add(tmp2);
		if (findViewById(R.id.fragment_container) != null) {
			ListAdapter adapter = new SimpleAdapter(MainActivity.this,
					tmp, R.layout.card_item, new String[] { TAG_FIRST, TAG_LAST },
					new int[] { R.id.first, R.id.last });
			listFragment = CardsListFragment.newInstance();
			listFragment.setListAdapter(adapter);
			getSupportFragmentManager().beginTransaction()
			.add(R.id.fragment_container, listFragment, CARDS_FRAGMENT).commit();
		}
		
		login = new Dialog(this, android.R.style.Theme_Light_NoTitleBar);
		login.requestWindowFeature(Window.FEATURE_NO_TITLE);
		login.setCancelable(false);
		login.setContentView(R.layout.dialog_login);

		Button b = (Button) login.findViewById(R.id.btn_login);
		b.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				submitLogin("dummy-usr", "dummy-pwd");
			}
		});

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(login.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.MATCH_PARENT;

		login.getWindow().setAttributes(lp);
		login.show();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	private void submitLogin(String user, String pwd) {

		mLoginTask = new LoginTask();
		mLoginTask.execute(user, pwd);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void scanCode(View v) {
		System.out.println("Let's scan!");
		Intent intentScan = new Intent(
				"com.phonegap.plugins.barcodescanner.SCAN");
		intentScan.addCategory(Intent.CATEGORY_DEFAULT);

		startActivityForResult(intentScan, REQUEST_CODE);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				System.out.println(intent.getStringExtra("SCAN_RESULT"));
				System.out.println(intent.getStringExtra("SCAN_RESULT_FORMAT"));
				new AsyncTask<String, Void, JSONObject>() {

					protected void onPreExecute() {
						confirm = new Dialog(MainActivity.this,
								R.style.PauseDialog);
						confirm.requestWindowFeature(Window.FEATURE_NO_TITLE);
						confirm.getWindow().setBackgroundDrawable(
								new ColorDrawable(
										android.graphics.Color.TRANSPARENT));
						confirm.setCancelable(true);
						confirm.setContentView(R.layout.dialog_confirm);
						WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
						lp.copyFrom(confirm.getWindow().getAttributes());
						lp.width = WindowManager.LayoutParams.MATCH_PARENT;
						lp.height = WindowManager.LayoutParams.MATCH_PARENT;
						confirm.getWindow().setAttributes(lp);
						confirm.show();

					};

					@Override
					protected JSONObject doInBackground(String... params) {
						JSONObject response = null;
						try {
							response = ServerUtilities.getJSON(
									ACTION_GETRESOURCE, "");
						} catch (IOException e) {
							e.printStackTrace();
						}
						return response;
					}

					@Override
					protected void onPostExecute(JSONObject result) {
						if (true || result != null) {
							if (confirm != null) {
								confirm.findViewById(R.id.footer)
										.setVisibility(View.VISIBLE);
								confirm.findViewById(R.id.text1).setVisibility(
										View.VISIBLE);
								confirm.findViewById(R.id.text2).setVisibility(
										View.VISIBLE);
								confirm.findViewById(R.id.text3).setVisibility(
										View.VISIBLE);
								confirm.findViewById(R.id.loading)
										.setVisibility(View.INVISIBLE);
							}
						}
					};

				}.execute("some nasty json");
			} else if (resultCode == Activity.RESULT_CANCELED) {
				System.out.println("RESULT_CANCELED");
			} else {
				System.out.println("Error");
			}
		}
	}

	private class LoginTask extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Button b = (Button) login.findViewById(R.id.btn_login);
			b.setVisibility(View.GONE);
			ProgressBar pb = (ProgressBar) login.findViewById(R.id.loading);
			pb.setVisibility(View.VISIBLE);
			EditText et1 = (EditText) login.findViewById(R.id.pwd_edit);
			EditText et2 = (EditText) login.findViewById(R.id.user_edit);
			et1.setEnabled(false);
			et2.setEnabled(false);
		}

		@Override
		protected Void doInBackground(String... paramArrayOfParams) {
			try {
				ServerUtilities.getJSON(ACTION_LOGIN, "");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Void result) {
			// TODO handle response
			if (login != null && login.isShowing())
				login.dismiss();
		};
	}

}
