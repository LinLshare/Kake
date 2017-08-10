package com.home77.kake.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author CJ
 */
public interface FragmentLifeCycle {
  View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

  void onStart();

  void onPause();

  void onResume();

  void onStop();

  void onDestroyView();
}
