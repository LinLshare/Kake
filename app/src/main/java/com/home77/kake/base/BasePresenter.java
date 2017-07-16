package com.home77.kake.base;

/**
 * receive all events from views,
 * and then do some logical things,
 * may call views' method for interaction.
 *
 * @author CJ
 */
public abstract class BasePresenter<T> {
  protected T contentView;

  public BasePresenter(T contentView) {
    this.contentView = contentView;
  }

  public abstract void start();
}
