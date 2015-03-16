package io.github.colriot.rssreaderdemo.presenter;

import io.github.colriot.rssreaderdemo.model.Feed;
import io.github.colriot.rssreaderdemo.view.ConfigView;

/**
 * @author Sergey Ryabov <colriot@gmail.com>
 *         14/03/15
 */
public interface ConfigPresenter extends BasePresenter<ConfigView> {
  void feedClicked(Feed feed);

  void addFeedClicked(String feedUrl);

  void searchClicked(String query);
}
