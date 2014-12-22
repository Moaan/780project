package com.moaan.project780.tmg;
import android.graphics.Point;
import android.util.Log;

public class Obstacles {
	Trap traps[];
	Point trapStart, trapEnd;
	
	// gets an array [ax1,ay1,ax2,ay2, bx1,by1,bx2,by2, cx1,cy1,cx2,cy2]
	// then, make traps a, b, c
	public Obstacles(int trapSpots[]) {
		traps = new Trap[trapSpots.length/4];
		for(int i = 0, t = 0; i < traps.length; i++, t+=4)
		{
			trapStart = new Point(trapSpots[t], trapSpots[t+1]);
			trapEnd = new Point(trapSpots[t+2], trapSpots[t+3]);
			traps[i] = new Trap(trapStart, trapEnd);
		}
	}
	
	public boolean trappedObject(int x, int y, int width, int height) {
		for(int i = 0; i < traps.length; i++)
		{
			//make gameover
			if(traps[i].isTrapped(x, y, width, height)) return true;
		}
		return false;
	}
	
	class Trap {
		Point start, end;
		
		public Trap(Point start, Point end) {
			this.start = start;
			this.end = end;
		}
		
		public boolean isTrapped(int x, int y, int width, int height) {
			Log.d("trap", "trap position: start="+start.x+"endX="+end.x+" y="+start.y+"\nplayer position: x="+x+" y="+y+" width="+width+" height="+height);
			if(((x > start.x) && (x < end.x)) && ((y == start.y) && (y == end.y))) {
				Log.d("trap", "Trapped!");
				return true;
			}
			return false;
		}
	}
}
