package com.home77.kake.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author CJ
 */
public interface FragmentLifeCycle {
  View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

  void onCreate(@Nullable Bundle savedInstanceState);

  void onActivityResult(int requestCode, int resultCode, Intent data);

  void onSaveInstanceState(Bundle outState);

  void onRequestPermissionsResult(int requestCode,
                                  @NonNull String[] permissions,
                                  @NonNull int[] grantResults);

  void onStart();

  void onPause();

  void onResume();

  void onStop();

  void onDestroyView();
}
