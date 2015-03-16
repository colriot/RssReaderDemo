package io.github.colriot.rssreaderdemo.presenter;

import android.content.Context;
import io.github.colriot.rssreaderdemo.R;
import io.github.colriot.rssreaderdemo.model.Article;
import io.github.colriot.rssreaderdemo.model.Feed;
import io.github.colriot.rssreaderdemo.model.RssModel;
import io.github.colriot.rssreaderdemo.view.ArticleListActivity;
import io.github.colriot.rssreaderdemo.view.ConfigView;
import java.util.List;
import javax.inject.Inject;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author Sergey Ryabov <colriot@gmail.com>
 *         14/03/15
 */
public class ConfigPresenterImpl implements ConfigPresenter {
  private final RssModel model;
  private ConfigView view;
  private Subscription subscription;

  @Inject public ConfigPresenterImpl(RssModel model) {
    this.model = model;
  }

  @Override public void takeView(ConfigView v) {
    view = v;

    view.showLoading();
    reloadFeeds();
  }

  private void reloadFeeds() {
    subscription = model.getFeeds()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(new Observer<List<Feed>>() {
          @Override public void onCompleted() {
            view.hideLoading();
          }

          @Override public void onError(Throwable e) {
            Timber.e(e, "Failed reload.");
            view.hideLoading();
          }

          @Override public void onNext(List<Feed> feeds) {
            view.renderFeedList(feeds);
          }
        });
  }

  @Override public void dropView(ConfigView v) {
    view = null;
    if (subscription != null) {
      subscription.unsubscribe();
      subscription = null;
    }
  }

  @Override public void feedClicked(Feed feed) {
    final Context context = view.getContext();
    context.startActivity(ArticleListActivity.intent(context, feed));
  }

  @Override public void addFeedClicked(String feedUrl) {
    view.showLoading();
    subscription = model.validateAndAddFeed(feedUrl)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(new Observer<Article>() {
          @Override public void onCompleted() {
            reloadFeeds();
            view.clearAddText();
            Timber.d("onCompleted");
          }

          @Override public void onError(Throwable e) {
            Timber.e(e, "Failed reload.");
            view.showError(R.string.error);
            view.hideLoading();
          }

          @Override public void onNext(Article article) {
          }
        });
  }

  @Override public void searchClicked(String query) {
    final Context context = view.getContext();
    context.startActivity(ArticleListActivity.intent(context, query));
  }
}
