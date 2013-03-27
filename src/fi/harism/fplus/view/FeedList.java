package fi.harism.fplus.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class FeedList extends ListView {

	private ScrollObserver mScrollObserver;
	private float mTouchY;

	public FeedList(Context context) {
		super(context);
	}

	public FeedList(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FeedList(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if (super.dispatchTouchEvent(event)) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mTouchY = event.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				if (event.getY() > mTouchY) {
					mScrollObserver.onScrollDown();
				}
				if (event.getY() < mTouchY) {
					mScrollObserver.onScrollUp();
				}
				mTouchY = event.getY();
				break;
			}
			return true;
		}
		return false;
	}

	public void setScrollObserver(ScrollObserver scrollObserver) {
		mScrollObserver = scrollObserver;
	}

	public interface ScrollObserver {
		public void onScrollDown();

		public void onScrollUp();
	}

}
