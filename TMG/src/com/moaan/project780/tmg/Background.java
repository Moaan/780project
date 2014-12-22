package com.moaan.project780.tmg;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

public class Background {
	private Bitmap background;
	private int imageWidth, imageHeight;
	private int xPosition, yPosition, screenWidth, screenHeight;
	private Rect src, dst;
	
	public Background(Bitmap spriteSheet, int screenWidth, int screenHeight) {
		background = spriteSheet;
		imageWidth = background.getWidth();
		imageHeight = background.getHeight();
		xPosition = 0;
		yPosition = imageHeight/2 - 50;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}
	
	public void update(boolean atStart, boolean atEnd, Point playerPosition) {
		if(atStart) xPosition = 0;
		else if(atEnd) xPosition = imageWidth - screenWidth;
		else xPosition = playerPosition.x - screenWidth/2;
		
		//for jumping
		yPosition += playerPosition.y;
	}
	
	public void draw(Canvas canvas) {
		src = new Rect(xPosition, yPosition, xPosition+screenWidth, yPosition + imageHeight/2);
		dst = new Rect(0, 0, screenWidth, screenHeight); //was using canvasWidth and canvasHeight
		canvas.drawBitmap(background, src, dst, null);
	}
	
	//get player position with respect to background image, not the screen
	public int getX() {return xPosition + screenWidth/2;}
	public int getY() {return yPosition + imageWidth/4;}

}
