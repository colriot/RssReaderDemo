package io.github.colriot.rssreaderdemo.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.InjectView;
import com.squareup.picasso.Picasso;
import io.github.colriot.rssreaderdemo.R;
import io.github.colriot.rssreaderdemo.model.Article;

/**
 * @author Sergey Ryabov <colriot@gmail.com>
 *         15/03/15
 */
public class ArticleActivity extends BaseActivity implements ArticleView {
  private static final String EXTRA_ARTICLE = "EXTRA_ARTICLE";

  @InjectView(R.id.image) ImageView imageView;
  @InjectView(R.id.title) TextView titleView;
  @InjectView(R.id.body) TextView bodyView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_article);

    final Article article = (Article) getIntent().getSerializableExtra(EXTRA_ARTICLE);

    Picasso.with(this)
        .load(article.getImgUrl())
        .centerCrop()
        .fit()
        .placeholder(R.mipmap.ic_launcher)
        .into(imageView);
    titleView.setText(article.getTitle());
    bodyView.setText(Html.fromHtml(article.getContent()));
  }

  @Override public Context getContext() {
    return null;
  }

  public static Intent intent(Context context, Article item) {
    return new Intent(context, ArticleActivity.class).putExtra(EXTRA_ARTICLE, item);
  }
}
