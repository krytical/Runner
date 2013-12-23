package ca.runner;

import java.util.ArrayList;

//Game view basically loads all your image files to actually
//draw onto the screen. It loads the counter for shield, saves
//score and change the achievement completion.
//surfaceDestroyed
//Method basically resets everything, and stop running the game.
//surfaceCreated
//Method starts the game and starts all the timers (score, shield etc.)
//onTouchEvent
//Allows you to restart the game by clicking the restart button etc.
//The timers are updated below that method. It counts the score by adding 5 and etc.
//The methods below that are more complicated.
//It counts all the shield timers and when they appear. Basically generates the next shield.
//Same with the spikes and coins.
//Adding ground is by getting the screen width and adding the ground image on to the screen.
//
//Mainmenu removes everything, all the coins, spikes, shields and the player.
//Runner does the opposite, draws the player, score text, ground and etc.
//
//Mainmenu is the screen without the game
//Runner is the game screen where the game is actually running.
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
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

//Sets the speed of the game (globalxSpeed) how fast things move towards you.
//Contains all the images
//Initialize the score and achievement etc.
//Gives the shield timer, zero's the timer for coins/spikes/shields appearing
//Saves last score.
//
//surfaceDestroyed
//1. Clears everything, scores, etc.
//2. Sets the game to stop running.
//
//surfaceCreated
//1. Set the game running.
//
//Adding the images
//_____.add sets the images and etc.
//
//onTouchEvent - what happenes when touched
//First if is at mainmenu
//If you click the button on Manimenu, it will start the game.
//
//update
//When game is running, update updates the score and achievement status.
//Saves highscore if score > last high score.
//
//updatetimers
//update the timers, adding spikes, shields and coins.
//Basically sets when they appear.
//
//endGame
//Sets the world state to Mainmenu
//Resets all timers, and removes everything. Adds the buttons
//
//onDraw
//Draws Mainmenu (buttons) if at state Mainmenu
//Draws Runner (game) if at state runner

@SuppressLint("DrawAllocation")
public class GameView extends SurfaceView {

	GameLoop gameLoop;
	SurfaceHolder holder;

	public static int globalxSpeed = 5;

	Bitmap playerbmp;
	Bitmap coinbmp;
	Bitmap groundbmp;
	Bitmap spikesbmp;
	Bitmap shieldbmp;
	Bitmap buttonsbmp;
	Bitmap backgroundbmp;
	int xx = 0;

	private List<Coin> coins = new ArrayList<Coin>();
	private Player player;
	private List<Ground> ground = new ArrayList<Ground>();
	private List<Spikes> spikes = new ArrayList<Spikes>();
	private List<Shield> shields = new ArrayList<Shield>();
	private List<Buttons> buttons = new ArrayList<Buttons>();

	private static Rect playerHitBox;
	private static Rect spike;
	private static Rect coin;
	private static Rect shield;

	// Counters
	public static int Score = 0;
	public static int CoinsCollected = 0;
	private static int PlayerShieldCountdown = 0;
	public static int HighScore = 0;

	// Timers

	private static int shieldTimer = 0;

	private static int coinTimer = 0;

	private static int spikeTimer = 0;

	private static int timerRandomShield = 0;
	private static int timerRandomSpikes = 1;


	// Counter String Values
	public static String scoreCountText = "Score: ";
	public static String coinCountText = "Coins: ";
	public static String shieldCountText = "Shield: ";
	public static String highScoreText = "High Score: ";

	// Counter String Position Values
	public static int scoreTextXPos = 0;
	public static int scoreTextYPos = 32;

	public static int coinTextXPos = 0;
	public static int coinTextYPos = 64;

	public static int shieldTextXPos = 0;
	public static int shieldTextYPos = 96;

	public static int highScoreTextXPos = 0;
	public static int highScoreTextYPos = 32;



	public static int Achievement10000 = 0; // False

	private boolean PlayerShielded = false;


	private static SharedPreferences prefs;

	private int lastScore = 0;

	private String saveAchievement10000 = "Achievement10000";
	private String saveScore = "Highscore";

	// gameState constants; either running the game or at the main menu

	private String gameRunning = "GAME_RUNNING";
	private String gameOver = "GAME_OVER";

	private String gameState = gameRunning; //start running the game

