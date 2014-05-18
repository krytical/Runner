package ca.runner;

import java.util.ArrayList;

import ca.runner.GameView;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class RunnerGameActivity extends Activity {
	private GameView gameView;
	private MediaPlayer bGMusic;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gameView = new GameView(this);
		setContentView(gameView);
		bGMusic = MediaPlayer.create(this, R.raw.game_music);
		if (!bGMusic.isPlaying()) {
			bGMusic.start();
			bGMusic.setLooping(true);
		}
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int m_nTotalW = dm.widthPixels;
		int  m_nTotalH = dm.heightPixels;
		// scale factor
		float m_fFrameS = (float)m_nTotalH / 640.0f;
		// compute our frame
		int m_nFrameW = (int) (960.0f * m_fFrameS);
		int m_nFrameH = m_nTotalH;
		// compute padding for our frame inside the total screen size
		
		int m_nPaddingY = 0;
		int m_nPaddingX = (m_nTotalW - m_nFrameW) / 2;
	}

//	@Override
//	public void onPause(){
//		super.onPause();
//		// gameView.gameLoop.runner = false;
//		bGMusic.stop();
//		ArrayList<MediaPlayer> soundsToRelease = gameView.getSounds();
//		for(MediaPlayer sound : soundsToRelease){
//			sound.release();
//		}
//		finish();
//	}
}