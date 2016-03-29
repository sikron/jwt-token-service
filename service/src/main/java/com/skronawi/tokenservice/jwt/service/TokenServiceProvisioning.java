package com.skronawi.tokenservice.jwt.service;

import com.skronawi.configservice.api.ConfigService;
import com.skronawi.configservice.api.DefaultConfigServiceConfiguration;
import com.skronawi.configservice.api.PropertyReadAccess;
import com.skronawi.configservice.core.ConfigServiceImpl;
import com.skronawi.tokenservice.jwt.api.TokenService;
import com.skronawi.tokenservice.jwt.api.TokenServiceConfig;
import com.skronawi.tokenservice.jwt.core.JwtKeyUtil;
import com.skronawi.tokenservice.jwt.core.TokenServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Collections;

@Configuration
public class TokenServiceProvisioning {

    private static final com.skronawi.configservice.api.Configuration TOKEN_CONFIG =
            () -> "jwt-tokenservice";

//    @Autowired
//    private Environment environment;

//    @Bean
//    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
//        return new PropertySourcesPlaceholderConfigurer();
//    }

    @Bean
    public TokenService tokenService() throws IOException {
        TokenServiceConfig tokenServiceConfig = new TokenServiceConfig();
        PropertyReadAccess propertyReadAccess = configService().getPropertyReadAccess();
        tokenServiceConfig.setLifeTimeMinutes(propertyReadAccess.getIntByKeyOrDefault(
                "tokenservice.jwt.key.lifetime.minutes", 30).getValue());
        tokenServiceConfig.setRsaPrivateKey(JwtKeyUtil.fromPrivateString(propertyReadAccess.getByKey(
                "tokenservice.jwt.key.private").getValue()));
        tokenServiceConfig.setRsaPublicKey(JwtKeyUtil.fromPublicString(propertyReadAccess.getByKey(
                "tokenservice.jwt.key.public").getValue()));
        return new TokenServiceImpl(tokenServiceConfig);
    }

    private ConfigService configService() throws IOException {
        ConfigServiceImpl configService = new ConfigServiceImpl();
        configService.getLifeCycle().init(new DefaultConfigServiceConfiguration());
        configService.getLifeCycle().load(Collections.singleton(TOKEN_CONFIG));
        return configService;
    }

//    @Bean
//    public TokenService tokenService() throws IOException {
//        //TODO should use configservice in order to be reloadable
//        Map<String, String> gatheredConfig = getGatheredConfig();
//        TokenServiceConfig tokenServiceConfig = new TokenServiceConfig();
//        tokenServiceConfig.setLifeTimeMinutes(Long.parseLong(gatheredConfig
//                .getOrDefault("tokenservice.jwt.key.lifetime.minutes", "30")));
//        tokenServiceConfig.setRsaPrivateKey(JwtKeyUtil.fromPrivateString(gatheredConfig
//                .get("tokenservice.jwt.key.private")));
//        tokenServiceConfig.setRsaPublicKey(JwtKeyUtil.fromPublicString(gatheredConfig
//                .get("tokenservice.jwt.key.public")));
//        return new TokenServiceImpl(tokenServiceConfig);
//    }
//
//    private Map<String, String> getGatheredConfig() {
//        Map<String, Object> map = new HashMap<>();
//        for (org.springframework.core.env.PropertySource<?> ps :
//                ((AbstractEnvironment) environment).getPropertySources()) {
//            if (ps instanceof MapPropertySource) {
//                map.putAll(((MapPropertySource) ps).getSource());
//            }
//        }
//        Map<String, String> keyValues = new HashMap<>();
//        for (AbstractMap.Entry<String, Object> entry : map.entrySet()) {
//            keyValues.put(entry.getKey(), String.valueOf(entry.getValue()));
//        }
//        return keyValues;
//    }
}