	public GameView(Context context) {
		super(context);
		prefs = context.getSharedPreferences("ca.runner",context.MODE_PRIVATE);

		String packageName ="ca.runner";

		HighScore = prefs.getInt(saveScore , 0);
		Achievement10000 = prefs.getInt(saveAchievement10000, 0);

		gameLoop = new GameLoop(this);

		holder = getHolder();
		holder.addCallback(new Callback() {

			public void surfaceDestroyed(SurfaceHolder arg0) {
				// TODO Auto-generated method stub
				Score = 0;
				CoinsCollected = 0;
				prefs.edit().putInt(saveScore,HighScore).commit();
				prefs.edit().putInt(saveAchievement10000,Achievement10000).commit();
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
		coinbmp = BitmapFactory.decodeResource(getResources(), R.drawable.coin);
		groundbmp = BitmapFactory.decodeResource(getResources(), R.drawable.ground);
		spikesbmp = BitmapFactory.decodeResource(getResources(), R.drawable.spikes);
		shieldbmp = BitmapFactory.decodeResource(getResources(), R.drawable.shield);
		buttonsbmp = BitmapFactory.decodeResource(getResources(), R.drawable.buttons);
		backgroundbmp = BitmapFactory.decodeResource(getResources(), R.drawable.background);
		shields.add(new Shield(this,shieldbmp,600,32));
		spikes.add(new Spikes(this,spikesbmp,400,0));
		spikes.add(new Spikes(this,spikesbmp,800,0));
		player = (new Player(this,playerbmp,50,50));
		coins.add(new Coin(this,coinbmp,120,32));
		coins.add(new Coin(this,coinbmp,50,0));
		// TODO Auto-generated constructor stub
	}


	@Override
	public boolean onTouchEvent(MotionEvent e){
		player.ontouch();

		if (gameState.equals(gameOver))
		{
			for(int i = 0; i < buttons.size(); i++){
				if (buttons.get(i).getState() == 1){   // Restart
					if ((buttons.get(i).getX()<e.getX() && buttons.get(i).getX()+84>e.getX())){
						if (buttons.get(i).getY()<e.getY() && buttons.get(i).getY()+32>e.getY()){
							gameState = "Runner";
							startGame();}	
					}
				}

			}
		}
		return false;

	}


	public void update(){
		if(gameState.equals(gameRunning)){
			Score += 1;
			lastScore = Score;
			updatetimers();
			deleteground();

			if (Score >= 10000 && Achievement10000 == 0)
			{
				Achievement10000 = 1;
			}
			if (Score > HighScore)
			{
				HighScore = Score;
			}
		}
	}

	public void updatetimers(){

		coinTimer ++;
		spikeTimer ++;
		shieldTimer ++;
		if (gameState.equals(gameRunning)){
			if (PlayerShielded){
				PlayerShieldCountdown --;
				if (PlayerShieldCountdown <= 0)
				{
					PlayerShielded = false;
				}
			}
			switch(timerRandomShield){

			case 0:
				if(shieldTimer >= 150){
					shields.add(new Shield(this,shieldbmp,this.getWidth()+32,0));
					Random randomShield = new Random();
					timerRandomShield = randomShield.nextInt(3);
					shieldTimer = 0;

				}break;
			case 1:
				if(shieldTimer >= 250){
					shields.add(new Shield(this,shieldbmp,this.getWidth()+32,0));
					Random randomShield = new Random();
					timerRandomShield = randomShield.nextInt(3);
					shieldTimer = 0;

				}break;
			case 2:
				if(shieldTimer >= 350){
					shields.add(new Shield(this,shieldbmp,this.getWidth()+32,0));
					Random randomShield = new Random();
					timerRandomShield = randomShield.nextInt(3);
					shieldTimer = 0;

				}break;
			}
			switch(timerRandomSpikes){

			case 0:
				if(spikeTimer >= 125)
				{
					spikes.add(new Spikes(this,spikesbmp,this.getWidth()+24,0));
					Random randomSpikes = new Random();
					timerRandomSpikes = randomSpikes.nextInt(3);
					spikeTimer = 0;
				}break;
			case 1:
				if(spikeTimer >= 175)
				{
					spikes.add(new Spikes(this,spikesbmp,this.getWidth()+24,0));
					Random randomSpikes = new Random();
					timerRandomSpikes = randomSpikes.nextInt(3);
					spikeTimer = 0;
				}break;

			case 2:
				if(spikeTimer >= 100)
				{
					spikes.add(new Spikes(this,spikesbmp,this.getWidth()+24,0));
					Random randomSpikes = new Random();
					timerRandomSpikes = randomSpikes.nextInt(3);
					spikeTimer = 0;
				}
				break;
			}

			if (coinTimer >= 100){
				Random randomCoin = new Random();
				int random;
				random = randomCoin.nextInt(3);
				switch(random){

				case 1:
					int currentcoin = 1;
					xx = 1;
					while(currentcoin <= 5){

						coins.add(new Coin(this,coinbmp,this.getWidth()+(32*xx),32));

						currentcoin++;
						xx++;
					}
					break;

				case 2:
					currentcoin = 1;
					coins.add(new Coin(this,coinbmp,this.getWidth()+32,32));
					coins.add(new Coin(this,coinbmp,this.getWidth()+64,48));
					coins.add(new Coin(this,coinbmp,this.getWidth()+96,32));
					coins.add(new Coin(this,coinbmp,this.getWidth()+128,48));
					coins.add(new Coin(this,coinbmp,this.getWidth()+160,32));
				}
				coinTimer = 0;
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
		for (int i = ground.size()-1;i >= 0; i--)
		{
			int groundx = ground.get(i).returnX();
			if (groundx<=-Ground.width){
				ground.remove(i);
				ground.add(new Ground(this,groundbmp,groundx+this.getWidth()+Ground.width,0));
			}
		}
	}
	public void startGame(){

		gameState = gameRunning;

		for(int i = 0; i < buttons.size(); i++){
			buttons.remove(i);
		}
		player = (new Player(this,playerbmp,50,50));
	}

	@SuppressLint("WrongCall")
	@Override
	protected void onDraw(Canvas canvas) {
		update();
		canvas.drawColor(Color.BLACK);
		canvas.drawBitmap(backgroundbmp, 0, 0, null);		
		// If the menu is Main menu, draw the button
		if (gameState.equals(gameOver))
		{
			for(Buttons buttons1: buttons)
			{
				buttons1.onDraw(canvas);
			}

		}
		// If the game is running, draw it
		if (gameState.equals(gameRunning)){
			addground();
			Paint textpaint = new Paint();
			textpaint.setTextSize(32);
			canvas.drawText(scoreCountText+String.valueOf(Score), 0, 32, textpaint);
			canvas.drawText(coinCountText+String.valueOf(CoinsCollected), coinTextXPos, coinTextYPos, textpaint);
			canvas.drawText(shieldCountText+String.valueOf(PlayerShieldCountdown), shieldTextXPos, shieldTextYPos, textpaint);
			canvas.drawText(highScoreText+String.valueOf(HighScore), canvas.getWidth()/2, highScoreTextYPos, textpaint);
			if (Achievement10000 == 0)
			{
				canvas.drawText("10000 Points - Not Complete", 0, 128, textpaint);
			}
			else if (Achievement10000 == 1)
			{
				canvas.drawText("10000 Points - Complete", 0, 128, textpaint);
			}

			for(Ground ground1: ground){
				ground1.onDraw(canvas);
			}
			player.onDraw(canvas);
		}
		for(int i = 0; i < spikes.size(); i++){
			spikes.get(i).onDraw(canvas);
			playerHitBox = player.GetBounds();
			spike = spikes.get(i).GetBounds();

			if (spikes.get(i).checkCollision(playerHitBox, spike)){
				if(!PlayerShielded){
					endGame();}
				else{
					spikes.remove(i);
					PlayerShielded = false;
					PlayerShieldCountdown = 0;						
				}
				break;
			}
		}
		for(int i = 0; i < coins.size(); i++){
			coins.get(i).onDraw(canvas);
			playerHitBox = player.GetBounds();
			coin = coins.get(i).GetBounds();
			if (coins.get(i).returnX() < 0-32){
				coins.remove(i);
			}
			else if (coins.get(i).checkCollision(playerHitBox, coin)){
				coins.remove(i);
				Score += 100;
				CoinsCollected += 1;
			}
		}
		for(int i = 0; i < shields.size(); i++){
			shields.get(i).onDraw(canvas);
			playerHitBox = player.GetBounds();
			shield = shields.get(i).GetBounds();

			if (shields.get(i).returnX() < 0-32){
				shields.remove(i);
			}
			else if(shields.get(i).checkCollision(playerHitBox, shield)){
				shields.remove(i);
				PlayerShielded = true;
				PlayerShieldCountdown = 100;
			}
		}

		if (gameState.equals(gameOver))
		{
			Paint textpaint = new Paint();
			textpaint.setTextSize(32);
			canvas.drawText("Score: "+String.valueOf(lastScore), canvas.getWidth()/2, canvas.getHeight()/2, textpaint);
		}
	}

	public void resetTimers(){
		coinTimer =0;
		spikeTimer =0;
		shieldTimer=0;
	}

	public void resetCounters(){
		Score = 0;
		CoinsCollected = 0;
		PlayerShieldCountdown = 0;
	}

	public void removeObjects(){
		for(int i = 0; i < coins.size(); i++)
		{
			coins.remove(i);
		}
		for(int i = 0; i < spikes.size(); i++)
		{
			spikes.remove(i);
		}
		for(int i = 0; i < shields.size(); i++)
		{
			shields.remove(i);
		}
	}
	public void endGame(){

		removeObjects();
		resetCounters();
		resetTimers();
		gameState  = gameOver;
		buttons.add(new Buttons(this,buttonsbmp,this.getWidth()/2-64,this.getHeight()/2,3));
		buttons.add(new Buttons(this,buttonsbmp,this.getWidth()/2-64,this.getHeight()/2+48,1));
	}
}