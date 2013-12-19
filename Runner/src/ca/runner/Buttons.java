package ca.runner;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Buttons {

	private int x;
	private int y;
	private Bitmap bmp;
	private GameView gameview;

	private int mColumnWidth = 4;
	private int mColumnHeight = 1;

	private int width;
	private int height;

	private int mcurrentFrame = 0;
	private Rect player1;

	public Buttons(GameView gameview, Bitmap bmp, int x, int y, int state)
	{
		this.x = x;;
		this.y=y;
		this.gameview = gameview;
		this.bmp = bmp;
		this.width = bmp.getWidth()/mColumnWidth;
		this.height = bmp.getHeight()/mColumnHeight;
		this.mcurrentFrame = state;

	}

	public void update(){	
	}	
	
	public int getState(){
		return mcurrentFrame;
	}

	public Rect GetBounds(){
		return new Rect(this.getX(),this.y,this.getX()+width,this.y+height);
	}

	public void onDraw(Canvas canvas){
		update();
		int srcX = mcurrentFrame*width;
		Rect src = new Rect(mcurrentFrame*width,0,srcX + width,32);
		Rect dst = new Rect(x,y,x+width,y+32);
		canvas.drawBitmap(bmp,src,dst,null);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
