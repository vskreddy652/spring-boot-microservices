package com.eg.mod.filter;

import javax.servlet.http.HttpServletRequest;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class PreFilter extends ZuulFilter {

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();

		ctx.addZuulRequestHeader("Authorization", request.getHeader("Authorization"));
		ctx.addZuulRequestHeader("Accept-Language", request.getHeader("Accept-Language"));
		System.out.println(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));
		System.out.println("Request Method : " + request.getMethod() + " Request URL : " + request.getRequestURL().toString());
		//System.out.println("Authorization "+request.getHeader("Authorization"));
		//System.out.println("Accept-Language "+request.getHeader("Accept-Language"));
		
		return null;
	}

}