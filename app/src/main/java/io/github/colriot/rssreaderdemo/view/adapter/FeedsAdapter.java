package io.github.colriot.rssreaderdemo.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import io.github.colriot.rssreaderdemo.model.Feed;

/**
 * @author Sergey Ryabov <colriot@gmail.com>
 *         14/03/15
 */
public class FeedsAdapter extends BindableAdapter<Feed> {
  public FeedsAdapter(Context context) {
    super(context);
  }

  @Override protected void bindView(int position, View view, Feed item) {
    ((TextView) view).setText(item.getTitle());
  }

  @Override protected View newView(LayoutInflater inflater, ViewGroup parent) {
    return inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
  }
}
