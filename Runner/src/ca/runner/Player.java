package ca.runner;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Player {
	static int x;
	static int y;
	static int gravity = (int) 1.2;
	static int vspeed = 2;
	static int playerheight;
	static int playerwidth;
	static int jumppower = -10;

	private int width,height;

	private int mColumnWidth = 4;
	private int animationcolumn = 0;
	private int mColumnHeight = 2;
	private int mcurrentFrame = 0;
	private int animationposy = 0;
	private int animationstate = 0;

	Rect player1;
	Bitmap bmp;
	GameView gameview;
	private int i;
	public Player(GameView gameview, Bitmap bmp, int x, int y){
		this.x = x;
		this.y = y;
		this.gameview = gameview;
		this.bmp = bmp;
		this.width = bmp.getWidth()/mColumnWidth;
		this.height = bmp.getHeight()/mColumnHeight;
		playerheight=bmp.getHeight()/2;
	}
	
	public void update(){
		checkground();
		checkanimationstate();
		switchanimations();
	}
	
	public void checkanimationstate(){
		if (vspeed < 0){
			animationstate =2;
		}
		else if(vspeed > 0){
			animationstate =1;
		}
		else{
			animationstate = 0;
		}
	}
	public void switchanimations(){

		if(animationstate ==0){
			animationcolumn = 4;
			animationposy = 0;
			if (mcurrentFrame >= (animationcolumn-1)){
				mcurrentFrame = 0;
			}
			else
				mcurrentFrame += 1;
			}
		else if(animationstate ==1){
			mcurrentFrame = 0;
			animationcolumn = 0;
			animationposy=1;
		}
		else if(animationstate == 2){
			mcurrentFrame = 1;
			animationposy=1;
			animationcolumn = 0;
		}
	}
	
	public void checkground(){
		if (y < gameview.getHeight()-Ground.height-playerheight){
			vspeed+=gravity;
			if (y > gameview.getHeight()-Ground.height-playerheight-vspeed){
				vspeed = gameview.getHeight()-Ground.height-playerheight;
			}
		}
		else if (vspeed>0){
			vspeed = 0;
			y = gameview.getHeight()-Ground.height-playerheight;
		}
		y += vspeed;
	}

	public void ontouch(){
		if (y>= gameview.getHeight()-Ground.height-playerheight){
			vspeed = jumppower;
		}
	}

	public Rect GetBounds(){
		return new Rect(this.x,this.y,this.x+width,this.y+height);
	}

	public void onDraw(Canvas canvas){
		update();
		int srcX = mcurrentFrame*width;
		int srcY = animationposy*48;
		Rect src = new Rect(srcX,srcY,srcX + width,srcY+height);
		Rect dst = new Rect(x,y,x+(width),y+(height));
		canvas.drawBitmap(bmp,src,dst,null);
	}
}