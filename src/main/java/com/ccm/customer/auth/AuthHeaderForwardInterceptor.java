package com.ccm.customer.auth;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class AuthHeaderForwardInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN_PREFIX = "Bearer ";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // Get the current request attributes
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();

            // Get the Authorization header from the incoming request
            String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

            // Check for existence and the "Bearer " prefix
            if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_TOKEN_PREFIX)) {
                // Adds the Full Authorization header to the outgoing FeignClient's request
                requestTemplate.header(AUTHORIZATION_HEADER, authorizationHeader);
            }
        }
    }
}
