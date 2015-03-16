package io.github.colriot.rssreaderdemo.view;

import android.support.annotation.StringRes;
import io.github.colriot.rssreaderdemo.model.Feed;
import java.util.List;

/**
 * @author Sergey Ryabov <colriot@gmail.com>
 *         14/03/15
 */
public interface ConfigView extends BaseView {
  void renderFeedList(List<Feed> feedList);

  void showLoading();

  void hideLoading();

  void clearAddText();

  void showError(@StringRes int msgId);
}
