package com.hdfcbank.ef.hazelcastserver.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    public static ApplicationContext getContext() {
        return context;
    }

    public static <T> T getBean(Class<T> bean) {
        return getContext().getBean(bean);
    }

    public static <T> T getBean(String beanName, Class<T> bean) {
        return getContext().getBean(beanName, bean);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    	//So check it "context" is a static so can not update/change its value
        context = applicationContext;
    }
}
