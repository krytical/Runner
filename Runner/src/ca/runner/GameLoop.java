package ca.runner;

import android.annotation.SuppressLint;
import android.graphics.Canvas;

//Static values
//MAX FPS is the fps you want to game at.
//Frame skips is if the phone is slower, how many frames to skip.
//Skipping more frames = smoother gameplay on slow phones. Skip too much and it's stupid.
//Frame period is just the frame period (google)
//
//Timers
//beginTime means when the cycle starts (this is a gameloop)
//timeDiff means the time it took for the cycle to run
//sleepTime is if you exited the game
//framesskipped frames being skipped
//
//run method
//1. reset frames skipped, update game state, render state to screen and draw on the canvas
//2. Checks how long the cycle took
//3. Calculate sleep time
//4. Checks sleep - if > 0 it's ok, if it's < 0 then elements on screen need to update
//
//This class essentially updates the elements on screen, and etc.

@SuppressLint("WrongCall")
public class GameLoop extends Thread{
	private GameView view;
	private final static int 	MAX_FPS = 40;
	private final static int	MAX_FRAME_SKIPS = 5;
	private final static int	FRAME_PERIOD = 1000 / MAX_FPS;

	boolean runner = false;

	public GameLoop(GameView view){
		this.view = view;
	}

	public void setRunner(){
		runner = true;
	}
	@Override
	public void run() {
		Canvas canvas;
		long beginTime;	
		long timeDiff;		
		int sleepTime;		
		int framesSkipped;	

		sleepTime = 0;

		while (runner) {
			canvas = null;
			try {
				canvas = view.holder.lockCanvas();
				synchronized (view.holder) {
					beginTime = System.currentTimeMillis();
					framesSkipped = 0;
					this.view.onDraw(canvas);
					timeDiff = System.currentTimeMillis() - beginTime;
					sleepTime = (int)(FRAME_PERIOD - timeDiff);

					if (sleepTime > 0) {
						try {
							Thread.sleep(sleepTime);
						} catch (InterruptedException e) {}
					}

					while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
						this.view.update();
						sleepTime += FRAME_PERIOD;
						framesSkipped++;
					}
				}
			} finally {
				if (canvas != null) {
					view.holder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}
}