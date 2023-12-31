package com.startdis.comm.thread.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author DianJiu
 * @email lidianjiu@njydsz.com
 * @date 2022-07-20
 * @desc
 */
@Data
@Component
@ConfigurationProperties(prefix = "async.executor.thread", ignoreInvalidFields = true)
public class ThreadPoolConfiguration {
    /**
     * 线程池总数
     */
    private int corePoolSize;
    /**
     * 最大线程池数
     */
    private int maxPoolSize;
    /**
     * 队列大小
     */
    private int queueCapacity;
    /**
     * 缓冲队列中线程的空闲时间
     */
    private int keepAliveSeconds;

}
