package io.github.colriot.rssreaderdemo;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

/**
 * @author Sergey Ryabov <colriot@gmail.com>
 *         16/03/15
 */
public class BaseActivityTest<T extends Activity> extends ActivityInstrumentationTestCase2<T> {
  private App app;

  public BaseActivityTest(Class<T> activityClass) {
    super(activityClass);
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();
    app = (App) getInstrumentation().getTargetContext().getApplicationContext();
    app.setUseMocks(true);

    getActivity();
  }

  @Override protected void tearDown() throws Exception {
    app.setUseMocks(false);
    super.tearDown();
  }
}
