package ca.runner;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Coin {

	private int x;
	private int y;
	private int y2;
	private Bitmap bmp;
	private GameView gameview;
	
	private int xSpeed =-GameView.globalxSpeed;
	
	private int mColumnWidth = 4;
	private int mColumnHeight = 1;
	
	private int width;
	private int height;
	
	private int mcurrentFrame = 0;
	private Rect player1;
	private Rect coin1;
	
	public Coin(GameView gameview, Bitmap bmp, int x, int y)
	{
		this.x = x;
		this.y2=y;
		this.gameview = gameview;
		this.bmp = bmp;
		this.width = bmp.getWidth()/mColumnWidth;
		this.height = bmp.getHeight()/mColumnHeight;
	}
	
	public void update()
	{
		x += xSpeed;
		y = gameview.getHeight()-Ground.height-y2-bmp.getHeight();
		
		if (mcurrentFrame >= (mColumnWidth-1))
		{
			mcurrentFrame = 0;
		}
		else
		mcurrentFrame += 1;
		

	}
		public int returnX(){
			return x;
		}
	
	public boolean checkCollision(Rect player1, Rect coin1){
		
		this.player1 = player1;
		this.coin1 = coin1;
		
		return Rect.intersects(player1, coin1);
	}
	public Rect GetBounds()
	{
		return new Rect(this.x,this.y,this.x+width,this.y+height);
	}
	
	public void onDraw(Canvas canvas){
		update();
		int srcX = mcurrentFrame*width;
		Rect src = new Rect(mcurrentFrame*width,0,srcX + width,32);
		Rect dst = new Rect(x,y,x+width,y+32);
		canvas.drawBitmap(bmp,src,dst,null);
	}
}
