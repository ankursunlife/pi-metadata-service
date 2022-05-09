package com.aarete.pi.metadata.bean;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "This bean will be used to send or receive columns which are not partition or sort key from the dynamodb.")
public class KeyValueBean {

	private String key;
	private String value;
}
