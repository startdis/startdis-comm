package com.startdis.comm.log.aspect;


import com.alibaba.fastjson.JSON;
import com.startdis.comm.core.spring.SpringProperties;
import com.startdis.comm.exception.custom.BusinessException;
import com.startdis.comm.exception.util.ExceptionUtils;
import com.startdis.comm.log.annotation.LogRecord;
import com.startdis.comm.log.enums.BusinessType;
import com.startdis.comm.log.model.LogRecordDTO;
import com.startdis.comm.log.service.LogRecordService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author DianJiu
 * @email dianjiuxyz@gmail.com
 * @date 2022-07-19
 * @desc
 */
@Aspect
@Component
@Order(10)
public class LogRecordAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogRecordAspect.class);

    private final ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<Map<String, Object>>();

    @Resource
    private LogRecordService logRecordService;

    @Pointcut("@annotation(com.startdis.comm.log.annotation.LogRecord)")
    public void logRecordPointCut() {
    }

    /**
     * 环绕通知
     * 方式一：传入注解，获取注解中的参数
     *
     * @Around("logRecordPointCut()&&@annotation(logRecord)") public void doAround(ProceedingJoinPoint joinPoint, LogRecord logRecord) {}
     * 方式二：通过切点获取注解实体
     */
    @Around("logRecordPointCut()")
    //@Around("logRecordPointCut()&&@annotation(logRecord)")
    //public void doAround(ProceedingJoinPoint joinPoint, LogRecord logRecord) {
    public Object doAround(ProceedingJoinPoint joinPoint) {
        // 接收到请求
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        // 记录请求内容，threadInfo存储所有内容
        Map<String, Object> threadInfo = new HashMap<>();
        //该方法用于获取客户端发出请求时的完整URL，包括协议、服务器名、端口号、资源路径等信息，但不包括后面的查询参数部分。
        // 注意，getRequestRUL()方法返回的时StringBuffer类型，而不是String类型。
        threadInfo.put("url", request.getRequestURL());
        //该方法用于获取请求行中资源名称部分，即位于URL的主机和端口之后、参数部分之前的部分
        threadInfo.put("uri", request.getRequestURI());
        //该方法用于获取HTTP请求消息中的请求方式（如GET、POST等）
        threadInfo.put("httpMethod", request.getMethod());
        //该方法用于获取请求的协议名，例如http、https或ftp
        threadInfo.put("scheme", request.getScheme());
        //该方法用于获取请求客户端的IP地址，其格式类似于"192.168.0.1"
        threadInfo.put("ip", request.getRemoteAddr());
        //该方法用于获取Content-Type头字段的值，结果为String类型
        threadInfo.put("contentType", request.getContentType());
        //包含了一个特征字符串，用来让网络协议的对端来识别发起请求的用户代理软件的应用类型、操作系统、软件开发商以及版本号。
        threadInfo.put("userAgent", request.getHeader("User-Agent"));
        //从请求头获取业务流水号
        threadInfo.put("groupTenantId", request.getHeader("X-Group-Tenant-Id"));
        threadLocal.set(threadInfo);
        //从请求头获取业务流水号
        threadInfo.put("companyTenantId", request.getHeader("X-Company-Tenant-Id"));
        threadLocal.set(threadInfo);
        //从请求头获取业务流水号
        threadInfo.put("operator", request.getHeader("X-Unique-Id"));
        threadLocal.set(threadInfo);
        //从请求头获取业务流水号
        threadInfo.put("businessNo", request.getHeader("X-Business-No"));
        threadLocal.set(threadInfo);
        //获取方法签名
        MethodSignature signature = getSignature(joinPoint);
        //获取注解方法
        Method method = getMethod(joinPoint);
        //获取注解对象
        LogRecord logRecord = getAnnotation(joinPoint, LogRecord.class);
        // 获取连接点所在类名
        String classname = joinPoint.getTarget().getClass().getSimpleName();
        // 获取连接点所在方法名称
        String methodName = method.getName();
        // 下面两个数组中，参数值和参数名的个数和位置是一一对应的。
        // 参数名
        String[] argNames = signature.getParameterNames();
        // 参数值
        Object[] args = joinPoint.getArgs();
        StringBuilder sb = new StringBuilder();
        //不为空时便利组装
        if (argNames.length > 0 && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                sb.append(argNames[i] + ":" + args[i] + ";");
            }
        }
        //String name = String.format("【%s】【%s】", signature.getDeclaringType().toString(), signature.toLongString());
        String name = String.format("【%s.%s()】", classname, methodName);

        if (logRecord.logParameters()) {
            // logger.info(String.format("【入参日志】调用 %s 的参数是：【%s】", name, JSONArray.toJSONString(args)));
            // logger.info(String.format("【入参日志】调用 %s 的参数是：【%s】", name, JSON.toJSONString(args)));
            logger.info(String.format("【入参日志】调用 %s 方法的参数是：【%s】", name, sb.toString()));
        }
        //执行到这里开始走进来的方法体（必须声明）
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = null;
        Long executeTime = 0L;
        String errorInfo = "";
        //实现连接点方法的执行，以及成功失败的打点，出现异常的时候还会记录日志
        Object returnValue = null;
        try {
            returnValue = joinPoint.proceed(args);
            ////记录方法的执行时间
            if (logRecord.recordSuccessMetrics()) {
                end = LocalDateTime.now();
                executeTime = Duration.between(start, end).toMillis();
                //在生产级代码中，我们应考虑使用类似Micrometer的指标框架，把打点信息记录到时间序列数据库中，实现通过图表来查看方法的调用次数和执行时间，在设计篇我们会重点介绍
                logger.info(String.format("【成功打点】调用 %s 方法成功，耗时：%d ms", name, executeTime));
            }
        } catch (Throwable throwable) {
            //记录方法的执行时间
            if (logRecord.recordFailMetrics()) {
                end = LocalDateTime.now();
                executeTime = Duration.between(start, end).toMillis();
                logger.info(String.format("【失败打点】调用 %s 方法失败，耗时：%d ms", name, executeTime));
            }
            //记录异常日志
            if (logRecord.logException()) {
                errorInfo = ExceptionUtils.getTrace(throwable);
                logger.error(String.format("【异常日志】调用 %s 方法出现异常！", name), errorInfo);
            }
            //出现异常后忽略异常返回默认值
            //if (logRecord.ignoreException()) {
            //    returnValue = getDefaultValue(signature.getReturnType());
            //    throw new SystemException("500", "系统异常");
            //}

            // 打印异常堆栈信息
            //throwable.printStackTrace();
            recordLogs(logRecord, args, start, end, executeTime, errorInfo);
            throw new BusinessException(((BusinessException) throwable).getCode(), ((BusinessException) throwable).getMessage());
        }

        //recordLogs(logRecord, args, start, end, executeTime);

        //开启返回值记录并保存到数据库
        if (logRecord.logReturn()) {
            logger.info(String.format("【出参日志】调用 %s 方法的返回是：【%s】", name, returnValue));
            recordLogs(logRecord, args, returnValue, start, end, executeTime);
        }

        return returnValue;
    }

    /**
     * 操作日志保存-保存异常信息
     *
     * @param logRecord
     * @param args
     * @param start
     * @param end
     * @param executeTime
     */
    private void recordLogs(LogRecord logRecord, Object[] args, LocalDateTime start, LocalDateTime end, Long executeTime, String errorInfo) {
        if (logRecord.condition()) {
            Map<String, Object> objectMap = threadLocal.get();
            LogRecordDTO logRecordDTO = new LogRecordDTO();
            logRecordDTO.setGroupTenantId(String.valueOf(objectMap.get("groupTenantId")));
            logRecordDTO.setCompanyTenantId(String.valueOf(objectMap.get("companyTenantId")));
            logRecordDTO.setOperator(Objects.nonNull(objectMap.get("operator")) ? String.valueOf(objectMap.get("operator")) : null);
            logRecordDTO.setModule(SpringProperties.getString("log.record.appName"));
            logRecordDTO.setBusinessNo(Objects.nonNull(objectMap.get("businessNo")) ? String.valueOf(objectMap.get("businessNo")) : null);
            logRecordDTO.setBusinessType(BusinessType.getMsg(logRecord.businessType()));
            logRecordDTO.setLogContent(logRecord.content());
            logRecordDTO.setLogExtras(logRecord.extras());
            logRecordDTO.setRequestUrl(String.valueOf(objectMap.get("url")));
            logRecordDTO.setRequestIp(String.valueOf(objectMap.get("ip")));
            logRecordDTO.setRequestType(String.valueOf(objectMap.get("httpMethod")));
            logRecordDTO.setContentType(Objects.nonNull(objectMap.get("contentType")) ? String.valueOf(objectMap.get("contentType")) : null);
            logRecordDTO.setRequestBody(JSON.toJSONString(args));
            logRecordDTO.setRequestTime(start);
            logRecordDTO.setResponseTime(end);
            logRecordDTO.setExecuteTime(executeTime);
            //logRecordDTO.setTraceId();
            logRecordDTO.setErrorInfo(errorInfo);
            logRecordDTO.setUserAgent(String.valueOf(objectMap.get("userAgent")));
            logRecordService.record(logRecordDTO);
        }
    }

    /**
     * 操作日志保存-保存成功信息-不保存返回值
     *
     * @param logRecord
     * @param args
     * @param start
     * @param end
     * @param executeTime
     */
    private void recordLogs(LogRecord logRecord, Object[] args, LocalDateTime start, LocalDateTime end, Long executeTime) {
        if (logRecord.condition()) {
            Map<String, Object> objectMap = threadLocal.get();
            LogRecordDTO logRecordDTO = new LogRecordDTO();
            logRecordDTO.setGroupTenantId(String.valueOf(objectMap.get("groupTenantId")));
            logRecordDTO.setCompanyTenantId(String.valueOf(objectMap.get("companyTenantId")));
            logRecordDTO.setOperator(Objects.nonNull(objectMap.get("operator")) ? String.valueOf(objectMap.get("operator")) : null);
            logRecordDTO.setModule(SpringProperties.getString("log.record.appName"));
            logRecordDTO.setBusinessNo(Objects.nonNull(objectMap.get("businessNo")) ? String.valueOf(objectMap.get("businessNo")) : null);
            logRecordDTO.setBusinessType(BusinessType.getMsg(logRecord.businessType()));
            logRecordDTO.setLogContent(logRecord.content());
            logRecordDTO.setLogExtras(logRecord.extras());
            logRecordDTO.setRequestUrl(String.valueOf(objectMap.get("url")));
            logRecordDTO.setRequestIp(String.valueOf(objectMap.get("ip")));
            logRecordDTO.setRequestType(String.valueOf(objectMap.get("httpMethod")));
            logRecordDTO.setContentType(Objects.nonNull(objectMap.get("contentType")) ? String.valueOf(objectMap.get("contentType")) : null);
            logRecordDTO.setRequestBody(JSON.toJSONString(args));
            logRecordDTO.setRequestTime(start);
            logRecordDTO.setResponseTime(end);
            logRecordDTO.setExecuteTime(executeTime);
            //logRecordDTO.setTraceId();
            logRecordDTO.setUserAgent(String.valueOf(objectMap.get("userAgent")));
            logRecordService.record(logRecordDTO);
        }
    }

    /**
     * 操作日志保存-保存成功信息-并保存返回值
     *
     * @param logRecord
     * @param args
     * @param returnValue
     * @param start
     * @param end
     * @param executeTime
     */
    private void recordLogs(LogRecord logRecord, Object[] args, Object returnValue, LocalDateTime start, LocalDateTime end, Long executeTime) {
        if (logRecord.condition()) {
            Map<String, Object> objectMap = threadLocal.get();
            LogRecordDTO logRecordDTO = new LogRecordDTO();
            logRecordDTO.setGroupTenantId(String.valueOf(objectMap.get("groupTenantId")));
            logRecordDTO.setCompanyTenantId(String.valueOf(objectMap.get("companyTenantId")));
            logRecordDTO.setOperator(Objects.nonNull(objectMap.get("operator")) ? String.valueOf(objectMap.get("operator")) : null);
            logRecordDTO.setModule(SpringProperties.getString("log.record.appName"));
            logRecordDTO.setBusinessNo(Objects.nonNull(objectMap.get("businessNo")) ? String.valueOf(objectMap.get("businessNo")) : null);
            logRecordDTO.setBusinessType(BusinessType.getMsg(logRecord.businessType()));
            logRecordDTO.setLogContent(logRecord.content());
            logRecordDTO.setLogExtras(logRecord.extras());
            logRecordDTO.setRequestUrl(String.valueOf(objectMap.get("url")));
            logRecordDTO.setRequestIp(String.valueOf(objectMap.get("ip")));
            logRecordDTO.setRequestType(String.valueOf(objectMap.get("httpMethod")));
            logRecordDTO.setContentType(Objects.nonNull(objectMap.get("contentType")) ? String.valueOf(objectMap.get("contentType")) : null);
            logRecordDTO.setRequestBody(JSON.toJSONString(args));
            logRecordDTO.setResponseBody(JSON.toJSONString(returnValue));
            logRecordDTO.setRequestTime(start);
            logRecordDTO.setResponseTime(end);
            logRecordDTO.setExecuteTime(executeTime);
            //logRecordDTO.setTraceId();
            logRecordDTO.setUserAgent(String.valueOf(objectMap.get("userAgent")));
            logRecordService.record(logRecordDTO);
        }
    }


    private MethodSignature getSignature(JoinPoint joinPoint) {
        //通过连接点获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature;
    }

    private Method getMethod(JoinPoint joinPoint) {
        //通过连接点获取方法签名
        MethodSignature signature = getSignature(joinPoint);
        //获取注解服务的方法
        Method method = signature.getMethod();
        return method;
    }

    private <T extends Annotation> T getAnnotation(JoinPoint joinPoint, Class<T> annotationClass) {
        Method method = getMethod(joinPoint);
        //获取注解类
        T annotation = method.getAnnotation(annotationClass);
        return annotation;
    }

}
