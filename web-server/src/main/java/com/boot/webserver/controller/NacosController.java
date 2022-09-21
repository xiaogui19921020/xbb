package com.boot.webserver.controller;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.boot.webserver.service.AsyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/nacos")
public class NacosController {

    @Autowired
    private AsyncService asyncService;

    @Value("${spring.application.name}")
    private String serviceName;

    @Value("${server.port}")
    private int servicePort;

    @Value("${spring.cloud.nacos.discovery.server-addr}")
    private String nacosServerAddr;


    @GetMapping(value = "/info")
    public Map<String, String> info() throws NacosException {
        log.info(" NacosController metaData start");
        NamingService namingService = NacosFactory.createNamingService(nacosServerAddr);
        Map<String, String> metaData = new LinkedHashMap<>();
        List<Instance> instances = namingService.selectInstances(serviceName, true);

        if (!CollectionUtils.isEmpty(instances)) {
            for (Instance instance : instances) {
                if (servicePort == instance.getPort()) {
                    metaData.put("ip", instance.getIp());
                    metaData.put("port", String.valueOf(instance.getPort()));
                    metaData.putAll(instance.getMetadata());
                }
            }
        }
        asyncService.traceId();
        log.info(" NacosController metaData {} , end", metaData);
        return metaData;
    }


}
