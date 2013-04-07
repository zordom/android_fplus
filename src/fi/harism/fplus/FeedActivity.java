package fi.harism.fplus;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import fi.harism.fplus.container.FeedContent;
import fi.harism.fplus.container.FeedMain;

public class FeedActivity extends Activity {

	private FeedContent mFeedContent;
	private FeedMain mFeedMain;

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (mFeedMain.isInfoVisible()) {
				mFeedMain.setInfoVisible(false);
				return true;
			}
			if (mFeedMain.isMenuVisible()) {
				mFeedMain.setMenuVisible(false);
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_feed);
		mFeedMain = (FeedMain) findViewById(R.id.container_main);
		mFeedContent = (FeedContent) findViewById(R.id.container_content);
		mFeedContent.setObserver(new FeedMainObserver());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mFeedContent.onDestroy();
	}

	private class FeedMainObserver implements FeedContent.Observer {

		@Override
		public void onButtonClicked(int buttonId) {
			switch (buttonId) {
			case R.id.button_footer_info:
				mFeedMain.setInfoVisible(true);
				break;
			case R.id.button_footer_chat:
			case R.id.button_header_menu:
				mFeedMain.setMenuVisible(true);
				mFeedMain.setInfoVisible(false);
				break;
			}
		}

	}

}
