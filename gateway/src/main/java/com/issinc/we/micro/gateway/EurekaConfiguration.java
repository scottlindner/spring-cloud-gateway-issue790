package com.issinc.we.micro.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableDiscoveryClient
public class EurekaConfiguration {

    @Autowired
    DiscoveryClient discoveryClient;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {

        RouteLocatorBuilder.Builder myBuilder = builder.routes();

        List<String> services = discoveryClient.getServices();
        for (String service : services) {
            String routePath = String.format("/%s/**", service);
            String loadBalancerUri = "lb://".concat(service);
            myBuilder.route(r -> r.path(routePath).filters(f -> f.stripPrefix(1)).uri(loadBalancerUri));
        }

        return myBuilder.build();
    }
}