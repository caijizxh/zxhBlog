package com.zxh.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {
    private BeanCopyUtils(){}

    public static <T> T copyBean(Object o, Class<T> clazz){
        T t = null;
        try {
            t = clazz.newInstance();
            BeanUtils.copyProperties(o,t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    public static <T,V> List<T> copyBeanList(List<V> list,Class<T> clazz){
        return list.stream()
                .map(o -> copyBean(o, clazz))
                .collect(Collectors.toList());
    }
}
