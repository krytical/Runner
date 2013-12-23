package ca.runner;

import ca.runner.R;
import ca.runner.RunnerGameActivity;
import android.app.Activity;
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
		setContentView(R.layout.main);
		bGMusic = MediaPlayer.create(this, R.raw.gamemusic);
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
}
