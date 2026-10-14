package com.ccm.customer.feignclient;

import com.ccm.customer.listener.EurekaClientInitializer;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import feign.Client;
import feign.Request;
import feign.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Locale;

/**
 * Custom Feign Client that dynamically resolves service instances through Eureka.
 * It replaces the service name in the Feign request with the URL of an available UP Eureka instance,
 * allowing inter-service communication without hard-coded URLs.
 */
public class EurekaFeignClientImpl implements Client {

    private static final Logger logger = LoggerFactory.getLogger(EurekaFeignClientImpl.class);

    private final EurekaClientInitializer eurekaClientInitializer;
    private final Client delegate;

    public EurekaFeignClientImpl(EurekaClientInitializer eurekaClientInitializer) {
        this.eurekaClientInitializer = eurekaClientInitializer;
        this.delegate = new Default(null, null);
    }

    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        EurekaClient eurekaClient = eurekaClientInitializer.getEurekaClient();

        if (eurekaClient == null) {
            throw new IllegalStateException("Eureka client is not initialized.");
        }

        URI originalUri = URI.create(request.url());
        String serviceName = originalUri.getHost();

        if (serviceName == null || serviceName.isBlank()) {
            throw new IllegalStateException("Unable to determine Eureka service name from Feign URL: " + request.url());
        }
        logger.debug("Resolving Feign service '{}' through Eureka.", serviceName);

        // Eureka normally stores application names in UPPERCASE.
        String eurekaApplicationName = serviceName.toUpperCase(Locale.ROOT);
        var application = eurekaClient.getApplication(eurekaApplicationName);

        if (application == null || application.getInstances() == null || application.getInstances().isEmpty()) {
            throw new IllegalStateException("Service '" + serviceName + "' is not registered with Eureka.");
        }

        // Only use instances that are currently UP.
        List<InstanceInfo> instances = application.getInstances().stream()
                .filter(instance -> instance.getStatus() == InstanceInfo.InstanceStatus.UP)
                .toList();

        if (instances.isEmpty()) {
            throw new IllegalStateException("No UP instance found for Eureka service => '" + serviceName + "'.");
        }

        // Use the first available UP instance.
        InstanceInfo instance = instances.get(0);

        // getHomePageUrl() is to get the application's servlet context path.
        String baseUrl = instance.getHomePageUrl();

        // Remove the Eureka service name from the original Feign URL & create new URL
        String path = originalUri.getPath();
        String newUrl = baseUrl + path;

        // Preserve query parameters if exists
        if (originalUri.getQuery() != null) {
            newUrl += "?" + originalUri.getQuery();
        }

        logger.debug("Feign service '{}' resolved to '{}'", serviceName, newUrl);

        // Create the actual HTTP request using the Eureka-resolved URL.
        Request newRequest = Request.create(
                request.httpMethod(),
                newUrl,
                request.headers(),
                request.body(),
                request.charset(),
                request.requestTemplate());

        return delegate.execute(newRequest, options);
    }
}
