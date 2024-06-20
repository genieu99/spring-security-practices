package ex02.config;

import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;

import ex02.filter.MySecurityFilter01;
import ex02.filter.MySecurityFilter02;
import ex02.filter.MySecurityFilter03;

@Configuration
public class SecurityConfig02 {
	
	@Bean
	public FilterChainProxy springSecurityFilterChain() {
		List<SecurityFilterChain> securityFilterChains = Arrays.asList(
			new DefaultSecurityFilterChain(new AntPathRequestMatcher("/assets/**")),
			new DefaultSecurityFilterChain(new AntPathRequestMatcher("/**"), mySecurityFilter01(), mySecurityFilter02(), mySecurityFilter03())
		);
		return new FilterChainProxy(securityFilterChains);
	}
	
	@Bean
	public MySecurityFilter01 mySecurityFilter01() {
		return new MySecurityFilter01();
	}
	
	@Bean
	public MySecurityFilter02 mySecurityFilter02() {
		return new MySecurityFilter02();
	}
	
	@Bean
	public MySecurityFilter03 mySecurityFilter03() {
		return new MySecurityFilter03();
	}
}
