package io.github.colriot.rssreaderdemo.view;

import android.os.Bundle;
import android.os.Handler;
import io.github.colriot.rssreaderdemo.MainThread;
import io.github.colriot.rssreaderdemo.R;
import javax.inject.Inject;

/**
 * Useless activity, deprecated in Guidelines.
 *
 * @author Sergey Ryabov <colriot@gmail.com>
 *         16/03/15
 */
public class SplashActivity extends BaseActivity {

  @Inject @MainThread Handler mainThread;
  private Runnable startRealActivityTask = new Runnable() {
    @Override public void run() {
      startActivity(ConfigActivity.intent(SplashActivity.this));
    }
  };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
  }

  @Override protected void onStart() {
    super.onStart();
    mainThread.postDelayed(startRealActivityTask, 3000);
  }

  @Override protected void onStop() {
    mainThread.removeCallbacks(startRealActivityTask);
    super.onStop();
  }
}
