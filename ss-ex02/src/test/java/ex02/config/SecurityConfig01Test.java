package ex02.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.servlet.Filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes= {WebConfig.class, SecurityConfig01.class})
@WebAppConfiguration
public class SecurityConfig01Test {
	
	private MockMvc mvc;
	private FilterChainProxy filterChainProxy;
	
	@BeforeEach
	public void setup(WebApplicationContext applicationContext) {
		filterChainProxy = applicationContext.getBean("springSecurityFilterChains", FilterChainProxy.class);
		mvc = MockMvcBuilders
				.webAppContextSetup(applicationContext)
				.addFilter(new DelegatingFilterProxy(filterChainProxy), "/*")
				.build();
	}
	
	@Test
	public void testSecurityFilterChains() {
		List<SecurityFilterChain> securityFilterChains = filterChainProxy.getFilterChains();
		assertEquals(2, securityFilterChains.size());
	}
	
	@Test
	public void testSecurityFilters() {
		SecurityFilterChain securityFilterChain = filterChainProxy.getFilterChains().get(1);
		List<Filter> filters = securityFilterChain.getFilters();
		
		assertEquals(3, filters.size());
	}
	
	@Test
	public void testSecurityFilterChain01() throws Throwable {
		mvc
			.perform(get("/asserts/images/logo.png"))
			.andExpect(status().isOk())
			.andExpect(cookie().doesNotExist("MySecurityFilter01"));
	}
	
	@Test
	public void testSecurityFilterChain02() throws Throwable {
		mvc
			.perform(get("/hello"))
			.andExpect(status().isOk())
			.andExpect(cookie().value("MySecurityFilter01", "Works"))
			.andExpect(cookie().value("MySecurityFilter02", "Works"))
			.andExpect(cookie().value("MySecurityFilter03", "Works"));
	}
}
