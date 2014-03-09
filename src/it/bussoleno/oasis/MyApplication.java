package it.bussoleno.oasis;

import java.util.ArrayList;
import android.app.Application;
import android.util.Log;
import android.widget.Toast;

public class MyApplication extends Application {

	private static final String TAG = "MyApplication";
	private Model cards;


	public MyApplication() {
		super();
		cards = new Model();
	}

	
	public Model getModel(){
		return cards;
	}

}
