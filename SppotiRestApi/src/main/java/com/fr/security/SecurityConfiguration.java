package com.fr.security;

import com.fr.filter.CsrfProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
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
//					.addFilterAfter(new CsrfHeaderFilter(this.filterProperties), CsrfFilter.class)
//					.csrf().ignoringAntMatchers("/trade/**").csrfTokenRepository(csrfTokenRepository())
//				.and()
					.formLogin()
					.successHandler(savedRequestAwareAuthenticationSuccessHandler())
					.usernameParameter("username")
					.passwordParameter("password")
					.successHandler(this.authSuccess).failureHandler(this.authFailure)

					//				.and().rememberMe().rememberMeParameter("remember-me").tokenRepository(this.tokenRepository)
					//				.tokenValiditySeconds(86400)
				.and().authorizeRequests().antMatchers(HttpMethod.GET, "/**").permitAll()
				.antMatchers(HttpMethod.POST, "/**/account/create").permitAll()
				.antMatchers(HttpMethod.POST, "/**/account/connexion/endpoint").permitAll()
					.antMatchers(HttpMethod.OPTIONS, "/**").permitAll().anyRequest().authenticated()
				.and().logout()
					.addLogoutHandler(this.logoutHandler).logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
					.logoutSuccessHandler(this.logoutSuccessHandler)
				.and()
					.exceptionHandling()
					.authenticationEntryPoint(this.pointUnAthorisedHandler)
				.and()
					.headers()
					.xssProtection()
					.xssProtectionEnabled(true);
	}
	
	/**
	 * Configure CSRF token.
	 */
	private CsrfTokenRepository csrfTokenRepository()
	{
		final HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
		return repository;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Bean
	public SavedRequestAwareAuthenticationSuccessHandler savedRequestAwareAuthenticationSuccessHandler()
	{
		
		final SavedRequestAwareAuthenticationSuccessHandler auth = new SavedRequestAwareAuthenticationSuccessHandler();
		auth.setTargetUrlParameter("/");
		
		return auth;
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
		//		auth.authenticationProvider(authenticationProvider());
		auth.userDetailsService(this.userDetailService).passwordEncoder(passwordEncoder());
		
		auth.inMemoryAuthentication()
				.withUser("user").password("password").roles("USER");
	}
	
	//	/** Init auth provider. */
	//	@Bean
	//	public DaoAuthenticationProvider authenticationProvider()
	//	{
	//		final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
	//		authenticationProvider.setUserDetailsService(this.userDetailService);
	//		authenticationProvider.setPasswordEncoder(passwordEncoder());
	//
	//		return authenticationProvider;
	//	}
	
	/** Init remember me token service. */
	@Bean
	public PersistentTokenBasedRememberMeServices getPersistentTokenBasedRememberMeServices()
	{
		return new PersistentTokenBasedRememberMeServices("remember-me", this.userDetailService, this.tokenRepository);
	}
	
	/** Init auth trust resolver. for remember me */
	@Bean
	public AuthenticationTrustResolver getAuthenticationTrustResolver()
	{
		return new AuthenticationTrustResolverImpl();
	}
}
