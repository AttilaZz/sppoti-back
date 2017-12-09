/**
 *
 */
package com.fr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mobile.device.DeviceHandlerMethodArgumentResolver;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */
@Configuration
@EnableWebMvc
//@EnableAspectJAutoProxy
public class ConfigApi extends WebMvcConfigurerAdapter
{
	//	@Bean
	//	public LiteDeviceResolver liteDeviceResolver() {
	//		final List<String> keywords = new ArrayList<>();
	//		keywords.add("iphone");
	//		keywords.add("android");
	//		return new LiteDeviceResolver(keywords);
	//	}
	
	
	@Bean
	public DeviceResolverHandlerInterceptor deviceResolverHandlerInterceptor() {
		return new DeviceResolverHandlerInterceptor();
	}
	
	@Bean
	public DeviceHandlerMethodArgumentResolver deviceHandlerMethodArgumentResolver() {
		return new DeviceHandlerMethodArgumentResolver();
	}
	
	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		registry.addInterceptor(deviceResolverHandlerInterceptor());
	}
	
	@Override
	public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(deviceHandlerMethodArgumentResolver());
	}
	
	//	@Bean
	//	public WebMvcConfigurerAdapter webMvcConfigurerAdapter() {
	//		return new WebMvcConfigurerAdapter()
	//		{
	//			@Bean
	//			public DeviceResolverHandlerInterceptor deviceResolverHandlerInterceptor() {
	//				return new DeviceResolverHandlerInterceptor();
	//			}
	//
	//			@Bean
	//			public DeviceHandlerMethodArgumentResolver deviceHandlerMethodArgumentResolver() {
	//				return new DeviceHandlerMethodArgumentResolver();
	//			}
	//
	//			@Override
	//			public void addInterceptors(final InterceptorRegistry registry) {
	//				registry.addInterceptor(deviceResolverHandlerInterceptor());
	//			}
	//
	//			@Override
	//			public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> argumentResolvers) {
	//				argumentResolvers.add(deviceHandlerMethodArgumentResolver());
	//			}
	//		};
	//	}
}
