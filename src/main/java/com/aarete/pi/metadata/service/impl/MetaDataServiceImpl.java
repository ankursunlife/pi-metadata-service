package com.aarete.pi.metadata.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aarete.pi.metadata.bean.MetaDataBean;
import com.aarete.pi.metadata.service.MetaDataService;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeAction;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ReturnConsumedCapacity;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemResult;

/**
 * @author akadam
 *
 */
@Service
public class MetaDataServiceImpl implements MetaDataService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AmazonDynamoDB amazonDynamoDB;

	@Override
	public Item getMetaData(String tableName, String partitionkey, String partitionkeyValue, String sortkey,
			String sortkeyvalue) throws AmazonServiceException {
		DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
		Table table = dynamoDB.getTable(tableName);
		GetItemSpec getItemSpec = new GetItemSpec().withPrimaryKey(partitionkey, partitionkeyValue, sortkey,
				sortkeyvalue);
		Item item = null;
		try {
			item = table.getItem(getItemSpec);
		} catch (AmazonServiceException e) {
			log.error(e.getErrorMessage());
			throw e;
		}
		return item;
	}

	@Override
	public DeleteItemResult deleteMetaData(String tableName, String partitionkey, String value)
			throws AmazonServiceException {

		DeleteItemRequest request = new DeleteItemRequest();

		/* Setting Table Name */
		request.setTableName(tableName);

		/* Setting Consumed Capacity */
		request.setReturnConsumedCapacity(ReturnConsumedCapacity.TOTAL);

		/* To get old value of item's attributes before it is deleted */
		request.setReturnValues(ReturnValue.ALL_OLD);

		/* Create a Map of Primary Key attributes */
		Map<String, AttributeValue> keysMap = new HashMap<>();
		keysMap.put(partitionkey, new AttributeValue(value));
		request.setKey(keysMap);

		DeleteItemResult result = null;
		try {
			/* Send Delete Item Request */
			result = amazonDynamoDB.deleteItem(request);
		} catch (AmazonServiceException e) {
			log.error(e.getErrorMessage());
			throw e;
		}
		return result;
	}

	@Override
	public PutItemResult addMetaData(MetaDataBean metaDataBean) throws AmazonServiceException {
		final Map<String, String> infoMap = new HashMap<String, String>();
		metaDataBean.getKeyValueBeans().stream().forEach(sdb -> infoMap.put(sdb.getKey(), sdb.getValue()));

		Map<String, AttributeValue> attributeValues = new HashMap<>();
		/* Adding Partition key in map */
		attributeValues.put(metaDataBean.getPartitionKeyName(),
				new AttributeValue().withS(metaDataBean.getPartitionKeyValue()));

		/* Adding rest of the columns in map */
		metaDataBean.getKeyValueBeans().stream()
				.forEach(sdb -> attributeValues.put(sdb.getKey(), new AttributeValue().withS(sdb.getValue())));

		PutItemRequest putItemRequest = new PutItemRequest().withTableName(metaDataBean.getTableName())
				.withItem(attributeValues);
		PutItemResult putItemResult = null;
		try {
			/* Send Add Item Request */
			putItemResult = amazonDynamoDB.putItem(putItemRequest);
		} catch (AmazonServiceException e) {
			log.error(e.getErrorMessage());
			throw e;
		}
		return putItemResult;
	}

	@Override
	public UpdateItemResult updateMetaData(MetaDataBean metaDataBean) throws AmazonServiceException {

		UpdateItemRequest request = new UpdateItemRequest();

		/* Setting Table Name */
		request.setTableName(metaDataBean.getTableName());

		/* Setting Consumed Capacity */
		request.setReturnConsumedCapacity(ReturnConsumedCapacity.TOTAL);

		/* To get old value of item's attributes before it is updated */
		request.setReturnValues(ReturnValue.UPDATED_OLD);

		/* Create a Map of Primary Key attributes */
		Map<String, AttributeValue> keysMap = new HashMap<>();
		keysMap.put(metaDataBean.getPartitionKeyName(), new AttributeValue(metaDataBean.getPartitionKeyValue()));
		keysMap.put(metaDataBean.getSortKeyName(), new AttributeValue(metaDataBean.getSortKeyValue()));
		request.setKey(keysMap);

		/* Create a Map of attributes to be updated */
		Map<String, AttributeValueUpdate> map = new HashMap<>();

		metaDataBean.getKeyValueBeans().stream().forEach(sdb -> map.put(sdb.getKey(),
				new AttributeValueUpdate(new AttributeValue(sdb.getValue()), AttributeAction.PUT)));

		request.setAttributeUpdates(map);
		UpdateItemResult result = null;
		try {
			/* Send Update Item Request */
			result = amazonDynamoDB.updateItem(request);
		} catch (AmazonServiceException e) {
			log.error(e.getErrorMessage());
			throw e;
		}

		return result;
	}

	@Override
	public String getAllItemsFromTable(String tableName) {
		return null;
	}

	@Override
	public ListTablesResult getTableList() {
		return amazonDynamoDB.listTables();
	}

}
