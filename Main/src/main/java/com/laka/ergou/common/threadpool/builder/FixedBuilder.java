/**
 * Title: FixedBuilder.java
 * Description: 
 * Copyright: Copyright (c) 2013-2015 luoxudong.com
 * Company: 个人
 * Author: 罗旭东 (hi@luoxudong.com)
 * Date: 2017年4月20日 下午3:36:36
 * Version: 1.0
 */
package com.laka.ergou.common.threadpool.builder;

import com.laka.ergou.common.threadpool.ThreadPoolType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @Author:summer
 * @Date:2019/3/6
 * @Description:创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
 */
public class FixedBuilder extends ThreadPoolBuilder<ExecutorService> {
	/** 固定线程池  */
	private int mSize = 1;
	
	@Override
	protected ExecutorService create() {
		return Executors.newFixedThreadPool(mSize);
	}

	@Override
	protected ThreadPoolType getType() {
		return ThreadPoolType.FIXED;
	}
	
	public FixedBuilder setSize(int size) {
		mSize = size;
		return this;
	}
}
