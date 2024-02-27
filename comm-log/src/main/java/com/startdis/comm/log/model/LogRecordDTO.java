package com.startdis.comm.log.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统日志表
 * </p>
 *
 * @author 点九
 * @since 2022-07-20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@ApiModel(description = "操作日志")
public class LogRecordDTO implements Serializable {

    private static final long serialVersionUID = -85509022761738038L;

    /**
     * 集团租户ID
     */
    @ApiModelProperty("集团租户ID")
    private String groupTenantId;

    /**
     * 公司租户ID
     */
    @ApiModelProperty("公司租户ID")
    private String companyTenantId;

    /**
     * 操作人ID
     */
    @ApiModelProperty("操作人ID")
    private String operator;

    /**
     * 业务模块，区分不同业务模块的应用编码
     */
    @ApiModelProperty("业务模块")
    private String module;

    /**
     * 业务流水号
     */
    @ApiModelProperty("业务流水号")
    private String businessNo;

    /**
     * 业务类型
     */
    @ApiModelProperty("业务类型（新增、修改、删除、查询、导入、导出、其他）")
    private String businessType;

    /**
     * 日志模板内容
     */
    @ApiModelProperty("日志模板内容")
    private String logContent;

    /**
     * 额外的日志内容
     */
    @ApiModelProperty("额外的内容")
    private String logExtras;

    /**
     * 请求地址
     */
    @ApiModelProperty("请求地址")
    private String requestUrl;

    /**
     * 请求IP
     */
    @ApiModelProperty("请求IP")
    private String requestIp;

    /**
     * 请求类型（POST、PUT、OPTIONS、DELETE）
     */
    @ApiModelProperty("请求类型（POST、PUT、OPTIONS、DELETE）")
    private String requestType;

    /**
     * 请求报文类型（map、json）
     */
    @ApiModelProperty("请求报文类型（map、json）")
    private String contentType;

    /**
     * 请求报文
     */
    @ApiModelProperty("请求报文")
    private String requestBody;

    /**
     * 响应报文
     */
    @ApiModelProperty("响应报文")
    private String responseBody;

    /**
     * 请求时间
     */
    @ApiModelProperty("请求时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime requestTime;

    /**
     * 响应时间
     */
    @ApiModelProperty("响应时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime responseTime;

    /**
     * 操作花费的时间 单位：ms
     */
    @ApiModelProperty("请求耗时")
    private Long executeTime;

    /**
     * 链路追踪ID
     */
    @ApiModelProperty("链路追踪ID")
    private String traceId;

    /**
     * 异常信息
     */
    @ApiModelProperty("异常信息")
    private String errorInfo;

    /**
     * 浏览器、操作系统、UI引擎标识
     */
    @ApiModelProperty("浏览器、操作系统、UI引擎标识")
    private String userAgent;

}
