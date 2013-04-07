package fi.harism.fplus.container;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import fi.harism.fplus.R;

public class FeedMain extends RelativeLayout {

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
	}

	public void setInfoVisible(boolean visible) {
		View disabled = findViewById(R.id.view_disabled);
		View info = findViewById(R.id.container_info);

		if (visible) {
			PropertyValuesHolder alpha, translation;
			alpha = PropertyValuesHolder.ofFloat("alpha", 0f, 1f);
			translation = PropertyValuesHolder.ofFloat("translationX",
					info.getWidth(), 0f);
			ObjectAnimator alphaAnim = ObjectAnimator.ofPropertyValuesHolder(
					disabled, alpha);
			alphaAnim.setDuration(500);
			alphaAnim.start();
			ObjectAnimator translationAnim = ObjectAnimator
					.ofPropertyValuesHolder(info, translation);
			translationAnim.setDuration(500);
			translationAnim.start();

			disabled.setVisibility(View.VISIBLE);
			info.setVisibility(View.VISIBLE);
		} else {
			PropertyValuesHolder alpha, translation;
			alpha = PropertyValuesHolder.ofFloat("alpha", 1f, 0f);
			translation = PropertyValuesHolder.ofFloat("translationX", 0f,
					info.getWidth());
			ObjectAnimator alphaAnim = ObjectAnimator.ofPropertyValuesHolder(
					disabled, alpha);
			alphaAnim.setDuration(500);
			alphaAnim.start();
			ObjectAnimator translationAnim = ObjectAnimator
					.ofPropertyValuesHolder(info, translation);
			translationAnim.setDuration(500);
			translationAnim.addListener(new Animator.AnimatorListener() {
				@Override
				public void onAnimationCancel(Animator animation) {
				}

				@Override
				public void onAnimationEnd(Animator animation) {
					findViewById(R.id.view_disabled).setVisibility(
							View.INVISIBLE);
					findViewById(R.id.container_info).setVisibility(
							View.INVISIBLE);
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

	public void setMenuVisible(boolean visible) {
		View menu = findViewById(R.id.container_menu);
		View content = findViewById(R.id.container_content);

		if (visible) {
			menu.setVisibility(View.VISIBLE);
			content.setOnTouchListener(new ContentTouchListener());
			PropertyValuesHolder translation;
			translation = PropertyValuesHolder.ofFloat("translationX",
					content.getTranslationX(), menu.getWidth());
			ObjectAnimator translationAnim = ObjectAnimator
					.ofPropertyValuesHolder(content, translation);
			translationAnim.setDuration(500);
			translationAnim.start();
		} else {
			content.setOnTouchListener(null);
			PropertyValuesHolder translation;
			translation = PropertyValuesHolder.ofFloat("translationX",
					content.getTranslationX(), 0f);
			ObjectAnimator translationAnim = ObjectAnimator
					.ofPropertyValuesHolder(content, translation);
			translationAnim.setDuration(500);
			translationAnim.addListener(new Animator.AnimatorListener() {
				@Override
				public void onAnimationCancel(Animator animation) {
				}

				@Override
				public void onAnimationEnd(Animator animation) {
					findViewById(R.id.container_menu).setVisibility(
							View.INVISIBLE);
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

				View content = findViewById(R.id.container_content);
				View menu = findViewById(R.id.container_menu);

				float translationX = content.getTranslationX() - mMoveX;
				if (translationX < 0) {
					translationX = 0;
				}
				if (translationX >= menu.getWidth()) {
					translationX = menu.getWidth();
				}

				content.setTranslationX(translationX);
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

}
