/**
 * Title: ThreadTaskObject.java
 * Description: 
 * Copyright: Copyright (c) 2013-2015 luoxudong.com
 * Company: 个人
 * Author: 罗旭东 (hi@luoxudong.com)
 * Date: 2017年4月20日 下午7:05:58
 * Version: 1.0
 */
package com.laka.ergou.common.threadpool;

import java.util.concurrent.ExecutorService;

/**
 * @Author:summer
 * @Date:2019/3/6
 * @Description:
 */
public abstract class ThreadTaskObject implements Runnable {
	private ExecutorService mExecutorService = null;
	
	private String mPoolName = null;
	
	public ThreadTaskObject() {
		init();
    }

	public ThreadTaskObject(String poolName) {
		mPoolName = poolName;
		init();
    }
	
	private void init() {
		mExecutorService = ThreadPoolHelp.Builder.cached().name(mPoolName).builder();
	}
	
	public void start() {
		mExecutorService.execute(this);
	}
}
