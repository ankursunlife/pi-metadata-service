package com.aarete.pi.metadata.helper;

import static com.aarete.pi.metadata.constant.MetaDataConstants.KEY_USER_ID;

import com.aarete.pi.metadata.bean.UserDetailsBean;
import com.aarete.pi.metadata.exception.AccessDenied;

public class AccessValidationHelper {

	private AccessValidationHelper() {
	}

	public static void validateGetMetaData(String tableName, String partitionkey, String partitionkeyvalue,
			String sortkey, String sortkeyvalue) throws AccessDenied {
		UserDetailsBean userDetailsBean = SecurityHelper.getUserDetailsBean();
		if (KEY_USER_ID.equals(partitionkey)
				&& !partitionkeyvalue.equals(userDetailsBean.getUserId())) {
			throw new AccessDenied(String.format("You have no access to %s ", sortkeyvalue));
		}

	}

}
