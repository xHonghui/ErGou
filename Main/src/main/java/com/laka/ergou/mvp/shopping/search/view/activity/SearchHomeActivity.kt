package com.laka.ergou.mvp.shopping.search.view.activity

import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import com.laka.androidlib.base.activity.BaseActivity
import com.laka.androidlib.data_structure.LimitQueue
import com.laka.androidlib.eventbus.Event
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.net.utils.parse.ParseUtil
import com.laka.androidlib.util.*
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.R
import com.laka.ergou.common.util.ClipBoardManagerHelper
import com.laka.ergou.mvp.main.constant.HomeConstant
import com.laka.ergou.mvp.main.constant.HomeEventConstant
import com.laka.ergou.mvp.main.constant.HomeEventConstant.EVENT_UPDATE_RESULT_SEARCH_BY_KEY_WORD
import com.laka.ergou.mvp.shopping.search.view.fragment.SearchHomeFragment
import com.laka.ergou.mvp.shopping.search.view.fragment.SearchResultFragment
import kotlinx.android.synthetic.main.activity_search.*
import java.util.*

/**
 * @Author:Rayman
 * @Date:2019/1/7
 * @Description:主页的商品搜索页面
 */
class SearchHomeActivity : BaseActivity(), View.OnClickListener {

    /**
     * description:Fragment配置
     **/
    private var mFragmentManger = supportFragmentManager
    private var mTransaction = supportFragmentManager.beginTransaction()
    private lateinit var mSearchHomeFragment: Fragment
    private lateinit var mSearchResultFragment: Fragment
    private lateinit var currentFragment: Fragment
    private val mStack = Stack<Fragment>()
    /**
     * description:数据配置
     **/
    private var keyWord = ""
    private var textWatcher = InputTextWatcher()
    val HomeFragmentIndex: Int = 0
    val ResultFragmentIndex: Int = 1
    override fun setContentView(): Int {
        return R.layout.activity_search
    }

    override fun initIntent() {
        keyWord = intent?.extras?.getString(HomeConstant.SEARCH_KEY_WORD) ?: ""
    }

