package com.ccm.customer.config;

import com.ccm.customer.auth.AuthHeaderForwardInterceptor;
import com.ccm.customer.feignclient.EurekaFeignClientImpl;
import com.ccm.customer.listener.EurekaClientInitializer;
import feign.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;

@Configuration
public class FeignClientConfig {

    // This allows the FeignClient to access the current request context and its header
    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    /* Registers the interceptor that copies the JWT Authorization header
        to all outgoing requests for the respective FeignClient's request.
    */
    @Bean
    public AuthHeaderForwardInterceptor authHeaderForwardInterceptor() {
        return new AuthHeaderForwardInterceptor();
    }

    @Bean
    public Client eurekaFeignClient(EurekaClientInitializer eurekaClientInitializer) {
        return new EurekaFeignClientImpl(eurekaClientInitializer);
    }
}

