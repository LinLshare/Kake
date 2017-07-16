package com.home77.kake.base;

/**
 * just post all events to presenter,
 * and wait presenter to handle.
 *
 * @author CJ
 */
public interface BaseView<T> {
  void setPresenter(T presenter);

  void toast(String content);

  void toast(int contentId);
}
