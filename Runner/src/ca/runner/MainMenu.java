package ca.runner;

import java.util.List;

import ca.runner.R;
import ca.runner.RunnerGameActivity;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainMenu extends Activity implements OnClickListener {
	private MediaPlayer bGMusic;
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		bGMusic = MediaPlayer.create(this, R.raw.game_music);
		bGMusic.setLooping(true);
		bGMusic.start();
		//click button
		View aboutButton = findViewById(R.id.button1);
		aboutButton.setOnClickListener(this);
	}
	
	public void onClick (View game) {
		switch (game.getId()) {
		case R.id.button1:
		Intent i = new Intent(this, RunnerGameActivity.class);
		startActivity(i);
		break;
		}
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
}
