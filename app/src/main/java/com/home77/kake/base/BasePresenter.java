package com.home77.kake.base;

/**
 * receive all events from views,
 * and then do some logical things,
 * may call views' method for interaction.
 *
 * @author CJ
 */
public abstract class BasePresenter<T> {
  protected T attachedView;

  public BasePresenter(T attachedView) {
    this.attachedView = attachedView;
  }

  public abstract void start();

  public abstract void onCreateView();
}
