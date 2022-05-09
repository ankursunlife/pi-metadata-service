package com.aarete.pi.metadata.service;

import org.springframework.stereotype.Service;

import com.aarete.pi.metadata.bean.MetaDataBean;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.UpdateItemResult;

/**
 * @author akadam
 *
 */
@Service
public interface MetaDataService {

	public Item getMetaData(String tableName, String partitionkey,String value, String sortkey, String sortkeyvalue);
	
	public UpdateItemResult updateMetaData(MetaDataBean staticDataBean);

	public DeleteItemResult deleteMetaData(String tableName, String partitionkey, String value);

	public PutItemResult addMetaData(MetaDataBean staticDataBean);

	public String getAllItemsFromTable(String tableName);

	public ListTablesResult getTableList();

}
