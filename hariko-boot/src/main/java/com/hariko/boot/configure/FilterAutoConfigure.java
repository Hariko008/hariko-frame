package com.hariko.boot.configure;


import com.hariko.web.filter.HttpHeaderFilter;
import com.hariko.web.filter.MdcFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.http.HttpProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.filter.OrderedCharacterEncodingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@ConditionalOnClass({ HttpHeaderFilter.class, MdcFilter.class })
@EnableConfigurationProperties({HttpProperties.class})
@SuppressWarnings({"unchecked"})
public class FilterAutoConfigure {

	@Autowired
	private HttpProperties httpProperties;

	@Bean
	@ConditionalOnMissingBean(HttpHeaderFilter.class)
	public HttpHeaderFilter httpHeaderFilter() {
		return new HttpHeaderFilter();
	}

	@Bean
	@ConditionalOnMissingBean(MdcFilter.class)
	public MdcFilter mdcFilter() {
		return new MdcFilter();
	}

	@Bean
	@ConditionalOnProperty(prefix = "spring.http.encoding", value = "enabled", matchIfMissing = true)
	public CharacterEncodingFilter characterEncodingFilter() {
		CharacterEncodingFilter filter = new OrderedCharacterEncodingFilter();
		filter.setEncoding(this.httpProperties.getEncoding().getCharset().name());
		filter.setForceRequestEncoding(true);
		filter.setForceResponseEncoding(true);
		return filter;
	}

	//另外一种用@WebFilter配置,在启动类上加载@ServletComponentScan
	@Bean(name = "httpHeaderFilter$1")
	public FilterRegistrationBean httpHeaderFilter$1() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(httpHeaderFilter());
		return registrationBean;
	}

	@Bean(name = "mdcFilter$1")
	public FilterRegistrationBean mdcFilter$1() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(mdcFilter());
		return registrationBean;
	}

}
