package io.github.colriot.rssreaderdemo.view;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import butterknife.ButterKnife;
import io.github.colriot.rssreaderdemo.App;

/**
 * @author Sergey Ryabov <colriot@gmail.com>
 *         14/03/15
 */
public class BaseActivity extends ActionBarActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    App.get().inject(this);
  }

  @Override public void onSupportContentChanged() {
    super.onSupportContentChanged();
    ButterKnife.inject(this);
  }
}
