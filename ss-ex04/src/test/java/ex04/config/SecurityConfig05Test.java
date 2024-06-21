package ex04.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
@ContextConfiguration(classes={WebConfig.class, SecurityConfig05.class})
@WebAppConfiguration
public class SecurityConfig05Test {
    private MockMvc mvc;

    private FilterChainProxy filterChainProxy;

    @BeforeEach
    public void setup(WebApplicationContext context) {
        filterChainProxy = (FilterChainProxy)context.getBean("springSecurityFilterChain", Filter.class);

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilter(new DelegatingFilterProxy(filterChainProxy), "/*")
                .build();
    }
    
    @Test
    public void testSecurityFilterChains() {
        List<SecurityFilterChain> SecurityFilterChains = filterChainProxy.getFilterChains();
        assertEquals(2, SecurityFilterChains.size());
    }
    
    @Test
    public void testSecurityFilters() {
        SecurityFilterChain securityFilterChain = filterChainProxy.getFilterChains().get(1);
        List<Filter> filters =  securityFilterChain.getFilters();

        assertEquals(13, filters.size());
        
        // All Filters
        for(Filter filter : filters) {
            System.out.println(filter.getClass());
        }
    }

    @Test
    public void testWebSecurity() throws Throwable {
        mvc
        	.perform(get("/assets/images/logo.png"))
        	.andExpect(status().isOk());
    }

    @Test
    public void testHttpSecurity() throws Throwable {
        mvc
        	.perform(get("/hello"))
        	.andExpect(status().isOk())
        	.andDo(print());
    }
}