    override fun setStatusBarColor(color: Int) {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.color_main), 0)
    }

    override fun initViews() {
        setClickView<TextView>(R.id.tv_search_cancel)
        initFragment()
    }

    private fun initFragment() {
        mTransaction = mFragmentManger.beginTransaction()
        mTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        mSearchHomeFragment = SearchHomeFragment.newInstance()
        mSearchResultFragment = SearchResultFragment.newInstance(keyWord)
        mTransaction.add(R.id.fl_search_container, mSearchResultFragment)
        mTransaction.add(R.id.fl_search_container, mSearchHomeFragment)
        mTransaction.commit()
        mStack.add(mSearchHomeFragment)
        mStack.add(mSearchResultFragment)
    }

    override fun initData() {
        if (!TextUtils.isEmpty(keyWord)) {
            et_search_keyword.setText(keyWord)
            et_search_keyword.setSelection(et_search_keyword.text.toString().length)
            val historyLocalData = SPHelper.getString(HomeConstant.SEARCH_HISTORY, "")
            LogUtils.debug("historyLocalData--->$historyLocalData")
            var historyLimitQueue: LimitQueue<String>? = null
            if (!TextUtils.isEmpty(historyLocalData)) {
                historyLimitQueue = ParseUtil.parseJson<LimitQueue<String>>(historyLocalData, LimitQueue::class.java)
                if (historyLimitQueue == null) {
                    historyLimitQueue = LimitQueue(5)
                }
            } else {
                historyLimitQueue = LimitQueue(5)
            }
            historyLimitQueue?.offerFirst(keyWord, false)
            val jsonStr = ParseUtil.toJson(historyLimitQueue)
            SPHelper.putString(HomeConstant.SEARCH_HISTORY, jsonStr)
            switchResultFragment(keyWord)
        } else {
            switchHomeFragment()
        }

    }

    override fun initEvent() {
        setClickView<ImageView>(R.id.iv_search_clear)
        et_search_keyword.addTextChangedListener(textWatcher)
        et_search_keyword.setOnEditorActionListener { v, actionId, event ->
            // 按下键盘搜索按钮，搜索数据
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                keyWord = et_search_keyword?.text?.toString()!!
                if (TextUtils.isEmpty(keyWord)) {
                    ToastHelper.showToast("请输入关键字")
                } else {
                    switchResultFragment(keyWord)
                }
            }
            false
        }
        et_search_keyword.setOnInputStateListener {
            //复制，剪切
            ClipBoardManagerHelper.getInstance.setLocalCopyContent(et_search_keyword.text.toString())
            LogUtils.info("输入框复制 -- 剪贴操作：${et_search_keyword.text}")
        }
    }

    override fun onEvent(event: Event?) {
        super.onEvent(event)
        when (event?.name) {
            HomeEventConstant.EVENT_SEARCH_KEY_WORD -> {
                // 当关键字为""的情况下，切换回搜索主页Fragment
                if (TextUtils.isEmpty(event.data as String)) {
                    switchHomeFragment()
                } else {
                    // 当关键字不为空，切换到搜索结果Fragment
                    keyWord = event?.data as String
                    switchResultFragment(keyWord)
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_search_cancel -> finish()
            R.id.iv_search_clear -> {
                switchHomeFragment()
                EventBusManager.postEvent(EVENT_UPDATE_RESULT_SEARCH_BY_KEY_WORD, "")
            }
        }
    }

    private fun switchFragment(position: Int) {
        val manager = mFragmentManger.beginTransaction()
        for (fragment in mStack) {
            manager.hide(fragment)
        }
        manager.show(mStack[position])
        manager.commit()
    }


    /**
     * 切换到搜索主页Fragment
     */
    private fun switchHomeFragment() {
        keyWord = ""
        iv_search_clear.visibility = View.GONE
        EventBusManager.postEvent(EVENT_UPDATE_RESULT_SEARCH_BY_KEY_WORD, keyWord)
        switchFragment(HomeFragmentIndex)

        et_search_keyword.setText(keyWord)
        et_search_keyword.isFocusable = true
        et_search_keyword.isFocusableInTouchMode = true
        Handler().postDelayed({
            et_search_keyword.requestFocus()
            KeyboardHelper.openKeyBoard(this, et_search_keyword)
        }, 100)
    }

    /**
     * 切换到搜索结果Fragment
     */
    private fun switchResultFragment(key: String) {
        // 因为第一次Fragment初始化的时候通过EventBus的方式发送是没有走生命周期的
        // 所以第一次发送事件到resultFragment是收不到的。
        if (!::mSearchResultFragment.isInitialized) {
            mSearchResultFragment = SearchResultFragment.newInstance(key)
        }
        if (!TextUtils.isEmpty(key)) {
            iv_search_clear.visibility = View.VISIBLE
        }
        switchFragment(ResultFragmentIndex)
        EventBusManager.postEvent(EVENT_UPDATE_RESULT_SEARCH_BY_KEY_WORD, key)
        et_search_keyword.setText(key)
        et_search_keyword.clearFocus()
        SoftKeyBoardUtil.hideInputMethod(et_search_keyword)
        cl_search.isFocusable = true
        cl_search.isFocusableInTouchMode = true
        cl_search.requestFocus()
        et_search_keyword.isFocusable = false
        et_search_keyword.isFocusableInTouchMode = false
    }

    override fun onDestroy() {
        super.onDestroy()
        et_search_keyword?.removeTextChangedListener(textWatcher)
    }

    inner class InputTextWatcher : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (!TextUtils.isEmpty(et_search_keyword?.text)) {
                if (iv_search_clear.visibility == View.GONE) {
                    iv_search_clear.visibility = View.VISIBLE
                }
            } else {
                iv_search_clear.visibility = View.GONE
                switchFragment(HomeFragmentIndex)
            }
        }
    }
}
