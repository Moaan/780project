package com.moaan.project780.tmg;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;

public class PlayActivity extends Activity implements SensorEventListener {

	private SensorManager sensorManager;
	private Sensor sensorGyroscope;

	PlayView playView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// sets full screen immersive mode
		getWindow().getDecorView().setSystemUiVisibility(
						View.SYSTEM_UI_FLAG_LAYOUT_STABLE
								| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
								| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
								| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
								| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
								| View.INVISIBLE);
		
		//get player skin (fbi or prof sprite sheet) setting
		Bundle extras = getIntent().getExtras();
		
		// get screen size. May need to be in immersion mode to look good
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int viewWidth = size.x + 125; //125px is the hard-coded size of the navigation panel on Nexus 5
		int viewHeight = size.y;

		playView = new PlayView(this, viewWidth, viewHeight, extras.getInt("playerSkin"));
		setContentView(playView);
		
		// setting up sensors
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		/*
		 * Retrieve the default Sensor for the gyroscope.
		 */
		sensorGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		/*
		 * Register this activity as the listener for gyroscope events.
		 */
		sensorManager.registerListener(this, sensorGyroscope,
				SensorManager.SENSOR_DELAY_GAME);
	}
	
	public void levelComplete() {
		Intent intent = new Intent(this, LevelEnd.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onSensorChanged(SensorEvent tiltEvent) {
		float tiltAngle = tiltEvent.values[2];
		playView.accelerate((-2.2f) * tiltAngle); //right should be (+), left should be (-)
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {} // Not using this.

	@Override
	protected void onPause() {
		super.onPause();
		playView.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		playView.resume();
	}

	@Override
	protected void onStop() {
		// unregister to save power.
		sensorManager.unregisterListener(this);
		super.onStop();
	}
	
	public void endGame() { //called when player hits a hole.
		//Didn't have time for a game over screen
		finish();
	}

	@Override
	protected void onStart() {
		// re-register to resume.
		sensorManager.registerListener(this, sensorGyroscope,
				SensorManager.SENSOR_DELAY_GAME);
		super.onStart();
	}
}
