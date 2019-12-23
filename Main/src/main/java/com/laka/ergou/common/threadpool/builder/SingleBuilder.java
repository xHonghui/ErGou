/**
 * Title: SingleBuilder.java
 * Description: 
 * Copyright: Copyright (c) 2013-2015 luoxudong.com
 * Company: 个人
 * Author: 罗旭东 (hi@luoxudong.com)
 * Date: 2017年4月20日 下午3:37:16
 * Version: 1.0
 */
package com.laka.ergou.common.threadpool.builder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.laka.ergou.common.threadpool.ThreadPoolType;

/**
 * @Author:summer
 * @Date:2019/3/6
 * @Description:创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
 */
public class SingleBuilder extends ThreadPoolBuilder<ExecutorService> {
	@Override
	protected ExecutorService create() {
		return Executors.newSingleThreadExecutor();
	}
	
	@Override
	protected ThreadPoolType getType() {
		return ThreadPoolType.SINGLE;
	}
}
