package it.bussoleno.oasis;

import java.util.Observable;
import java.util.Observer;

import it.bussoleno.oasis.httpservice.HttpService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.FragmentTransaction;

public class MainActivity extends ActionBarActivity implements
		ActionBar.TabListener, CardsListFragment.OnStartScanListener,
		CardsListFragment.OnCardClickListener, Observer {

	private static final int NUM_PAGES = 2;
	private static final int LIST_CHECKEDIN = 0;
	private static final int LIST_TOBECHECKEDIN = 1;

	private ViewPager mPager;
	private ScreenSlidePagerAdapter mPagerAdapter;

	CardsListFragment mCheckedInList;
	CardsListFragment mWaitingList;

	private Dialog login;

	// public static final int REQUEST_CODE = 0x0bf7c0de;
	public static final int REQUEST_CODE = 1;

	private MyResultReceiver resultReceiver;
	private MyApplication myApp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mCheckedInList = new CardsListFragment();
		mWaitingList = new CardsListFragment();

		// Instantiate a ViewPager and a PagerAdapter.
		mPager = (ViewPager) findViewById(R.id.pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);

		mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// When swiping between pages, select the
				// corresponding tab.
				getSupportActionBar().setSelectedNavigationItem(position);
			}
		});

		actionBar.addTab(actionBar.newTab()
				.setText(getString(R.string.tab_checked)).setTabListener(this));

		actionBar.addTab(actionBar.newTab()
				.setText(getString(R.string.tab_queued)).setTabListener(this));

		login = new Dialog(this, android.R.style.Theme_Light_NoTitleBar);
		login.requestWindowFeature(Window.FEATURE_NO_TITLE);
		login.setCancelable(false);
		login.setContentView(R.layout.dialog_login);

		resultReceiver = new MyResultReceiver(new Handler());

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
		// login is temporary disabled
		// login.show();
		myApp = ((MyApplication) getApplication());
		myApp.getModel().addObserver(this);
//		myApp.getModel().addToList(
//				new Card("2", "Alessandro Nassisi Nassisi", "TEST TEST", 8,
//						false));
		updateAdapters();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		CardsAdapter cardsAdapter = new CardsAdapter(this, myApp.getModel()
//				.getCheckedInList());
//		WaitingCardsAdapter cardsWAdapter = new WaitingCardsAdapter(this, myApp
//				.getModel().getWaitingList());
//
//		mCheckedInList.setListAdapter(cardsAdapter);
//		mWaitingList.setListAdapter(cardsWAdapter);
	}
	
	private void updateAdapters(){
		CardsAdapter cardsAdapter = new CardsAdapter(this, myApp.getModel()
				.getCheckedInList());
		WaitingCardsAdapter cardsWAdapter = new WaitingCardsAdapter(this, myApp
				.getModel().getWaitingList());

		mCheckedInList.setListAdapter(cardsAdapter);
		mWaitingList.setListAdapter(cardsWAdapter);
	}

	private void submitLogin(String user, String pwd) {
		Button b = (Button) login.findViewById(R.id.btn_login);
		b.setVisibility(View.GONE);
		ProgressBar pb = (ProgressBar) login.findViewById(R.id.loading);
		pb.setVisibility(View.VISIBLE);
		EditText et1 = (EditText) login.findViewById(R.id.pwd_edit);
		EditText et2 = (EditText) login.findViewById(R.id.user_edit);
		et1.setEnabled(false);
		et2.setEnabled(false);
		Intent intent = new Intent(this, HttpService.class);
		intent.putExtra(HttpService.REQTYPE, HttpService.REQLOGIN);
		intent.putExtra("receiver", resultReceiver);
		startService(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public void scanCode() {
		System.out.println("Let's scan!");
		Intent intentScan = new Intent("it.oasis.bussoleno.SCAN");
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

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction transaction) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction transaction) {

		mPager.setCurrentItem(tab.getPosition());

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction transaction) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_settings:
			scanCode();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onStartScanSelected() {
		scanCode();
	}

	@Override
	public void onCardClicked(String id) {
		final Card c = myApp.getModel().findCardById(id);
		if (c != null && !c.mIsConfirmed) {
			new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setTitle(R.string.confirm)
					.setMessage(
							getString(R.string.body_confirm) + " "
									+ c.mFullname)
					.setPositiveButton(R.string.yes,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Log.d("MainActivity", c.mFullname);
									//request a check in
									Intent intent = new Intent(MainActivity.this, HttpService.class);
									intent.putExtra(HttpService.REQTYPE, HttpService.REQCONFIRM);
									intent.putExtra("receiver", resultReceiver);
									intent.putExtra("card", c);
									startService(intent);
								}
							}).setNegativeButton(R.string.no, null).show();

		}
	}
	
	@Override
	public void update(Observable observable, Object data) {
		updateAdapters();
	}

	class MyResultReceiver extends ResultReceiver {

		public MyResultReceiver(Handler handler) {
			super(handler);
		}

		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) {
			if (resultCode == HttpService.LOGIN_OK) {
				login.dismiss();
			}else if (resultCode == HttpService.CONFIRM_OK){
				Card card = resultData.getParcelable("card");
				myApp.getModel().confirmCard(card);
			}
		}
	}

	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if (position == LIST_CHECKEDIN) {
				return mCheckedInList;
			} else {
				return mWaitingList;
			}
		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}

	}

}
