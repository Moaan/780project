package com.moaan.project780.tmg;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class ShowControlsActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_menu);
		setContentView(R.layout.layout_showcontrols);
	}
	
	public void returnToMenu(View v) {
		finish();
	}
}
