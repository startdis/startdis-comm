package com.startdis.comm.util.agent;


import com.blueconic.browscap.BrowsCapField;
import com.blueconic.browscap.Capabilities;
import com.blueconic.browscap.UserAgentParser;
import com.blueconic.browscap.UserAgentService;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Startdis
 * @email startdis@dianjiu.cc
 * @desc BrowserUtil 浏览器工具类
 */
public class BrowserUtils {

    /**
     * 获取远程客户端系统名称
     *
     * @param request
     * @return
     */
    public static String getOsName(HttpServletRequest request) {
        Capabilities capabilities = getCapabilities(request);
        return capabilities.getPlatform();
    }

    /**
     * 获取远程客户端系统版本
     *
     * @param request
     * @return
     */
    public static String getOsVersion(HttpServletRequest request) {
        Capabilities capabilities = getCapabilities(request);
        return capabilities.getPlatformVersion();
    }

    /**
     * 获取远程客户端系统信息
     *
     * @param request
     * @return
     */
    public static String getOsInfo(HttpServletRequest request) {
        Capabilities capabilities = getCapabilities(request);
        return capabilities.getPlatform() + "/" + capabilities.getPlatformVersion();
    }

    /**
     * 获取远程客户端浏览器名称
     *
     * @param request
     * @return
     */
    public static String getBrowserName(HttpServletRequest request) {
        Capabilities capabilities = getCapabilities(request);
        return capabilities.getValue(BrowsCapField.BROWSER);
    }

    /**
     * 获取远程客户端浏览器版本
     *
     * @param request
     * @return
     */
    public static String getBrowserVersion(HttpServletRequest request) {
        Capabilities capabilities = getCapabilities(request);
        return capabilities.getBrowserMajorVersion();
    }

    /**
     * 获取远程客户端浏览器信息
     *
     * @param request
     * @return
     */
    public static String getBrowserInfo(HttpServletRequest request) {
        Capabilities capabilities = getCapabilities(request);
        return capabilities.getBrowser() + "/" + capabilities.getBrowserMajorVersion();
    }

    private static Capabilities getCapabilities(HttpServletRequest request) {
        try {
            UserAgentParser userAgentParser = new UserAgentService().loadParser();
            return userAgentParser.parse(request.getHeader("User-Agent"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36";
        try {
            UserAgentParser userAgentParser = new UserAgentService().loadParser();
            Capabilities capabilities = userAgentParser.parse(userAgent);
            System.out.println("系统名称==>" + capabilities.getPlatform() + "/" + capabilities.getPlatformVersion());
            System.out.println("浏览器信息==>" + capabilities.getBrowser() + "/" + capabilities.getBrowserMajorVersion());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
