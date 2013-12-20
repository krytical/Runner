package ca.runner;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Spikes {

	private int x,y;
	private Bitmap bmp;
	private GameView gameview;
	private Rect player1;
	private Rect spikes1;
	
	public Spikes(GameView gameview, Bitmap spikesbmp,int x,int y){
		this.gameview = gameview;
		this.bmp = spikesbmp;
		this.x = x;
	}
	public boolean checkCollision(Rect player1, Rect spikes1){
		
		this.player1 = player1;
		this.spikes1 = spikes1;
		
		return Rect.intersects(player1, spikes1);
	}
	public Rect GetBounds()
	{
		return new Rect(this.x,this.y,this.x+bmp.getWidth(),this.y+bmp.getHeight());
	}
	
	public void Update(){
		x-=gameview.globalxSpeed;
		y = gameview.getHeight()-Ground.height-bmp.getHeight();
	}
	public int returnX(){
		return x;
	}

	public void onDraw(Canvas canvas){
		Update();
		int srcX = bmp.getWidth();
		Rect src = new Rect(0,0,srcX,bmp.getHeight());
		Rect dst = new Rect(x,y,x+bmp.getWidth(),y+bmp.getHeight());
		canvas.drawBitmap(bmp,src,dst,null);
	}
}
