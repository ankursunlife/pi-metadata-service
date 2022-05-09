package com.aarete.pi.metadata.controller;

import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aarete.pi.metadata.bean.MetaDataBean;
import com.aarete.pi.metadata.exception.AccessDenied;
import com.aarete.pi.metadata.exception.RecordNotFound;
import com.aarete.pi.metadata.helper.AccessValidationHelper;
import com.aarete.pi.metadata.helper.Helper;
import com.aarete.pi.metadata.service.MetaDataService;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.UpdateItemResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("item")
@Api(tags = "Meta data APIs", value = "MetadataManagement")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MetaDataLoadController {

	@Autowired
	private MetaDataService metaDataService;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	//@RolesAllowed({"ADMIN"})
	@PostMapping("/add")
	@ApiOperation(value = "Add metadata record in the DB")
	public ResponseEntity<PutItemResult> addMetaData(@Valid @RequestBody MetaDataBean metaDataBean) {
		PutItemResult putItemResult = metaDataService.addMetaData(metaDataBean);
			return new ResponseEntity<>(putItemResult, HttpStatus.OK);
	}

	// @Cacheable(key="#tableName",value="TestTable")
	@GetMapping("/{tableName}/{partitionkey}/{partitionkeyvalue}/{sortkey}/{sortkeyvalue}")
	@ApiOperation(value = "Get metadata record by providing, table, partition key and sort key", notes = "All 5 fields are mandatory.")
	public ResponseEntity<Map<String, Object>> getMetaData(@PathVariable("tableName") String tableName,
			@PathVariable("partitionkey") String partitionkey, @PathVariable("partitionkeyvalue") String partitionkeyvalue,
			@PathVariable("sortkey") String sortkey, @PathVariable("sortkeyvalue") String sortkeyvalue)
			throws RecordNotFound, AccessDenied {
		
		AccessValidationHelper.validateGetMetaData(tableName,partitionkey, partitionkeyvalue,sortkey,sortkeyvalue);
		Optional<Item> item;
		item = Optional.ofNullable(metaDataService.getMetaData(tableName, partitionkey, partitionkeyvalue,sortkey,sortkeyvalue));
		return Helper.formatMetaDataJson(tableName, partitionkey, partitionkeyvalue, sortkey, sortkeyvalue, item);
		
	}
	

	//@RolesAllowed({"ADMIN"})
	@GetMapping("/itemlist/{tableName}")
	@ApiOperation(value = "Get all Items from the table. (WIP)")
	public String getAllItemsFromTable(@PathVariable("tableName") String tableName) {
		return metaDataService.getAllItemsFromTable(tableName);
	}
	
	@GetMapping("/table-list")
	@ApiOperation(value = "List all tables from the DB")
	public ListTablesResult getTableList() {
		return metaDataService.getTableList();
	}

	// @CachePut(key="#tableName",value="TestTable")
	//@RolesAllowed({"ADMIN"})
	@PutMapping("/update")
	@ApiOperation(value = "Update metadata record by providing, table, partition key and sort key")
	public ResponseEntity<UpdateItemResult> updateMetaData(@Valid @RequestBody MetaDataBean metaDataBean) {
		Optional<UpdateItemResult> updateItemResult;
		try {
			updateItemResult = Optional.of(metaDataService.updateMetaData(metaDataBean));
			if (updateItemResult.isPresent()) {
				return new ResponseEntity<>(updateItemResult.get(), HttpStatus.OK);
			} else {
				throw new RecordNotFound(String.format("Record with partitionkey %s with value %s from table %s not found",
						metaDataBean.getPartitionKeyName(), metaDataBean.getPartitionKeyValue(),
						metaDataBean.getTableName()));
			}
		} catch (Exception e) {
			log.error("Error while updating item.", e);
			return new ResponseEntity<>(new UpdateItemResult(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// @CacheEvict(key="#tableName",value="TestTable")
	//@RolesAllowed({"ADMIN"})
	@DeleteMapping("/user/{tableName}/{partitionkey}/{value}")
	@ApiOperation(value = "Delete metadata record by providing, table, partition key and sort key")
	public ResponseEntity<DeleteItemResult> deleteMetaData(@PathVariable("tableName") String tableName,
			@PathVariable("partitionkey") String partitionkey, @PathVariable("value") String value) {
		Optional<DeleteItemResult> deleteItemResult;
		try {
			deleteItemResult = Optional.of(metaDataService.deleteMetaData(tableName, partitionkey, value));
			if (deleteItemResult.isPresent()) {
				return new ResponseEntity<DeleteItemResult>(deleteItemResult.get(), HttpStatus.OK);
			} else {
				throw new RecordNotFound(String.format("Record with partitionkey %s with value %s from table %s not found",
						partitionkey, value, tableName));
			}

		} catch (Exception e) {
			log.error("Error while deleting item.", e);
			return new ResponseEntity<>(new DeleteItemResult(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
}
