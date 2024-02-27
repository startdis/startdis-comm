package com.startdis.comm.log.service.impl;

import com.alibaba.fastjson2.JSON;
import com.startdis.comm.core.spring.SpringProperties;
import com.startdis.comm.log.model.LogRecordDTO;
import com.startdis.comm.log.service.LogRecordService;
import com.startdis.comm.util.http.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统日志表 服务实现类
 * </p>
 *
 * @author 点九
 * @since 2022-07-19
 */
@Service
@Slf4j
public class LogRecordServiceImpl implements LogRecordService {

    /**
     * 把解析完成后的日志内容实体信息发送给我们的使用者，
     * 无论他接收到日志存储在数据库,ES,MQ还是哪里，让使用者来决定。
     *
     * @param logRecord 日志实体
     */
    @Override
    //@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void record(LogRecordDTO logRecord) {
        String pushUrl = SpringProperties.getString("log.record.pushUrl");
        HttpClientUtils.doPostJson(pushUrl, JSON.toJSONString(logRecord));
        log.info("【logRecord】logInfo={}", JSON.toJSONString(logRecord));
    }
}
