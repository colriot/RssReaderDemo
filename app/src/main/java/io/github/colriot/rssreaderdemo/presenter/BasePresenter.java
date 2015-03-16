package io.github.colriot.rssreaderdemo.presenter;

import io.github.colriot.rssreaderdemo.view.BaseView;

/**
 * @author Sergey Ryabov <colriot@gmail.com>
 *         15/03/15
 */
public interface BasePresenter<T extends BaseView> {
  void takeView(T view);
  void dropView(T view);
}
