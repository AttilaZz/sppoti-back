package com.fr;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

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

//    @Override
//    public void onStartup(ServletContext servletContext) throws ServletException {
//        servletContext.getSessionCookieConfig().setName("SPPOTI-SESSION-ID");
//    }

//    @Bean
//    public FilterRegistrationBean corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.addAllowedOrigin(Origins.getValue());
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("*");
//        source.registerCorsConfiguration("/**", config);
//        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
//        bean.setOrder(0);
//        return bean;
//    }
}