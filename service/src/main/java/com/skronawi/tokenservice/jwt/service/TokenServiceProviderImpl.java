package com.skronawi.tokenservice.jwt.service;

import com.skronawi.tokenservice.jwt.api.TokenService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TokenServiceProviderImpl implements TokenServiceProvider {

    private AnnotationConfigApplicationContext annotationConfigApplicationContext;

    public void init() {
        annotationConfigApplicationContext =
                new AnnotationConfigApplicationContext(TokenServiceProvisioning.class);
    }

    public void teardown() {
        //TODO
        //annotationConfigApplicationContext.getBean(TokenService.class).getLifeCycle().teardown();
    }

    public TokenService get() {
        return annotationConfigApplicationContext.getBean(TokenService.class);
    }
}
