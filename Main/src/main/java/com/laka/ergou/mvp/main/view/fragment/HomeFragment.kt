package com.laka.ergou.mvp.main.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import com.laka.androidlib.base.fragment.BaseLazyLoadFragment
import com.laka.ergou.mvp.advert.fragment.AdvertFragment
import com.laka.ergou.mvp.circle.view.fragment.CircleFragment
import com.laka.ergou.mvp.constant.MainConstant
import com.laka.ergou.mvp.constant.MainConstant.HOMEPAGE_FRAGMENT_INDEX
import com.laka.ergou.mvp.shopping.center.view.fragment.ShoppingHomeFragment
import com.laka.ergou.mvp.user.view.fragment.UserHomeFragment

/**
 * @Author:Rayman
 * @Date:2018/12/21
 * @Description:主页类型Fragment，统一使用懒加载的方式去加载数据 .
 * 主页的Fragment都需继承它。
 */
abstract class HomeFragment : BaseLazyLoadFragment() {

    /**
     * description:定义静态内存块
     **/
    companion object {

        /**
         * description:新建Fragment
         **/
        fun newInstance(@MainConstant.HomePageFragmentType fragmentType: Int): Fragment {

            var bundle = Bundle()
            bundle.putInt(HOMEPAGE_FRAGMENT_INDEX, fragmentType)

            when (fragmentType) {
                MainConstant.HOMEPAGE_SHOPPING -> return ShoppingHomeFragment()
                MainConstant.HOMEPAGE_MINE -> return UserHomeFragment()
                MainConstant.HOMEPAGE_CIRCLE -> return CircleFragment()
                MainConstant.HOMEPAGE_ADVERT -> return AdvertFragment()
            }

            // 默认返回ShoppingHomeFragment()----
            return ShoppingHomeFragment()
        }
    }
}