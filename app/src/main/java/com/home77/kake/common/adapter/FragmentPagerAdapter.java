package com.home77.kake.common.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

  private ArrayList<Fragment> fragmentList;
  private List<String> titleList;

  public FragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
    super(fm);
    this.fragmentList = fragments;
  }

  public FragmentPagerAdapter(FragmentManager fm, Fragment[] fragments) {
    super(fm);
    fragmentList = new ArrayList<>();
    for (int i = 0; i < fragments.length; i++) {
      fragmentList.add(fragments[i]);
    }
  }

  public void setPageTitleList(List<String> titleList) {
    this.titleList = titleList;
  }

  public void setPageTitleList(String... pageTitle) {
    titleList = Arrays.asList(pageTitle);
  }

  @Override
  public Fragment getItem(int position) {
    return fragmentList.get(position);
  }

  @Override
  public int getCount() {
    return fragmentList != null ? fragmentList.size() : 0;
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    // super.destroyItem(container, position, object);
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    Fragment fragment = getItem(position);
    if (fragment.isAdded()) {
      return fragment;
    }
    return super.instantiateItem(container, position);
  }

  @Override
  public CharSequence getPageTitle(int position) {
    if (titleList != null && !TextUtils.isEmpty(titleList.get(position))) {
      return titleList.get(position);
    }
    return super.getPageTitle(position);
  }
}