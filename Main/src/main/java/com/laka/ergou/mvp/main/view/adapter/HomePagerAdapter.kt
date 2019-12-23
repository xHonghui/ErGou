package com.laka.ergou.mvp.main.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup
import com.laka.ergou.mvp.main.view.fragment.HomeFragment

/**
 * @Author:Rayman
 * @Date:2018/12/24
 * @Description:App主页类型Adapter
 */
class HomePagerAdapter(var fragmentManager: FragmentManager, var fragmentList: ArrayList<Fragment>) :
        FragmentPagerAdapter(fragmentManager) {


    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        this.fragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss()
        return fragment
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any) {
        val fragment = fragmentList[position]
        this.fragmentManager.beginTransaction().hide(fragment).commitAllowingStateLoss()
    }
}