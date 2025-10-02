package com.ksanaDock.config;

import com.ksanaDock.util.concurrent.ThreadPoolManager;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Component
public class ThreadPoolShutdownHook implements ApplicationListener<ContextClosedEvent> {

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        // 当主程序停止时，关闭线程池
        ThreadPoolManager.shutdown();
    }
}
