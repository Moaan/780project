package com.moaan.project780.tmg;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

public class Player {
	private int frame, runPosition, x, y;
	private Bitmap spriteSheet;
	private Rect src, dst;
	private int spriteWidth, spriteHeight, spriteX, spriteY;
	private int screenWidth, screenHeight, levelWidth;
	private Boolean isJumping = false;
	
	public Player(Bitmap spriteSheet, int canvasWidth, int canvasHeight, int levelWidth) {
		this.spriteSheet = spriteSheet;
		spriteWidth = spriteSheet.getWidth()/9;
		spriteHeight = spriteSheet.getHeight()/4;
		screenWidth = canvasWidth;
		screenHeight = canvasHeight;
		this.levelWidth = levelWidth;
		x = (screenWidth/2) - spriteWidth/2;
		y = (screenHeight/2) - spriteHeight/2 + 10;
		runPosition = 2;
		frame = 0;
	}
	
	public int getWidth() {return spriteWidth;}
	public int getHeight() {return spriteHeight;}
	public int getY() {return y;}
	public int getX() {return x;}
	
	public void setDirection(int direction) {
		if(direction > 0) { //moving right
			runPosition = 3;
		}else if(direction < 0) { //moving left
			runPosition = 1;
		}
		else {
			runPosition = 2;
		}
	}
	
	public void update(boolean atStart, boolean atEnd, Point playerPosition) {
		if(atStart) {
			x = playerPosition.x - spriteWidth/2;
		}else if(atEnd) {
			x = screenWidth - (levelWidth - playerPosition.x) - spriteWidth/2;
		}else {
			x = (screenWidth/2) - spriteWidth/2;
		}
		if(playerPosition.y == -3) isJumping = true;
		else if(playerPosition.y == 3) isJumping = false;
		//If I want to have jump animation, it would be done here.
	}
	
	public void draw(Canvas canvas) {
		if(!isJumping) frame = (++frame)%8;
		spriteX = frame*spriteWidth;
		spriteY = runPosition*spriteHeight;
		src = new Rect(spriteX, spriteY, spriteX+spriteWidth, spriteY+spriteHeight);
		dst = new Rect(x, y, x+spriteWidth, y+spriteHeight);
		canvas.drawBitmap(spriteSheet, src, dst, null);
	}
}
