package com.startdis.comm.log.service;

import com.startdis.comm.log.model.LogRecordDTO;

/**
 * <p>
 * 系统日志表 服务类
 * </p>
 *
 * @author 点九
 * @since 2022-07-19
 */
public interface LogRecordService {
    /**
     * 把解析完成后的日志内容实体信息发送给我们的使用者，
     * 无论他接收到日志存储在数据库,ES,MQ还是哪里，让使用者来决定。
     *
     * @param logRecord 日志实体
     */
    void record(LogRecordDTO logRecord);

}
