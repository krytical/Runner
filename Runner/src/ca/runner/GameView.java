package ca.runner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class GameView extends SurfaceView {
	GameLoop gameLoop;
	SurfaceHolder holder;

	public static int globalxSpeed = 8;

	Bitmap playerbmp;
	Bitmap groundbmp;
	Bitmap buttonsbmp;
	int xx = 0;

	private List<Player> player = new ArrayList<Player>();
	private List<Ground> ground = new ArrayList<Ground>();
	private List<Buttons> buttons = new ArrayList<Buttons>();

	public static int Score = 0;
	public static int HighScore = 0;
	public static int AchievementScore = 0;

	private static SharedPreferences prefs;

	private int lastScore = 0;

	private String saveAchievementScore = "AchievementScore";
	private String saveScore = "HighScore";
	private String Menu = "Runner";

	public GameView(Context context) {
		super(context);
		prefs = context.getSharedPreferences("ca.runner.RunnerGameActivity",context.MODE_PRIVATE);

		String spackage ="ca.runner.RunnerGameActivity";

		HighScore = prefs.getInt(saveScore , 0);
		AchievementScore = prefs.getInt(saveAchievementScore, 0);

		gameLoop = new GameLoop(this);

		holder = getHolder();
		holder.addCallback(new Callback() {

			public void surfaceDestroyed(SurfaceHolder arg0) {
				// TODO Auto-generated method stub
				Score =0;
				prefs.edit().putInt(saveScore,HighScore).commit();
				prefs.edit().putInt(saveAchievementScore,AchievementScore).commit();
				gameLoop.runner = false;		
			}

			public void surfaceCreated(SurfaceHolder arg0) {
				// TODO Auto-generated method stub
				gameLoop.setRunner();
				gameLoop.start();
			}

			public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub			

			}

		});
		playerbmp = BitmapFactory.decodeResource(getResources(), R.drawable.player);
		groundbmp = BitmapFactory.decodeResource(getResources(), R.drawable.ground);
		buttonsbmp = BitmapFactory.decodeResource(getResources(), R.drawable.buttons);
	}

	@Override
	public boolean onTouchEvent(MotionEvent e){
		for(Player player1: player)
		{
			player1.ontouch();
		}
		if (Menu =="Mainmenu")
		{
			for(int i = 0; i < buttons.size(); i++){
				if (buttons.get(i).getState() == 1){ 
					if ((buttons.get(i).getX()<e.getX() && buttons.get(i).getX()+84>e.getX())){
						if (buttons.get(i).getY()<e.getY() && buttons.get(i).getY()+32>e.getY()){
							Menu = "Runner";
							startGame();}	
					}
				}

			}
		}
		return false;
	}

	public void update(){
		if(Menu=="Runner"){
			Score += 5;
			lastScore = Score;
			deleteground();

			if (Score >= 10000 && AchievementScore == 0)
			{
				AchievementScore = 1;
			}
			if (Score > HighScore)
			{
				HighScore = Score;
			}
		}
	}

	public void addground(){
		while(xx < this.getWidth()+Ground.width){
			ground.add(new Ground(this,groundbmp,xx,0));
			xx += groundbmp.getWidth();
		}
	}

	public void deleteground(){
		for (int i = ground.size()-1;i >= 0; i--){
			int groundx = ground.get(i).returnX();
			if (groundx<=-Ground.width){
				ground.remove(i);
				ground.add(new Ground(this,groundbmp,groundx+this.getWidth()+Ground.width,0));
			}
		}
	}
	public void startGame(){
		for(int i = 0; i < buttons.size(); i++){
			buttons.remove(i);
		}
		player.add(new Player(this,playerbmp,50,50));
	}

	public void endGame(){
		Menu  = "Mainmenu";
		buttons.add(new Buttons(this,buttonsbmp,this.getWidth()/2-64,this.getHeight()/2,3));
		buttons.add(new Buttons(this,buttonsbmp,this.getWidth()/2-64,this.getHeight()/2+48,1));
		player.remove(0);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		update();
		canvas.drawColor(Color.BLACK);
		if (Menu=="Mainmenu"){
			for(Buttons buttons: buttons){
				buttons.onDraw(canvas);
			}
		}

		if (Menu == "Runner"){
			addground();
			Paint textpaint = new Paint();
			textpaint.setTextSize(32);
			canvas.drawText("Score: "+String.valueOf(Score), 0, 32, textpaint);
			canvas.drawText("High Score: "+String.valueOf(HighScore), 0, 64, textpaint);
			if (AchievementScore == 0){
				canvas.drawText("Score 10000 - Not Complete", 0, 128, textpaint);
			}
			else if (AchievementScore == 1){
				canvas.drawText("Score 10000 - Complete", 0, 128, textpaint);
			}
			for(Ground ground1: ground){
				ground1.onDraw(canvas);
			}
			for(Player player1: player){
				player1.onDraw(canvas);
			}
			break;
		}

		if (Menu=="Mainmenu"){
			Paint textpaint = new Paint();
			textpaint.setTextSize(32);
			canvas.drawText("Score: "+String.valueOf(lastScore), canvas.getWidth()/2, canvas.getHeight()/2, textpaint);

		}
	}
}