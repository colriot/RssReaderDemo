package io.github.colriot.rssreaderdemo.presenter;

import android.content.Context;
import android.text.TextUtils;
import io.github.colriot.rssreaderdemo.model.Article;
import io.github.colriot.rssreaderdemo.model.Feed;
import io.github.colriot.rssreaderdemo.model.RssModel;
import io.github.colriot.rssreaderdemo.view.ArticleActivity;
import io.github.colriot.rssreaderdemo.view.ArticleListView;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Sergey Ryabov <colriot@gmail.com>
 *         15/03/15
 */
public class ArticleListPresenterImpl implements ArticleListPresenter {
  private final RssModel model;
  private ArticleListView view;
  private Subscription subscription;

  @Inject public ArticleListPresenterImpl(RssModel model) {
    this.model = model;
  }

  @Override public void takeView(ArticleListView v) {
    view = v;
    final Feed feed = view.getFeed();
    final String query = view.getQuery();

    Observable<List<Article>> request = feed != null
        ? model.getArticles(feed)
        : model.getArticlesByQuery(TextUtils.isEmpty(query) ? "" : query);

    view.showLoading();
    subscription = request.observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(new Observer<List<Article>>() {
          @Override public void onCompleted() {
            view.hideLoading();
          }

          @Override public void onError(Throwable e) {
            view.hideLoading();
          }

          @Override public void onNext(List<Article> articles) {
            view.renderArticleList(articles);
          }
        });
  }

  @Override public void dropView(ArticleListView v) {
    if (subscription != null) {
      subscription.unsubscribe();
      subscription = null;
    }
    view = null;
  }

  @Override public void articleClicked(Article item) {
    final Context context = view.getContext();
    context.startActivity(ArticleActivity.intent(context, item));
  }
}
