package ca.runner;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Player {
	static float x;
	static float y;
	static float gravity = (float) 1.2;
	static float vspeed = 1;
	static int playerheight;
	static int playerwidth;
	static int jumppower = -10;
	Rect playerr;
	Bitmap bmp;
	GameView gameview;
	public Player(GameView gameview, Bitmap bmp, float x, float y)
	{
		this.x = x;
		this.y = y;
		this.gameview = gameview;
		this.bmp = bmp;
		playerheight=bmp.getHeight();
	}
	public void update(){
		checkground();

	}

	public void checkground(){
		if (y < gameview.getHeight()-64-playerheight){
			vspeed+=gravity;
			if (y > gameview.getHeight()-64-playerheight-vspeed)
			{
				vspeed = gameview.getHeight()-64-y-playerheight;
			}
		}
		else if (vspeed>0)
		{
			vspeed = 0;
			y = gameview.getHeight()-64-playerheight;
		}
		y += vspeed;
	}

	public void ontouch(){
		if (y>= gameview.getHeight()-64-playerheight)
		{
			vspeed = jumppower;
		}
	}
	public void onDraw(Canvas canvas){
		update();
		canvas.drawBitmap(bmp, x, y, null);

	}
}
