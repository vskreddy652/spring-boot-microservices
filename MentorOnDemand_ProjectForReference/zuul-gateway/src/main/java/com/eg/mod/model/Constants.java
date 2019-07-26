package com.eg.mod.model;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Constants {

	public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 5 * 60 * 60;
	public static final String SIGNING_KEY = "devglan123r";
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String AUTHORITIES_KEY = "scopes";

	public static final List<Locale> LOCALES = Arrays.asList(
			new Locale("en"), new Locale("fr"), new Locale("de"),
			new Locale("it"), new Locale("ja"), new Locale("ko"), 
			new Locale("zh"), new Locale("de"), new Locale("de")
	);
}
