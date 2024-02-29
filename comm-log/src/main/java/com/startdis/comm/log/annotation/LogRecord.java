package com.startdis.comm.log.annotation;

import com.startdis.comm.log.enums.BusinessType;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * @author DianJiu
 * @email dianjiuxyz@gmail.com
 * @date 2022-07-19
 * @desc 操作日志记录
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface LogRecord {

    /**
     * 集团租户ID
     */
    //String groupTenantId() default "";

    /**
     * 公司租户ID
     */
    //String companyTenantId() default "";


    /**
     * 操作人ID
     *
     * @return
     */
    //String operator() default "";

    /**
     * 业务模块，区分不同业务模块的应用编码
     *
     * @return
     */
    //String module() default "";

    /**
     * 业务流水号
     *
     * @return
     */
    //String businessNo() default "";

    /**
     * 日志操作类型
     */
    BusinessType businessType() default BusinessType.OTHER;

    /**
     * 日志模板内容
     *
     * @return
     */
    String content();

    /**
     * 额外的补充内容
     *
     * @return
     */
    String extras() default "";

    /**
     * 是否记录日志的条件
     *
     * @return
     */
    boolean condition() default true;

    /**
     * 在方法成功执行后打点，记录方法的执行时间发送到指标系统，默认开启
     *
     * @return
     */
    boolean recordSuccessMetrics() default true;

    /**
     * 在方法成功失败后打点，记录方法的执行时间发送到指标系统，默认开启
     *
     * @return
     */
    boolean recordFailMetrics() default true;

    /**
     * 通过日志记录请求参数，默认开启
     *
     * @return
     */
    boolean logParameters() default true;

    /**
     * 通过日志记录方法返回值，默认开启
     *
     * @return
     */
    boolean logReturn() default true;

    /**
     * 出现异常后通过日志记录异常信息，默认开启
     *
     * @return
     */
    boolean logException() default true;

    /**
     * 出现异常后忽略异常返回默认值，默认关闭
     *
     * @return
     */
    boolean ignoreException() default false;
}
