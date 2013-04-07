package fi.harism.fplus.container;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import fi.harism.fplus.R;

public class FeedMain extends RelativeLayout {

	private ViewGroup mContainerMenu;
	private FeedContent mFeedContent;
	private FeedInfo mFeedInfo;
	private View mViewDisabled;

	public FeedMain(Context context) {
		super(context);
	}

	public FeedMain(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FeedMain(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public boolean isInfoVisible() {
		return findViewById(R.id.container_info).getVisibility() == View.VISIBLE;
	}

	public boolean isMenuVisible() {
		return findViewById(R.id.container_menu).getVisibility() == View.VISIBLE;
	}

	@Override
	public void onFinishInflate() {
		super.onFinishInflate();

		mFeedContent = (FeedContent) findViewById(R.id.container_content);
		mContainerMenu = (ViewGroup) findViewById(R.id.container_menu);
		mFeedInfo = (FeedInfo) findViewById(R.id.container_info);
		mViewDisabled = findViewById(R.id.view_disabled);

		mContainerMenu.setVisibility(View.GONE);
		mFeedInfo.setVisibility(View.GONE);
		mFeedInfo.setOnTouchListener(new InfoTouchListener());
		mViewDisabled.setVisibility(View.GONE);
		mViewDisabled.setAlpha(0f);
	}

	public void setInfoVisible(boolean visible) {
		float scrollWidth = getWidth() * 0.8f;
		ViewGroup.LayoutParams layoutParams = mFeedInfo.getLayoutParams();
		layoutParams.width = (int) (scrollWidth);
		mFeedInfo.setLayoutParams(layoutParams);

		if (visible) {
			PropertyValuesHolder alpha, translation;
			alpha = PropertyValuesHolder.ofFloat("alpha",
					mViewDisabled.getAlpha(), 1f);

			long duration = 500;
			if (mFeedInfo.getVisibility() == View.VISIBLE) {
				translation = PropertyValuesHolder.ofFloat("translationX",
						mFeedInfo.getTranslationX(), 0);
				duration = (long) (500 * mFeedInfo.getTranslationX() / scrollWidth);
			} else {
				translation = PropertyValuesHolder.ofFloat("translationX",
						scrollWidth, 0);
			}
			ObjectAnimator alphaAnim = ObjectAnimator.ofPropertyValuesHolder(
					mViewDisabled, alpha);
			ObjectAnimator translationAnim = ObjectAnimator
					.ofPropertyValuesHolder(mFeedInfo, translation);

			alphaAnim.setDuration(duration);
			translationAnim.setDuration(duration);
			alphaAnim.start();
			translationAnim.start();

			mViewDisabled.setVisibility(View.VISIBLE);
			mFeedInfo.setVisibility(View.VISIBLE);
		} else {
			PropertyValuesHolder alpha, translation;
			alpha = PropertyValuesHolder.ofFloat("alpha",
					mViewDisabled.getAlpha(), 0f);
			translation = PropertyValuesHolder.ofFloat("translationX",
					mFeedInfo.getTranslationX(), scrollWidth);
			ObjectAnimator alphaAnim = ObjectAnimator.ofPropertyValuesHolder(
					mViewDisabled, alpha);
			ObjectAnimator translationAnim = ObjectAnimator
					.ofPropertyValuesHolder(mFeedInfo, translation);

			long duration = (long) (500 * (scrollWidth - mFeedInfo
					.getTranslationX()) / scrollWidth);
			alphaAnim.setDuration(duration);
			translationAnim.setDuration(duration);

			translationAnim.addListener(new Animator.AnimatorListener() {
				@Override
				public void onAnimationCancel(Animator animation) {
				}

				@Override
				public void onAnimationEnd(Animator animation) {
					mViewDisabled.setVisibility(View.GONE);
					mFeedInfo.setVisibility(View.GONE);
				}

				@Override
				public void onAnimationRepeat(Animator animation) {
				}

				@Override
				public void onAnimationStart(Animator animation) {
				}
			});
			alphaAnim.start();
			translationAnim.start();
		}
	}

	public void setMenuVisible(boolean visible) {
		float scrollWidth = getWidth() * 0.8f;
		ViewGroup.LayoutParams layoutParams = mContainerMenu.getLayoutParams();
		layoutParams.width = (int) (scrollWidth);
		mContainerMenu.setLayoutParams(layoutParams);

		if (visible) {
			mContainerMenu.setVisibility(View.VISIBLE);
			mFeedContent.setOnTouchListener(new ContentTouchListener());
			PropertyValuesHolder translation;
			translation = PropertyValuesHolder.ofFloat("translationX",
					mFeedContent.getTranslationX(), scrollWidth);
			ObjectAnimator translationAnim = ObjectAnimator
					.ofPropertyValuesHolder(mFeedContent, translation);
			translationAnim
					.setDuration((long) (500 * (scrollWidth - mFeedContent
							.getTranslationX()) / scrollWidth));
			translationAnim.start();
		} else {
			mFeedContent.setOnTouchListener(null);
			PropertyValuesHolder translation;
			translation = PropertyValuesHolder.ofFloat("translationX",
					mFeedContent.getTranslationX(), 0f);
			ObjectAnimator translationAnim = ObjectAnimator
					.ofPropertyValuesHolder(mFeedContent, translation);
			translationAnim.setDuration((long) (500 * mFeedContent
					.getTranslationX() / scrollWidth));
			translationAnim.addListener(new Animator.AnimatorListener() {
				@Override
				public void onAnimationCancel(Animator animation) {
				}

				@Override
				public void onAnimationEnd(Animator animation) {
					findViewById(R.id.container_menu).setVisibility(View.GONE);
				}

				@Override
				public void onAnimationRepeat(Animator animation) {
				}

				@Override
				public void onAnimationStart(Animator animation) {
				}
			});
			translationAnim.start();
		}
	}

	private class ContentTouchListener implements View.OnTouchListener {

		private float mMoveX;
		private float mStartX;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN: {
				mStartX = event.getX();
				break;
			}
			case MotionEvent.ACTION_MOVE: {
				mMoveX = mStartX - event.getX();

				float translationX = mFeedContent.getTranslationX() - mMoveX;
				if (translationX < 0) {
					translationX = 0;
				}
				if (translationX >= mContainerMenu.getWidth()) {
					translationX = mContainerMenu.getWidth();
				}

				mFeedContent.setTranslationX(translationX);
				break;
			}
			case MotionEvent.ACTION_UP: {
				if (mMoveX < 0) {
					setMenuVisible(true);
				} else {
					setMenuVisible(false);
				}
				break;
			}
			}

			return true;
		}

	}

	private class InfoTouchListener implements View.OnTouchListener {

		private float mMoveX;
		private float mStartX;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN: {
				mStartX = event.getX();
				break;
			}
			case MotionEvent.ACTION_MOVE: {
				mMoveX = mStartX - event.getX();

				float translationX = mFeedInfo.getTranslationX() - mMoveX;
				if (translationX < 0) {
					translationX = 0;
				}
				if (translationX >= mFeedInfo.getWidth()) {
					translationX = mFeedInfo.getWidth();
				}

				mFeedInfo.setTranslationX(translationX);
				break;
			}
			case MotionEvent.ACTION_UP: {
				if (mMoveX > 0) {
					setInfoVisible(true);
				} else {
					setInfoVisible(false);
				}
				break;
			}
			}

			return true;
		}

	}

}
