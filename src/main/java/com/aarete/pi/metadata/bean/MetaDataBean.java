package com.aarete.pi.metadata.bean;

import java.util.ArrayList;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "This bean will be used to send or receive records from the dynamodb.")
public class MetaDataBean {
	
	@NotBlank(message = "Table Name is Mandatory")
	private String tableName;
	
	@NotBlank(message = "PartitionKey Name is Mandatory")
	private String partitionKeyName;
	
	@NotBlank(message = "PartitionKey Value is Mandatory")
	private String partitionKeyValue;
	
	private String sortKeyName;
	
	private String sortKeyValue;
	
	@ApiModelProperty(notes = "This list will be used to send or receive columns which are not partition or sort key from the dynamodb.")
	private ArrayList<KeyValueBean> keyValueBeans = new ArrayList<>();
	 
}
