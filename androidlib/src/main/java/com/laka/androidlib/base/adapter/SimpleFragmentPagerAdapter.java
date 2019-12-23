package com.laka.androidlib.base.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import com.laka.androidlib.util.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:Rayman
 * @Date:2018/12/24
 * @Description:简单封装FragmentPagerAdapter
 */

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList = new ArrayList();
    private List<String> titleList = new ArrayList();
    private FragmentManager fragmentManager;

    public SimpleFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentLis) {
        super(fm);
        this.fragmentManager = fm;
        this.fragmentList.clear();
        this.fragmentList.addAll(fragmentLis);
    }

    public SimpleFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titleList) {
        this(fm, fragmentList);
        this.titleList.clear();
        this.titleList.addAll(titleList);
    }

    public void setFragments(ArrayList<Fragment> fragments, List<String> titleList) {
        if (this.fragmentList != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            for (int i = 0; i < fragmentList.size(); i++) {
                ft.remove(fragmentList.get(i));
            }
            ft.commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        this.fragmentList.clear();
        this.fragmentList.addAll(fragments);
        this.titleList.clear();
        this.titleList.addAll(titleList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);

    }

    @Override
    public int getCount() {
        if (ListUtils.isEmpty(fragmentList)) {
            return 0;
        }
        return fragmentList == null ? 0 : fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (ListUtils.isNotEmpty(titleList) &&
                position >= 0 && position < titleList.size()) {
            return titleList.get(position);
        }
        return super.getPageTitle(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        this.fragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss();
        return fragment;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (position >= 0 && position < fragmentList.size()) {
            Fragment fragment = fragmentList.get(position);
            this.fragmentManager.beginTransaction().hide(fragment).commitAllowingStateLoss();
        }
    }

}
