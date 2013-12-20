package ca.runner;

import android.app.Activity;
import android.os.Bundle;

public class RunnerGameActivity extends Activity {
	private GameView gameView;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gameView = new GameView(this);
		setContentView(gameView);
	}

	@Override
	public void onPause(){
		super.onPause();
		gameView.gameLoop.runner = false;
		finish();
	}
}