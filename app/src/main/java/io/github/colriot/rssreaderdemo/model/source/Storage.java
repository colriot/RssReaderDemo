package io.github.colriot.rssreaderdemo.model.source;

import android.content.Context;
import io.github.colriot.rssreaderdemo.model.Article;
import io.github.colriot.rssreaderdemo.model.Feed;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Singleton;
import nl.qbusict.cupboard.DatabaseCompartment;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * @author Sergey Ryabov <colriot@gmail.com>
 *         15/03/15
 */
@Singleton public class Storage {
  private final Context context;
  private final SqlOpenDbHelper openDbHelper;
  private final DatabaseCompartment dbCompartment;

  @Inject public Storage(Context context) {
    this.context = context;
    this.openDbHelper = new SqlOpenDbHelper(context);
    this.dbCompartment = cupboard().withDatabase(openDbHelper.getWritableDatabase());
  }

  public long saveArticle(Article article) {
    return dbCompartment.put(article);
  }

  public long saveFeed(Feed feed) {
    return dbCompartment.put(feed);
  }

  public List<Feed> getFeeds() {
    return dbCompartment.query(Feed.class).list();
  }

  public List<Article> getArticlesByFeed(long feedId) {
    return dbCompartment.query(Article.class)
        .withSelection("feedId = ?", String.valueOf(feedId))
        .list();
  }

  public List<Article> getArticlesByQuery(String query) {
    return dbCompartment.query(Article.class)
        .withSelection("title like ? or content like ?", String.format(Locale.US, "%%%s%%", query))
        .list();
  }
}
