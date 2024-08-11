package com.aixming.yudada.config;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Duzeming
 * @since 2024-08-10 16:39:24
 */
@Configuration
public class VipSchedulerConfig {

    @Bean
    public Scheduler vipSchduler() {
        ThreadFactory threadFactory = new ThreadFactory() {

            private final AtomicInteger threadNumber = new AtomicInteger(0);

            @Override
            public Thread newThread(@NotNull Runnable r) {
                Thread thread = new Thread("VIPThreadPool-" + threadNumber.getAndIncrement());
                thread.setDaemon(false);
                return thread;
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(10, threadFactory);

        return Schedulers.from(executor);
    }
}
