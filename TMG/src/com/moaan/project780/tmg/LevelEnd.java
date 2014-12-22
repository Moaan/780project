package com.moaan.project780.tmg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class LevelEnd extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_end);
	}
	
	public void goToMenu(View v) {
		Intent intent = new Intent(this, MenuActivity.class);
		startActivity(intent);
	}

}
