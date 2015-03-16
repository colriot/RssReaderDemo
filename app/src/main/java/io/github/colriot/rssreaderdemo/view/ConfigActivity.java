package io.github.colriot.rssreaderdemo.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import io.github.colriot.rssreaderdemo.BuildConfig;
import io.github.colriot.rssreaderdemo.R;
import io.github.colriot.rssreaderdemo.model.Feed;
import io.github.colriot.rssreaderdemo.presenter.ConfigPresenter;
import io.github.colriot.rssreaderdemo.view.adapter.FeedsAdapter;
import java.util.List;
import javax.inject.Inject;

public class ConfigActivity extends BaseActivity implements ConfigView {

  @InjectView(R.id.add_rss_edit) EditText addRssEdit;
  @InjectView(R.id.add_rss_btn) Button addRssBtn;
  @InjectView(R.id.search_edit) EditText searchEdit;
  @InjectView(R.id.search_btn) Button searchBtn;

  @InjectView(R.id.progressContainer) View progressContainer;
  @InjectView(R.id.listContainer) View listContainer;
  @InjectView(android.R.id.list) ListView listView;
  @InjectView(android.R.id.empty) View emptyView;

  @Inject ConfigPresenter presenter;

  private FeedsAdapter adapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_config);
    adapter = new FeedsAdapter(this);
    listView.setAdapter(adapter);

    if (BuildConfig.DEBUG) {
      addRssEdit.setText("http://cyrilmottier.com/atom.xml");
    }
  }

  @Override public void onStart() {
    super.onStart();
    presenter.takeView(this);
  }

  @Override protected void onStop() {
    presenter.dropView(this);
    super.onStop();
  }

  @OnClick(R.id.add_rss_btn) void onAddRssClicked() {
    presenter.addFeedClicked(addRssEdit.getText().toString());
  }

  @OnClick(R.id.search_btn) void onSearchClicked() {
    presenter.searchClicked(searchEdit.getText().toString());
  }

  @OnItemClick(android.R.id.list) void onItemClick(AdapterView<?> parent, View view, int position,
      long id) {
    presenter.feedClicked(adapter.getItem(position));
  }

  @Override public void renderFeedList(List<Feed> feedList) {
    adapter.setItems(feedList);
    hideLoading();
  }

  @Override public void showLoading() {
    listContainer.setVisibility(View.GONE);
    progressContainer.setVisibility(View.VISIBLE);
    addRssEdit.setEnabled(false);
    addRssBtn.setEnabled(false);
    searchEdit.setEnabled(false);
    searchBtn.setEnabled(false);
  }

  @Override public void hideLoading() {
    progressContainer.setVisibility(View.GONE);
    listContainer.setVisibility(View.VISIBLE);
    final boolean isEmpty = adapter.isEmpty();
    listView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    emptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);

    addRssEdit.setEnabled(true);
    addRssBtn.setEnabled(true);
    searchEdit.setEnabled(true);
    searchBtn.setEnabled(true);
  }

  @Override public void clearAddText() {
    addRssEdit.setText("");
  }

  @Override public void showError(@StringRes int msgId) {
    Toast.makeText(this, msgId, Toast.LENGTH_SHORT).show();
  }

  @Override public Context getContext() {
    return this;
  }

  public static Intent intent(Context context) {
    return new Intent(context, ConfigActivity.class);
  }
}
