package ca.runner;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;

import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class GameView extends SurfaceView {
	private GameLoop gameLoop;
	private SurfaceHolder holder;

	Bitmap playerbmp;
	private List<Player> player = new ArrayList<Player>();



	public GameView(Context context) {
		super(context);

		gameLoop = new GameLoop(this);

		holder = getHolder();
		holder.addCallback(new Callback() { // CTRL + Space tar farm allt

			public void surfaceDestroyed(SurfaceHolder arg0) {
				// TODO Auto-generated method stub

			}

			public void surfaceCreated(SurfaceHolder arg0) {
				// TODO Auto-generated method stub
				gameLoop.setRunning(true);
				gameLoop.start();


			}

			public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

		});
		playerbmp = BitmapFactory.decodeResource(getResources(), R.drawable.player);
		player.add(new Player(this,playerbmp,50,50));
		// TODO Auto-generated constructor stub
	}


	@Override
	public boolean onTouchEvent(MotionEvent e){
		for(Player pplayer: player)
		{
			pplayer.ontouch();
		}
		return false;

	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		for(Player pplayer: player)
		{
			pplayer.onDraw(canvas);
		}
	}
}
