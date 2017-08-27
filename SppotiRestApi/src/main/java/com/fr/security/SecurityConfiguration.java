package com.fr.security;

import com.fr.filter.CsrfProperties;
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
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
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
	
	/** Custom auth failure. */
	@Autowired
	private AuthFailure authFailure;
	
	/** Custom auth success. */
	@Autowired
	private AuthSuccess authSuccess;
	
	/** Security user details. */
	@Autowired
	private UserDetailsService userDetailService;
	
	/** Custom logout success handler. */
	@Autowired
	private LogoutSuccessHandler logoutSuccessHandler;
	
	/** Logout handler. */
	@Autowired
	private CustomLogoutHandler logoutHandler;
	
	/** Remember me token repository. */
	@Autowired
	private PersistentTokenRepository tokenRepository;
	
	/** Entry point filter. */
	@Autowired
	private EntryPointUnAthorisedHandler pointUnAthorisedHandler;
	/** CSRf token properties. */
	@Autowired
	private CsrfProperties filterProperties;
	
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
						.antMatchers("/**/sport/**", "/**/contact/**", "/**/init/token").permitAll()
						.antMatchers(HttpMethod.POST, "/**/account/create").permitAll()
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
	
	//	/**
	//	 * Configure CSRF token.
	//	 */
	//	private CsrfTokenRepository csrfTokenRepository()
	//	{
	//		final HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
	//		repository.setHeaderName("X-XSRF-TOKEN");
	//		return repository;
	//	}
	
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
	
	//	/** Init remember me token service. */
	//	@Bean
	//	public PersistentTokenBasedRememberMeServices getPersistentTokenBasedRememberMeServices()
	//	{
	//		return new PersistentTokenBasedRememberMeServices("remember-me", this.userDetailService, this.tokenRepository);
	//	}
}
