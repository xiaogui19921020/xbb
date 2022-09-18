package com.boot.webserver.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AsyncService {
    @Async
    @SneakyThrows
    public void traceId() {
        Thread.sleep(1000);
        log.info("AsyncService traceId test  ");
    }
}
