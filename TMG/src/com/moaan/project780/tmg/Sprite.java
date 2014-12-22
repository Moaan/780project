package com.moaan.project780.tmg;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

public class Sprite {
	private int frame, runPosition, direction, x, y;
	private float velocity;
	private Bitmap sheet;
	private Rect src, dst;
	private int spriteWidth, spriteHeight, spriteX, spriteY;
	private int screenWidth, screenHeight, levelWidth;
	private int jump[] = {1,2,3,4,5,6,5,4,3,2,1}; //perhaps better to make background move down and keep player centered.
	
	public Sprite(Bitmap spriteSheet, int canvasWidth, int canvasHeight, int levelWidth) {
		sheet = spriteSheet;
		spriteWidth = sheet.getWidth()/9;
		spriteHeight = sheet.getHeight()/4;
		screenWidth = canvasWidth;
		screenHeight = canvasHeight;
		this.levelWidth = levelWidth;
		x = (screenWidth/2) - spriteWidth/2;
		y = (screenHeight/2) - spriteHeight/2 + 10;
		runPosition = 2;
		frame = 0;
	}
	
	public void setDirection(int direction) {
		this.direction = direction;
		if(direction > 0) {
			runPosition = 3;
		}else if(direction < 0) {
			runPosition = 1;
		}
		else {
			frame = 0;
			runPosition = 2;
		}
	}
	
	public void update(boolean atStart, boolean atEnd, Point playerPosition) {
		if(atStart) {
			x = playerPosition.x - spriteWidth/2;
		}else if(atEnd) {
			x = screenWidth - (levelWidth - playerPosition.x) - spriteWidth/2;
			/*
			x += (direction*newXposition);
			if(x > screenWidth) x = screenWidth - spriteWidth/2;*/
		}else {
			x = (screenWidth/2) - spriteWidth/2;
		}
	}
	
	public void draw(Canvas canvas) {
		frame = (++frame)%9;
		spriteX = frame*spriteWidth;
		spriteY = runPosition*spriteHeight;
		src = new Rect(spriteX, spriteY, spriteX+spriteWidth, spriteY+spriteHeight);
		dst = new Rect(x, y, x+spriteWidth, y+spriteHeight);
		canvas.drawBitmap(sheet, src, dst, null);
	}
}
