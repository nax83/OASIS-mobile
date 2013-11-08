package it.bussoleno.oasis;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class MainActivity extends FragmentActivity {

	private static final String CARDS_FRAGMENT = "cards_fragment";

    private static final int NUM_PAGES = 2;
    private static final int LIST_CHECKEDIN = 0;
    private static final int LIST_TOBECHECKEDIN = 1;
    
    private ViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private HashMap<Integer,Fragment> mPageReferenceMap = new HashMap<Integer, Fragment>();
	
	Dialog login;
	Dialog confirm;

	LoginTask mLoginTask = null;
	// public static final int REQUEST_CODE = 0x0bf7c0de;
	public static final int REQUEST_CODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

//		if (findViewById(R.id.fragment_container) != null) {
//			listFragment = CardsListFragment.newInstance();
//			getSupportFragmentManager().beginTransaction()
//					.add(R.id.fragment_container, listFragment, CARDS_FRAGMENT)
//					.commit();
//		}
		
		 // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

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
		//listFragment.setListAdapter(cardsAdapter);
		//listFragment.getListView().setDividerHeight(0);
		Fragment f = mPagerAdapter.getFragment(LIST_CHECKEDIN);
		if(f != null){
			System.out.println("List not null");
			((ListFragment)f).setListAdapter(cardsAdapter);
			((ListFragment)f).getListView().setDividerHeight(0);
		}
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
	/**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public  Fragment getItem(int position) {
        	Fragment fg;
        	if(position==LIST_CHECKEDIN){
        		fg = CardsListFragment.newInstance();
        	}else{
        		fg = CardsListFragment.newInstance();
        	}
        	mPageReferenceMap.put(position, fg);
        	return fg;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
        
    	public Fragment getFragment(int key) {
    	    return mPageReferenceMap.get(key);
    	}
    }
}
