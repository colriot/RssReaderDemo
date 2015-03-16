package io.github.colriot.rssreaderdemo.view;

import io.github.colriot.rssreaderdemo.model.Article;
import io.github.colriot.rssreaderdemo.model.Feed;
import java.util.List;

/**
 * @author Sergey Ryabov <colriot@gmail.com>
 *         15/03/15
 */
public interface ArticleListView extends BaseView {

  void renderArticleList(List<Article> articleList);

  Feed getFeed();
  String getQuery();

  void showLoading();
  void hideLoading();
}
