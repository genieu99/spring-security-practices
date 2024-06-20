package ex01.filter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.Filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

import ex01.config.AppConfig;
import ex01.config.WebConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes= {WebConfig.class, AppConfig.class})
@WebAppConfiguration
public class MyFilterTest {
	private MockMvc mvc;
	
	@BeforeEach
	public void setUp(WebApplicationContext applicationContext) {
		Filter myFilter = applicationContext.getBean("myFilter", Filter.class);
		
		mvc = MockMvcBuilders
				.webAppContextSetup(applicationContext)
				.addFilter(new DelegatingFilterProxy(myFilter), "/*")
				.build();
	}
	
	@Test
	public void testMyFilter() throws Throwable {
		mvc
			.perform(get("/hello"))
			.andExpect(status().isOk())
			.andExpect(cookie().value("MyFilter", "Wroks"))
			.andDo(print());
	}
}
