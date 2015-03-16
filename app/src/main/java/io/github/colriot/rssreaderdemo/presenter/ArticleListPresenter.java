package io.github.colriot.rssreaderdemo.presenter;

import io.github.colriot.rssreaderdemo.model.Article;
import io.github.colriot.rssreaderdemo.view.ArticleListView;

/**
 * @author Sergey Ryabov <colriot@gmail.com>
 *         15/03/15
 */
public interface ArticleListPresenter extends BasePresenter<ArticleListView> {

  void articleClicked(Article item);
}
