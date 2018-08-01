package com.xrj.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
    private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtAuthenticationProvider customJwtAuthenticationProvider;
	
    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }
    
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // This method is here with the @Bean annotation so that Spring can
        // autowire it
        return super.authenticationManagerBean();
    }

    /**
     * 配置认证Manager,自定义身份验证
     * @param authenticationManagerBuilder
     * @throws Exception
     */
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(customJwtAuthenticationProvider);
//    }
    
    @Autowired
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
    
    /**
     * 装载BCrypt密码编码器
     * SHA-256 +随机盐+密钥
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	/* 
	 * 对http的请求配置
	 */
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception{
		httpSecurity.headers()
				.frameOptions()  //缓解点击劫持攻击
		        .sameOrigin()
	        .and()
		        // disable CSRF, httpSecurity basic, form login
		        .csrf().disable()
		        // 跨域支持
		        //.cors().and()
		        .httpBasic().disable()  //仅仅使用basic认证方式来实现用户登录
		        .formLogin().disable()  //仅仅使用form表单认证方式来实现用户登录
		        // REST is stateless, no sessions
		        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
	        .and()
	        		// return 403 when not authenticated
	        		.exceptionHandling().authenticationEntryPoint(new NoAuthenticationEntryPoint());
		
		httpSecurity.authorizeRequests()
			.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()  //OPTIONS是预防问
			
			.antMatchers(HttpMethod.GET,
					"/*.html",
                    "/favicon.ico",
                    "/**/*.html",
                    "/**/*.css",
                    "/**/*.js").permitAll()
			
			.antMatchers("/login").permitAll()
			// 对于获取token的rest api要允许匿名访问
            .antMatchers("/auth/**").permitAll()
            // 除上面外的所有请求全部需要鉴权认证
            .anyRequest().authenticated()
			;
		
        // 添加JWT filter-获取username->UsernamePasswordAuthenticationFilter-验证pwd是否正确
		httpSecurity.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
		
		// 禁用缓存
        httpSecurity.headers().cacheControl();
	}

}
