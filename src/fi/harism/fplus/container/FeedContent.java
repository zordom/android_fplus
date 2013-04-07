package fi.harism.fplus.container;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import fi.harism.fplus.MainApplication;
import fi.harism.fplus.R;
import fi.harism.fplus.data.FeedData;
import fi.harism.fplus.data.FeedItemData;
import fi.harism.fplus.data.NetworkCache;
import fi.harism.fplus.view.FeedList;
import fi.harism.fplus.view.FeedListItem;

public class FeedContent extends RelativeLayout {

	private FeedAdapter mFeedAdapter;
	private FeedData mFeedData;
	private NetworkObserver mNetworkObserver;
	private Observer mObserver;
	private View.OnTouchListener mTouchListener;

	public FeedContent(Context context) {
		super(context);
	}

	public FeedContent(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FeedContent(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if (mTouchListener != null) {
			if (mTouchListener.onTouch(this, event)) {
				return true;
			}
		}
		return super.dispatchTouchEvent(event);
	}

	public void onDestroy() {
		MainApplication.getNetworkCache().removeNetworkObserver(
				mNetworkObserver);
	}

	@Override
	public void onFinishInflate() {
		super.onFinishInflate();

		ButtonObserver buttonObserver = new ButtonObserver();
		findViewById(R.id.button_header_menu)
				.setOnClickListener(buttonObserver);
		findViewById(R.id.button_footer_camera).setOnClickListener(
				buttonObserver);
		findViewById(R.id.button_footer_comment).setOnClickListener(
				buttonObserver);
		findViewById(R.id.button_footer_chat)
				.setOnClickListener(buttonObserver);
		findViewById(R.id.button_footer_info)
				.setOnClickListener(buttonObserver);

		mNetworkObserver = new NetworkObserver();
		MainApplication.getNetworkCache().addNetworkObserver(mNetworkObserver);

		findViewById(R.id.button_header_refresh).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								NetworkCache cache = MainApplication
										.getNetworkCache();
								mFeedData.setFeedItems(cache.getHomeFeed(false));
								post(new Runnable() {
									@Override
									public void run() {
										mFeedAdapter.notifyDataSetChanged();
									}
								});
							}
						}).start();
					}
				});

		new Thread(new Runnable() {
			@Override
			public void run() {
				NetworkCache cache = MainApplication.getNetworkCache();
				String userId = cache.getUserId();
				mFeedData = new FeedData(userId);
				mFeedData.setFeedItems(cache.getHomeFeed(true));
				post(new Runnable() {
					@Override
					public void run() {
						mFeedAdapter = new FeedAdapter(getContext());
						FeedList feedList = (FeedList) findViewById(R.id.feedlist);
						feedList.setAdapter(mFeedAdapter);
						feedList.setScrollObserver(new ScrollObserver());
					}
				});
				mFeedData.setFeedItems(cache.getHomeFeed(false));
				post(new Runnable() {
					@Override
					public void run() {
						mFeedAdapter.notifyDataSetChanged();
					}
				});
			}
		}).start();
	}

	public void setObserver(Observer observer) {
		mObserver = observer;
	}

	@Override
	public void setOnTouchListener(View.OnTouchListener touchListener) {
		mTouchListener = touchListener;
		super.setOnTouchListener(touchListener);
	}

	private class ButtonObserver implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			mObserver.onButtonClicked(v.getId());
		}
	}

	private class FeedAdapter extends BaseAdapter {

		private LayoutInflater mLayoutInflater;

		public FeedAdapter(Context context) {
			mLayoutInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return mFeedData.getFeedItemCount();
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
				feedListItem = (FeedListItem) mLayoutInflater.inflate(
						R.layout.layout_feed_item, null);
			}

			FeedItemData feedItem = mFeedData.getFeedItem(position);
			feedListItem.setData(feedItem);

			if (feedItem.getAnimateIn()) {
				feedItem.setAnimateIn(false);
				ListView listView = (ListView) parent;
				if (listView.getFirstVisiblePosition() > position) {
					feedListItem.setRotationX(-30f, 0f, 500);
					feedListItem.setTranslationY(-200, 0, 500);
				}
				if (listView.getLastVisiblePosition() < position) {
					feedListItem.setRotationX(30f, 0f, 500);
					feedListItem.setTranslationY(200, 0, 500);
				}
			}

			return feedListItem;
		}

	}

	private class NetworkObserver implements NetworkCache.NetworkObserver {

		private ObjectAnimator mRotationAnim;

		public NetworkObserver() {
			View v = findViewById(R.id.button_header_refresh);
			PropertyValuesHolder rotation;
			rotation = PropertyValuesHolder.ofFloat("rotation", 0, 360);
			mRotationAnim = ObjectAnimator.ofPropertyValuesHolder(v, rotation);
			mRotationAnim.setDuration(1000);
			mRotationAnim.setRepeatCount(ValueAnimator.INFINITE);
			mRotationAnim.setInterpolator(new LinearInterpolator());
		}

		@Override
		public void onNetworkFinish() {
			post(new Runnable() {
				@Override
				public void run() {
					findViewById(R.id.button_header_refresh).setClickable(true);
					mRotationAnim.end();
				}
			});
		}

		@Override
		public void onNetworkStart() {
			post(new Runnable() {
				@Override
				public void run() {
					findViewById(R.id.button_header_refresh)
							.setClickable(false);
					mRotationAnim.start();
				}
			});
		}
	}

	public interface Observer {
		public void onButtonClicked(int buttonId);
	}

	private class ScrollObserver implements FeedList.ScrollObserver {
		private boolean mFooterVisible = true;

		@Override
		public void onScrollDown() {
			if (!mFooterVisible) {
				mFooterVisible = true;
				View v = findViewById(R.id.container_footer);
				PropertyValuesHolder translationYHolder;
				translationYHolder = PropertyValuesHolder.ofFloat(
						"translationY", v.getTranslationY(), 0);
				ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(v,
						translationYHolder);
				anim.setDuration(300);
				anim.start();
			}
		}

		@Override
		public void onScrollUp() {
			if (mFooterVisible) {
				mFooterVisible = false;
				View v = findViewById(R.id.container_footer);
				PropertyValuesHolder translationYHolder;
				translationYHolder = PropertyValuesHolder.ofFloat(
						"translationY", v.getTranslationY(), v.getHeight());
				ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(v,
						translationYHolder);
				anim.setDuration(300);
				anim.start();
			}
		}
	}

}
