package com.fr;

import static com.fr.filter.HeadersValues.Origins;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Created by Moi on 25-Nov-16.
 */

@SpringBootApplication
@PropertySources({
        @PropertySource(value = "classpath:pagination.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:headerConfig.properties", ignoreResourceNotFound = true)
})
public class SppotiApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SppotiApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin(Origins.getValue());
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }
}