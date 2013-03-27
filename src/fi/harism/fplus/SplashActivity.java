package fi.harism.fplus;

import java.util.Timer;
import java.util.TimerTask;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;

public class SplashActivity extends Activity {
	
	private boolean mCancelLaunch;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_splash);
		fadeTitle();
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			mCancelLaunch = true;
		}
		return super.dispatchKeyEvent(event);
	}
	
	private void fadeCaption() {
		PropertyValuesHolder holderAlpha = PropertyValuesHolder.ofFloat(
				"alpha", 0, 1);
		ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(
				findViewById(R.id.textview_splash_caption), holderAlpha)
				.setDuration(500);

		anim.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationCancel(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				if (mCancelLaunch) {
					return;
				}
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						if (mCancelLaunch) {
							return;
						}
						Intent intent = new Intent(SplashActivity.this,
								LoginActivity.class);
						startActivity(intent);
						finish();
						overridePendingTransition(android.R.anim.slide_in_left,
								android.R.anim.slide_out_right);
					}					
				}, 2000);
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationStart(Animator animation) {
			}
		});

		anim.start();
	}

	private void fadeTitle() {
		PropertyValuesHolder holderAlpha = PropertyValuesHolder.ofFloat(
				"alpha", 0, 1);
		ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(
				findViewById(R.id.textview_splash_title), holderAlpha)
				.setDuration(500);

		anim.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationCancel(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				if (mCancelLaunch) {
					return;
				}
				fadeCaption();
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationStart(Animator animation) {
			}
		});

		anim.start();
	}

}
