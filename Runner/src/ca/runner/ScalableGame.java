package ca.runner;

import android.content.Context;
import android.widget.ImageView;

public class ScalableGame extends ImageView {
	int m_w,
		m_h;
	public ScalableGame(Context context, int w, int h) {
		super(context);
		m_w = w; m_h = h;
	}
	@Override 
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		this.setMeasuredDimension(m_w, m_h);
	}
}
