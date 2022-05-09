package com.aarete.pi.metadata.helper;

import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import com.aarete.pi.metadata.bean.UserDetailsBean;
import com.aarete.pi.metadata.exception.AccessDenied;
import com.aarete.pi.metadata.util.JsonParser;

import static com.aarete.pi.metadata.constant.MetaDataConstants.PAYLOAD_NAME;
import static com.aarete.pi.metadata.constant.MetaDataConstants.PAYLOAD_ROLES;
import static com.aarete.pi.metadata.constant.MetaDataConstants.PAYLOAD_PREFERRED_USERNAME;

public class SecurityHelper {

	private SecurityHelper() {

	}

	public static UserDetailsBean getUserDetailsBean() throws AccessDenied {
		UserDetailsBean userDetailsBean = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			if (Objects.nonNull(authentication) && Objects.nonNull(authentication.getDetails())) {
				String tokenValue = ((OAuth2AuthenticationDetails) authentication.getDetails()).getTokenValue();
				String[] chunks = tokenValue.split("\\.");
				Base64.Decoder decoder = Base64.getDecoder();
				if (Objects.nonNull(chunks) && chunks.length > 1) {
					String payload = new String(decoder.decode(chunks[1]));
					LinkedHashMap<String, Object> payloadMap = (LinkedHashMap) JsonParser.convertToJson(payload);
					userDetailsBean = new UserDetailsBean();
					userDetailsBean.setName((String) payloadMap.get(PAYLOAD_NAME));
					userDetailsBean.setRoleList((List<String>) payloadMap.get(PAYLOAD_ROLES));
					userDetailsBean.setUserId((String) payloadMap.get(PAYLOAD_PREFERRED_USERNAME));
				}
			} else {
				throw new AccessDenied("Token Not Found");
			}
		}

		return userDetailsBean;
	}

}
