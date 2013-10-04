package it.bussoleno.oasis;

import java.io.IOException;

import org.json.JSONException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class MainActivity extends FragmentActivity {

	private static final String CARDS_FRAGMENT = "cards_fragment";

	Dialog login;
	Dialog confirm;

	CardsListFragment listFragment = null;

	LoginTask mLoginTask = null;
	// public static final int REQUEST_CODE = 0x0bf7c0de;
	public static final int REQUEST_CODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (findViewById(R.id.fragment_container) != null) {
			listFragment = CardsListFragment.newInstance();
			getSupportFragmentManager().beginTransaction()
					.add(R.id.fragment_container, listFragment, CARDS_FRAGMENT)
					.commit();
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
		CardsAdapter cardsAdapter = new CardsAdapter(this);
		listFragment.setListAdapter(cardsAdapter);
		listFragment.getListView().setDividerHeight(0);
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

		Intent intentScan = new Intent("it.oasis.bussoleno.SCAN");

		//startActivityForResult(intentScan, REQUEST_CODE);
		startActivity(intentScan);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				System.out.println(intent.getStringExtra("SCAN_RESULT"));
				System.out.println(intent.getStringExtra("SCAN_RESULT_FORMAT"));
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
				ServerUtilities.getJSON(ServerUtilities.ACTION_LOGIN, "");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
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
