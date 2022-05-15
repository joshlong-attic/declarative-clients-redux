package com.example.client;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Collection;

@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    @Bean
    WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl("http://localhost:8080").build();
    }

    @Bean
    CrmClient crmClient(WebClient http) {
        var proxy = HttpServiceProxyFactory
                .builder(new WebClientAdapter(http))
                .build();
        return proxy.createClient(CrmClient.class);
    }

    @Bean
    ApplicationRunner applicationRunner(CrmClient crm) {
        return args -> crm.getCustomers().forEach(System.out::println);
    }
}


record Customer(Integer id, String name) {
}

interface CrmClient {

    @GetExchange("/customers")
    Collection<Customer> getCustomers();
}