package com.moaan.project780.tmg;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuActivity extends Activity {	
	private int invisible = android.view.View.INVISIBLE;
	private int visible = android.view.View.VISIBLE;
	
	private InformationOption informationOptions;
	private SettingsOption settingsOptions;
	
	private int playerSkin = 0; //play as fbi agent(0) or a professor(1)
	
	//sub-options of settings and information
	private ImageView skinImage;
	private Button skinButton;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_menu);
		
		informationOptions = new InformationOption();
		settingsOptions = new SettingsOption();
		
		skinImage = (ImageView) findViewById(R.id.characterIcon);
		skinButton = (Button) findViewById(R.id.characterButton);
	}

	public void startGame(View v) {
		Intent intent = new Intent(this, PlayActivity.class);
		intent.putExtra("playerSkin", playerSkin);
		startActivity(intent);
	}

	public void settings(View v) { //the gear icon
		settingsOptions.expandSettings(v);
	}
	
	public void information(View v) {//the i icon
		informationOptions.expandInformation(v);
	}
	
	//------sub-buttons of information and settings below
	public void showControls(View v) {
		Intent intent = new Intent(this, ShowControlsActivity.class);
		startActivity(intent);
	}
	
	public void showCredits(View v) {
		Intent intent = new Intent(this, ShowCreditsActivity.class);
		startActivity(intent);
	}
	
	public void changeSkin(View v) {
		if(playerSkin == 0) {
			playerSkin = 1;
			skinImage.setImageResource(R.drawable.prof_icon);
			skinButton.setText(R.string.prof_label);
		} else {
			playerSkin = 0;
			skinImage.setImageResource(R.drawable.fbi_icon);
			skinButton.setText(R.string.fbi_label);
		}
	}
	//------sub-buttons of information and settings above
	
	//Below are the two private classes which control the settings and information options.
	private class InformationOption {
		private LinearLayout expandedOptions;
		private int visibility;
		private ImageView backLight;
		
		public InformationOption() {
			expandedOptions = (LinearLayout) findViewById(R.id.information_options);
			visibility = expandedOptions.getVisibility();
			backLight = (ImageView) findViewById(R.id.infoLight);
		}
		
		public void expandInformation(View v) {
			if (visibility == visible) {
				backLight.setVisibility(invisible);
				visibility = invisible;
			}
			else {
				backLight.setVisibility(visible);
				visibility = visible;
			}
			expandedOptions.setVisibility(visibility);
		}
	}
	
	private class SettingsOption {
		private LinearLayout expandedOptions;
		private int visibility;
		private ImageButton settingsButton;
		private float buttonRotation;
		private AnimatorSet settingsAnimatorSet;
		private ObjectAnimator spinAnimator;		
		
		public SettingsOption() {
			expandedOptions = (LinearLayout) findViewById(R.id.settings_options);
			visibility = expandedOptions.getVisibility();
			settingsButton = (ImageButton) findViewById(R.id.settingsButton);
			settingsAnimatorSet = new AnimatorSet();
			spinAnimator = ObjectAnimator.ofFloat(settingsButton, "rotation", 0f, 0f);
			spinAnimator.setDuration(300);
		}
		
		public void expandSettings(View v) {
			if (visibility == visible) {
				rotateButton(90);
				visibility = invisible;
			}else {
				rotateButton(-90);
				visibility = visible;
			}
			expandedOptions.setVisibility(visibility);
		}
		
		private void rotateButton(int rotationChange) {
			buttonRotation = settingsButton.getRotation();
			spinAnimator.setFloatValues(buttonRotation, buttonRotation + rotationChange);
			settingsAnimatorSet.play(spinAnimator);
			settingsAnimatorSet.start();
		}
	}
}
