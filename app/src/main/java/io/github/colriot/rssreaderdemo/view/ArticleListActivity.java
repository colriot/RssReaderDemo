package io.github.colriot.rssreaderdemo.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import butterknife.InjectView;
import butterknife.OnItemClick;
import io.github.colriot.rssreaderdemo.R;
import io.github.colriot.rssreaderdemo.model.Article;
import io.github.colriot.rssreaderdemo.model.Feed;
import io.github.colriot.rssreaderdemo.presenter.ArticleListPresenter;
import io.github.colriot.rssreaderdemo.view.adapter.ArticlesAdapter;
import java.util.List;
import javax.inject.Inject;

/**
 * @author Sergey Ryabov <colriot@gmail.com>
 *         15/03/15
 */
public class ArticleListActivity extends BaseActivity implements ArticleListView {
  private static final String EXTRA_FEED = "EXTRA_FEED";
  private static final String EXTRA_QUERY = "EXTRA_QUERY";

  @InjectView(R.id.progressContainer) View progressContainer;
  @InjectView(R.id.listContainer) View listContainer;
  @InjectView(android.R.id.list) ListView listView;
  @InjectView(android.R.id.empty) View emptyView;

  @Inject ArticleListPresenter presenter;

  private ArticlesAdapter adapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_articles_list);

    adapter = new ArticlesAdapter(this);
    listView.setAdapter(adapter);
  }

  @Override public void onStart() {
    super.onStart();
    presenter.takeView(this);
  }

  @Override protected void onStop() {
    presenter.dropView(this);
    super.onStop();
  }

  @OnItemClick(android.R.id.list) void onItemClick(AdapterView<?> parent, View view, int position,
      long id) {
    presenter.articleClicked(adapter.getItem(position));
  }

  @Override public void renderArticleList(List<Article> articleList) {
    adapter.setItems(articleList);
    hideLoading();
  }

  @Override public Feed getFeed() {
    return (Feed) getIntent().getSerializableExtra(EXTRA_FEED);
  }
  @Override public String getQuery() {
    return getIntent().getStringExtra(EXTRA_QUERY);
  }

  @Override public void showLoading() {
    listContainer.setVisibility(View.GONE);
    progressContainer.setVisibility(View.VISIBLE);
  }

  @Override public void hideLoading() {
    progressContainer.setVisibility(View.GONE);
    listContainer.setVisibility(View.VISIBLE);
    final boolean isEmpty = adapter.isEmpty();
    listView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    emptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
  }

  @Override public Context getContext() {
    return this;
  }

  public static Intent intent(Context context, Feed feed) {
    return new Intent(context, ArticleListActivity.class).putExtra(EXTRA_FEED, feed);
  }
  public static Intent intent(Context context, String query) {
    return new Intent(context, ArticleListActivity.class).putExtra(EXTRA_QUERY, query);
  }
}
