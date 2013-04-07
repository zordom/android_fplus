package fi.harism.fplus.container;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class FeedInfo extends LinearLayout {

	private View.OnTouchListener mTouchListener;

	public FeedInfo(Context context) {
		super(context);
	}

	public FeedInfo(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FeedInfo(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if (mTouchListener != null) {
			mTouchListener.onTouch(this, event);
			super.dispatchTouchEvent(event);
			return true;
		}
		return super.dispatchTouchEvent(event);
	}

	@Override
	public void setOnTouchListener(View.OnTouchListener touchListener) {
		mTouchListener = touchListener;
		super.setOnTouchListener(touchListener);
	}

}
