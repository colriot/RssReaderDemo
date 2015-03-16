package io.github.colriot.rssreaderdemo;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.OkHttpClient;
import dagger.Module;
import dagger.Provides;
import io.github.colriot.rssreaderdemo.model.Article;
import io.github.colriot.rssreaderdemo.model.Feed;
import io.github.colriot.rssreaderdemo.model.RssModel;
import io.github.colriot.rssreaderdemo.model.RssModelImpl;
import io.github.colriot.rssreaderdemo.presenter.ArticleListPresenter;
import io.github.colriot.rssreaderdemo.presenter.ArticleListPresenterImpl;
import io.github.colriot.rssreaderdemo.presenter.ConfigPresenter;
import io.github.colriot.rssreaderdemo.presenter.ConfigPresenterImpl;
import io.github.colriot.rssreaderdemo.view.ArticleActivity;
import io.github.colriot.rssreaderdemo.view.ArticleListActivity;
import io.github.colriot.rssreaderdemo.view.ConfigActivity;
import io.github.colriot.rssreaderdemo.view.SplashActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import rx.Observable;

import static java.util.Arrays.asList;

/**
 * @author Sergey Ryabov <colriot@gmail.com>
 *         15/03/15
 */
@Module(injects = {
    App.class,                  //
    SplashActivity.class,       //
    ConfigActivity.class,       //
    ArticleListActivity.class,  //
    ArticleActivity.class,      //
})
public class AppModule {

  static final int DEFAULT_READ_TIMEOUT_MILLIS = 20 * 1000; // 20s
  static final int DEFAULT_WRITE_TIMEOUT_MILLIS = 20 * 1000; // 20s
  static final int DEFAULT_CONNECT_TIMEOUT_MILLIS = 15 * 1000; // 15s

  private boolean useMocks;

  public AppModule(boolean useMocks) {
    this.useMocks = useMocks;
  }

  @Provides @Singleton OkHttpClient provideOkHttp() {
    final OkHttpClient client = new OkHttpClient();
    client.networkInterceptors().add(new StethoInterceptor());

    client.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
    client.setReadTimeout(DEFAULT_READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
    client.setWriteTimeout(DEFAULT_WRITE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
    return client;
  }

  @Provides Context provideApp() {
    return App.get();
  }
  @Provides @MainThread Handler provideMainThread() {
    return new Handler(Looper.getMainLooper());
  }

  @Provides ConfigPresenter provideConfigPresenter(ConfigPresenterImpl impl) {
    return impl;
  }
  @Provides ArticleListPresenter provideArticlesPresenter(ArticleListPresenterImpl impl) {
    return impl;
  }
  @Provides RssModel provideRssModel(RssModelImpl impl) {
    if (useMocks) {
      return new RssModel() {

        List<Feed> feeds = new ArrayList<>();

        @Override public Observable<Article> validateAndAddFeed(String feedUrl) {
          feeds.add(new Feed(feedUrl, feedUrl));
          return Observable.empty();
        }

        @Override public Observable<List<Feed>> getFeeds() {
          return Observable.just(feeds);
        }

        @Override public Observable<List<Article>> getArticles(Feed feed) {
          return Observable.empty();
        }

        @Override public Observable<List<Article>> getArticlesByQuery(String query) {
          return Observable.just(asList(
              new Article("Search title 1", "Search content 1", "http://example.com", null),
              new Article("Search title 2", "Search content 2", "http://example.com", null),
              new Article("Search title 3", "Search content 3", "http://example.com", null),
              new Article("Search title 4", "Search content 4", "http://example.com", null)
          ));
        }
      };
    } else {
      return impl;
    }

  }
}
