package com.aarete.pi.metdata.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.aarete.pi.metadata.bean.UserDetailsBean;
import com.aarete.pi.metadata.constant.MetaDataConstants;
import com.aarete.pi.metadata.helper.SecurityHelper;



@Component
public class ServiceInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		UserDetailsBean userDetailBean = SecurityHelper.getUserDetailsBean();
		if (userDetailBean != null) {
			MDC.put(MetaDataConstants.LOGGING_MDC_USER_ID, userDetailBean.getUserId());
			MDC.put(MetaDataConstants.LOGGING_MDC_USER, userDetailBean.getName());
		}
		return true;
	}
}