package com.fr;

import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.servlet.DispatcherServlet;

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

    /*@Bean
    public SessionFactory sessionFactory(HibernateEntityManagerFactory hemf) {
        return hemf.getSessionFactory();
    }*/

    /*@Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

    @Bean
    public ServletRegistrationBean dispatcherServletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(
                dispatcherServlet(), "/api");
        registration.setName(DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME);
        return registration;
    }*/
}