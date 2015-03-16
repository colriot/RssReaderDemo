package io.github.colriot.rssreaderdemo.model;

import java.util.List;
import rx.Observable;

/**
 * @author Sergey Ryabov <colriot@gmail.com>
 *         14/03/15
 */
public interface RssModel {

  Observable<Article> validateAndAddFeed(String feedUrl);

  Observable<List<Feed>> getFeeds();

  Observable<List<Article>> getArticles(Feed feed);

  Observable<List<Article>> getArticlesByQuery(String query);
}
