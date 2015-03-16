package io.github.colriot.rssreaderdemo.model;

import android.content.Context;
import android.support.annotation.NonNull;
import io.github.colriot.rssreaderdemo.model.source.Network;
import io.github.colriot.rssreaderdemo.model.source.Storage;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * @author Sergey Ryabov <colriot@gmail.com>
 *         14/03/15
 */
public class RssModelImpl implements RssModel {
  private final Context context;
  private final Storage storage;
  private final Network network;

  @Inject public RssModelImpl(Context context, Storage storage, Network network) {
    this.context = context;
    this.storage = storage;
    this.network = network;
  }

  @Override public Observable<Article> validateAndAddFeed(@NonNull final String feedUrl) {
    return getFeeds()
        .flatMap(new Func1<List<Feed>, Observable<Feed>>() {
          @Override public Observable<Feed> call(List<Feed> feeds) {
            return Observable.from(feeds);
          }
        })
        .all(new Func1<Feed, Boolean>() {
          @Override public Boolean call(Feed feed) {
            return !feed.getUrl().equals(feedUrl); // Check if this feed already stored in DB.
          }
        })
        .flatMap(new Func1<Boolean, Observable<Article>>() {
          @Override public Observable<Article> call(Boolean isValid) {
            return isValid ? fetchFeedAndArticles(feedUrl) // Fire network if given feedUrl does not already exist.
                : Observable.<Article>error(new IllegalArgumentException("Feed already exists.")); // Throw otherwise.
          }
        });
  }

  private Observable<Article> fetchFeedAndArticles(@NonNull final String feedUrl) {
    return network.fetchArticles(feedUrl).doOnNext(new Action1<Article>() {
      Feed feed = null;
      long feedId = 0;

      @Override public void call(Article article) {
        if (feed == null) {
          feed = new Feed(feedUrl, feedUrl);
          feedId = storage.saveFeed(feed);
        }
        article.setFeedId(feedId);
        storage.saveArticle(article);
        Timber.d("Article saved: %s.", article.getUrl());
      }
    }).takeLast(0);
  }

  @Override public Observable<List<Feed>> getFeeds() {
    return Observable.defer(new Func0<Observable<List<Feed>>>() {
      @Override public Observable<List<Feed>> call() {
        return Observable.just(storage.getFeeds());
      }
    });
  }

  @Override public Observable<List<Article>> getArticles(final Feed feed) {
    return Observable.defer(new Func0<Observable<List<Article>>>() {
      @Override public Observable<List<Article>> call() {
        return Observable.just(storage.getArticlesByFeed(feed.getId()));
      }
    });
  }

  @Override public Observable<List<Article>> getArticlesByQuery(final String query) {
    return Observable.defer(new Func0<Observable<List<Article>>>() {
      @Override public Observable<List<Article>> call() {
        return Observable.just(storage.getArticlesByQuery(query));
      }
    });
  }
}
