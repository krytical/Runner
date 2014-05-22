package ca.runner;

import java.util.List;

import ca.runner.GameView;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
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
	
	  @Override
	  protected void onPause() {
	    if (this.isFinishing()){
	      bGMusic.stop();
	    }
	    Context context = getApplicationContext();
	    ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    List<RunningTaskInfo> taskInfo = am.getRunningTasks(1);
	    if (!taskInfo.isEmpty()) {
	      ComponentName topActivity = taskInfo.get(0).topActivity; 
	      if (!topActivity.getPackageName().equals(context.getPackageName())) {
	        bGMusic.stop();
	      }
	    }
	    super.onPause();
	  }
	  
	  @Override
	  protected void onStop() {
	      if (bGMusic != null && bGMusic.isPlaying()) {
	          bGMusic.stop();
	      }
	      super.onPause();
	  }
}