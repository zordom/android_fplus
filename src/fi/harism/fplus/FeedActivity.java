package fi.harism.fplus;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.facebook.Session;

import fi.harism.fplus.view.FeedList;
import fi.harism.fplus.view.FeedListItem;

public class FeedActivity extends Activity {

	private boolean mFooterVisible = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_feed);

		FeedList feedList = (FeedList) findViewById(R.id.feedlist);
		feedList.setAdapter(new FeedAdapter(this));
		feedList.setScrollObserver(new FeedList.ScrollObserver() {
			@Override
			public void onScrollDown() {
				if (!mFooterVisible) {
					mFooterVisible = true;
					View v = findViewById(R.id.textview_footer);
					PropertyValuesHolder translationYHolder;
					translationYHolder = PropertyValuesHolder.ofFloat(
							"translationY", v.getTranslationY(), 0);
					ObjectAnimator anim = ObjectAnimator
							.ofPropertyValuesHolder(v, translationYHolder);
					anim.setDuration(300);
					anim.start();
				}
			}

			@Override
			public void onScrollUp() {
				if (mFooterVisible) {
					mFooterVisible = false;
					View v = findViewById(R.id.textview_footer);
					PropertyValuesHolder translationYHolder;
					translationYHolder = PropertyValuesHolder.ofFloat(
							"translationY", v.getTranslationY(), v.getHeight());
					ObjectAnimator anim = ObjectAnimator
							.ofPropertyValuesHolder(v, translationYHolder);
					anim.setDuration(300);
					anim.start();
				}
			}
		});

		Log.d("SESSION", "session=" + Session.getActiveSession());
	}

	private class FeedAdapter extends BaseAdapter {

		private Activity mActivity;

		public FeedAdapter(Activity activity) {
			mActivity = activity;
		}

		@Override
		public int getCount() {
			return 20;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			FeedListItem feedListItem = (FeedListItem) convertView;
			if (feedListItem == null) {
				feedListItem = (FeedListItem) mActivity.getLayoutInflater()
						.inflate(R.layout.layout_feed_item, null);
			}

			ListView listView = (ListView) parent;
			if (listView.getFirstVisiblePosition() > position) {
				feedListItem.setRotationX(-30f, 0f, 300);
				feedListItem.setTranslationY(-100, 0, 300);
			}
			if (listView.getLastVisiblePosition() < position) {
				feedListItem.setRotationX(30f, 0f, 300);
				feedListItem.setTranslationY(100, 0, 300);
			}

			return feedListItem;
		}

	}

}
