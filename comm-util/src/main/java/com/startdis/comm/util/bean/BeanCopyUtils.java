package com.startdis.comm.util.bean;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Startdis
 * @email startdis@dianjiu.cc
 * @desc BeanCopyUtils
 */
public class BeanCopyUtils {
    
    /**
     * 浅拷贝list
     *
     * @param source 数据源
     * @param clazz  对象
     * @return LIST
     */
    public static List copyListProperties(List source, Class clazz) {
        List target = new ArrayList();
        source.forEach(o -> {
            try {
                target.add(JSONObject.parseObject(JSONObject.toJSONString(o), clazz));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return target;
    }
    
    /**
     * 源对象和目标对象浅拷贝 PS：目标对象和源对象中的属性名称需一至，否则无法copy
     *
     * @param sourceObj 源对象
     * @param targetObj 目标对象
     */
    public static void copyPropertiesThird(Object sourceObj, Object targetObj) {
        BeanUtils.copyProperties(sourceObj, targetObj);
    }
    
    public static <T> T copyPropertiesThird(Object sourceObj, Class<T> calzz) {
        if (Objects.isNull(sourceObj) || Objects.isNull(calzz)) {
            return null;
        }
        T c = null;
        try {
            // c = calzz.newInstance();
            c = calzz.getDeclaredConstructor().newInstance();
            BeanCopyUtils.copyPropertiesThird(sourceObj, c);
        } catch (Exception e) {
            System.out.println("BeanCopyKits -> copyPropertiesThird error：" + e);
        }
        return c;
    }
    
    /**
     * 转换list泛型
     *
     * @param source 数据源
     * @param clazz  对象
     * @return LIST
     */
    public static <T> List<T> coverList(List source, Class<T> clazz) {
        List<T> res = new ArrayList<>();
        source.forEach(o -> res.add(BeanCopyUtils.copyPropertiesThird(o, clazz)));
        return res;
    }
    
}