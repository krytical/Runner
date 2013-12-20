package ca.runner;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Shield {
	GameView gameview;
	Bitmap bmp;
	int x,y,y2;
	private Rect shield1;
	private Rect player1;

	public Shield(GameView gameview, Bitmap bmp, int x, int y)
	{
		this.gameview = gameview;
		this.bmp = bmp;
		this.x = x;
		this.y2 = y;
	}
	public boolean checkCollision(Rect player1, Rect shield1){

		this.player1 = player1;
		this.shield1 = shield1;

		return Rect.intersects(player1, shield1);
	}
	public Rect GetBounds()
	{
		return new Rect(this.x,this.y,this.x+bmp.getWidth(),this.y+bmp.getHeight());
	}

	public void Update(){
		x-=gameview.globalxSpeed;
		y = gameview.getHeight()-Ground.height-bmp.getHeight()-y2;
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
