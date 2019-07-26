package com.eg.mod.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import com.eg.mod.model.Constants;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Configuration
public class CustomLocaleResolver extends AcceptHeaderLocaleResolver implements WebMvcConfigurer {

	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		Locale local = null;
		String lang = request.getParameter("lang");
		if (lang != null && lang.trim().length() > 0)
			local = new Locale(lang);
		else {
			lang = request.getHeader("Accept-Language");
			if (lang != null)
				local = Locale.lookup(Locale.LanguageRange.parse(lang), Constants.LOCALES);
			else {
				local = Locale.getDefault();
			}
		}
		return local;
	}

	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource rs = new ResourceBundleMessageSource();
		rs.setBasename("messages");
		rs.setDefaultEncoding("UTF-8");
		rs.setUseCodeAsDefaultMessage(true);
		return rs;
	}
}
