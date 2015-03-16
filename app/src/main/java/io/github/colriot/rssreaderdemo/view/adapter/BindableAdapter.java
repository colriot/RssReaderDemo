package io.github.colriot.rssreaderdemo.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergey Ryabov <colriot@gmail.com>
 *         14/03/15
 */
public abstract class BindableAdapter<T> extends BaseAdapter {
  private final Context context;
  private final LayoutInflater inflater;

  private List<T> items = new ArrayList<>();

  protected BindableAdapter(Context context) {
    this.context = context;
    this.inflater = LayoutInflater.from(context);
  }

  protected Context getContext() {
    return context;
  }

  public void setItems(List<T> items) {
    this.items = items;
    notifyDataSetChanged();
  }

  @Override public int getCount() {
    return items.size();
  }

  @Override public T getItem(int position) {
    return items.get(position);
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public View getView(int position, View convertView, @NonNull ViewGroup parent) {
    if (convertView == null) {
      convertView = newView(inflater, parent);
    }
    bindView(position, convertView, getItem(position));
    return convertView;
  }

  protected abstract void bindView(int position, View view, T item);

  protected abstract View newView(LayoutInflater inflater, ViewGroup parent);
}
