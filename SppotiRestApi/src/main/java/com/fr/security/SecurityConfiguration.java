package com.fr.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
	
	@Autowired
	private AuthFailure authFailure;
	
	@Autowired
	private AuthSuccess authSuccess;
	
	@Autowired
	private UserDetailsService userDetailService;
	
	@Autowired
	private LogoutSuccessHandler logoutSuccessHandler;
	
	@Autowired
	private CustomLogoutHandler logoutHandler;
	
	@Autowired
	private EntryPointUnAthorisedHandler pointUnAthorisedHandler;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void configure(final HttpSecurity http) throws Exception
	{
		http.
			cors()
				.and()
					.csrf().disable()
					.requestCache()
					.requestCache(new NullRequestCache())
				.and()
					.httpBasic()
				.and()
					.formLogin()
				.and()
					.addFilterBefore(customUsernamePasswordAuthenticationFilter(),
							UsernamePasswordAuthenticationFilter.class)
					.authorizeRequests()
						.antMatchers("/trade/**").permitAll()
						//.antMatchers(HttpMethod.GET, "/**").permitAll()
						.antMatchers("/**/sport/**", "/**/contact/**", "/**/init/token").permitAll()
						.antMatchers(HttpMethod.POST, "/**/account/create").permitAll()
						.antMatchers(HttpMethod.PUT,"/**/account/recover/**", "/**/account/validate/password/**").permitAll()
						.antMatchers(HttpMethod.POST, "/**/account/connexion/endpoint").permitAll()
						.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.anyRequest().authenticated()
				.and()
					.logout()
						.addLogoutHandler(this.logoutHandler)
						.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
						.logoutSuccessHandler(this.logoutSuccessHandler)
				.and()
					.exceptionHandling()
					.authenticationEntryPoint(this.pointUnAthorisedHandler)
				.and()
					.headers()
					.xssProtection()
					.xssProtectionEnabled(true);
	}
	
	/** Init custom authentification filter. */
	@Bean
	CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter() throws Exception {
		final CustomUsernamePasswordAuthenticationFilter authenticationFilter
				= new CustomUsernamePasswordAuthenticationFilter();
		authenticationFilter.setAuthenticationManager(authenticationManagerBean());
		authenticationFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
		authenticationFilter.setUsernameParameter("username");
		authenticationFilter.setPasswordParameter("password");
		authenticationFilter.setFilterProcessesUrl("/auth/user");
		authenticationFilter.setAuthenticationSuccessHandler(this.authSuccess);
		authenticationFilter.setAuthenticationFailureHandler(this.authFailure);
		return authenticationFilter;
	}
	
	/** Init password encoder bean. */
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	/** Configure authentication provider. */
	@Autowired
	public void configureGlobalSecurity(final AuthenticationManagerBuilder auth) throws Exception
	{
		auth.userDetailsService(this.userDetailService).passwordEncoder(passwordEncoder());
	}
}
