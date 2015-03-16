package io.github.colriot.rssreaderdemo.view.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.squareup.picasso.Picasso;
import io.github.colriot.rssreaderdemo.R;
import io.github.colriot.rssreaderdemo.model.Article;

/**
 * @author Sergey Ryabov <colriot@gmail.com>
 *         15/03/15
 */
public class ArticlesAdapter extends BindableAdapter<Article> {
  public ArticlesAdapter(Context context) {
    super(context);
  }

  @Override protected void bindView(int position, View view, Article item) {
    final ViewHolder holder = (ViewHolder) view.getTag();
    Picasso.with(getContext())
        .load(item.getImgUrl())
        .fit()
        .centerCrop()
        .placeholder(R.mipmap.ic_launcher)
        .into(holder.imageView);
    holder.titleView.setText(item.getTitle());
    final String content = item.getContent();
    holder.descriptionView.setText(Html.fromHtml(content.substring(0, Math.min(300, content.length()))));
  }

  @Override protected View newView(LayoutInflater inflater, ViewGroup parent) {
    final View view = inflater.inflate(R.layout.list_item_article, parent, false);
    view.setTag(new ViewHolder(view));
    return view;
  }

  static class ViewHolder {
    @InjectView(R.id.image) ImageView imageView;
    @InjectView(R.id.title) TextView titleView;
    @InjectView(R.id.description) TextView descriptionView;

    public ViewHolder(View view) {
      ButterKnife.inject(this, view);
    }
  }
}
