package com.liferon.ip.management.utils;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

public class OrikaUtils {
    private static MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();


    public static <T> List<T> map(List<?> objects, Class<T> target) {
        if (CollectionUtils.isEmpty(objects)) {
            return Collections.EMPTY_LIST;
        }
        return mapperFactory.getMapperFacade().mapAsList(objects.toArray(), target);
    }
}
