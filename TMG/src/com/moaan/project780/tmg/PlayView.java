package com.moaan.project780.tmg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.ImageView;

/*
 * This is the view for the play activity. 
 * It runs a separate thread for all the drawing animations.
 */
public class PlayView extends SurfaceView implements Runnable { 
	Thread gameLooper;
	SurfaceHolder holder;
	Boolean readyToRun = false;
	
	private Bitmap spriteSheet, backgroundSheet, pauseButton;
	private Player player;
	private Background background;
	//private long speedDelay; //was using this to manage speed of animation.
	private float scrollVelocity; //this is used by all drawable objects.
	private int direction, levelWidth, screenWidth, screenHeight;
	private Point playerPosition; //player position with respect to the background image (the level) length
	public boolean atBeginning = false; 
	public boolean endIsReached = false; //also could be used for ending level
	public boolean gameRunning = true;
	
	private PlayActivity thisActivity;
	
	//for jumping
	private int jumpPositions[] = {0,-3,-6,-9,-12,-15,-18,-21,-24,-27,-30,-33,-36-39,-42,-45,
						 45, 42, 39, 36, 33, 30, 27, 24, 21, 18, 15, 12, 9, 6, 3, 0};
	private int jumpIndex = 0;
	private Boolean currentlyJumping = false;
	
	// make array [ax1,ay1,ax2,ay2, bx1,by1,bx2,by2, cx1,cy1,cx2,cy2]
	// then, Obstacles object makes traps a, b, c
	
	// These are the image pixel positions of the traps * 4
	private int trapPositions[] = {2100,0,2400,0,5100,0,5400,0,8100,0,8400,0};  
	private Obstacles obstacles;


	public PlayView(Context context, int screenWidth, int screenHeight, int playerSkin) {
		super(context);
		thisActivity = (PlayActivity) context;
		holder = getHolder();
		backgroundSheet = BitmapFactory.decodeResource(getResources(), R.drawable.levelone_holes);
		levelWidth = backgroundSheet.getWidth();
		
		//spriteSheet = BitmapFactory.decodeResource(getResources(), R.drawable.fbi_walking);
		if(playerSkin == 0) spriteSheet = BitmapFactory.decodeResource(getResources(), R.drawable.fbi_walking);
		else if(playerSkin == 1) spriteSheet = BitmapFactory.decodeResource(getResources(), R.drawable.prof_walking);
		
		pauseButton = BitmapFactory.decodeResource(getResources(), R.drawable.pause_icon);
		
		//get screen size. may not look right unless in immersion mode.
		this.screenWidth = screenWidth; 
		this.screenHeight = screenHeight; 
		
		playerPosition = new Point((screenWidth/2) - spriteSheet.getWidth()/16, 0);
		
		obstacles = new Obstacles(trapPositions);
		
	}
	
	@Override
	public void run() {
		background = new Background(backgroundSheet, screenWidth, screenHeight);
		player = new Player(spriteSheet, screenWidth, screenHeight, levelWidth);
		while (readyToRun) {
			if(!holder.getSurface().isValid()) continue;
			Canvas canvas = holder.lockCanvas();
			try {
				//was using this to manage speed of animation:
				//Thread.sleep(speedDelay - (long)(scrollVelocity*direction));
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if(playerPosition.x > levelWidth ) {
				playerPosition.x = levelWidth;
			}else if(playerPosition.x < 0 ) {
				playerPosition.x =  0;
			}
			else playerPosition.x += (int)(scrollVelocity);//2.f);
			
			if(playerPosition.x < screenWidth/2) {
				atBeginning = true;
				endIsReached = false;
			}else if(playerPosition.x > (levelWidth - screenWidth/2)) {
				atBeginning = false;
				endIsReached = true;
				if(playerPosition.x == levelWidth) thisActivity.levelComplete();
			}else {
				atBeginning = false;
				endIsReached = false;
			}
			
			//jumping
			if(currentlyJumping){
				playerPosition.y = jumpPositions[++jumpIndex];
				if(jumpIndex == jumpPositions.length-1) {
					jumpIndex = 0;
					currentlyJumping = false;
				}
			}
						
			background.update(atBeginning, endIsReached, playerPosition);
			background.draw(canvas);
			player.update(atBeginning, endIsReached, playerPosition);
			player.draw(canvas);
			canvas.drawBitmap(pauseButton, 75, 75, null);
			holder.unlockCanvasAndPost(canvas);
			
			//check if player fell in any of the holes
			if(obstacles.trappedObject(playerPosition.x, playerPosition.y, player.getWidth(), player.getHeight())){
				thisActivity.endGame();
			}
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent touch) {
		Log.d("onTouch", "device has been touched, sire!");

		if((touch.getX() < screenWidth/5) && (touch.getY() < screenHeight/5)) {
			if(gameRunning) {
				pause();
				gameRunning = false;
			}
			else {
				gameRunning = true;
				resume();
			}
		}

		if(!currentlyJumping && touch.getY() > screenHeight*2/3){
			currentlyJumping = true;
		}
		return super.onTouchEvent(touch);
	}
	
	public void accelerate(float acceleration) {
		scrollVelocity += acceleration;
		//if((long)(scrollVelocity*direction) > speedDelay) scrollVelocity = ((float)speedDelay)*direction;
		if(scrollVelocity < 0.1f && scrollVelocity > -0.1f) {
			direction = 0;
		}else if(scrollVelocity > 0.5f) {
			direction = 1;
		}
		else if(scrollVelocity < -0.5f) {
			direction = -1;
		}
		//background.setVelocity((int)scrollVelocity); 
		player.setDirection(direction);
	}

	public void pause() { //should this be synchronized?
		readyToRun = false;
		
		//should following be surrounded in while loop as in tutorial?
		try {
			gameLooper.join(); 
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void resume() { // gets called before run()
		readyToRun = true;
		gameLooper = new Thread(this);
		gameLooper.start();
	}
}
