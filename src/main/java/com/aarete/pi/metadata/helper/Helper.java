package com.aarete.pi.metadata.helper;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.aarete.pi.metadata.exception.RecordNotFound;
import com.aarete.pi.metadata.util.JsonParser;
import com.amazonaws.services.dynamodbv2.document.Item;

public class Helper {

	private Helper() {
		super();
	}

	public static ResponseEntity<Map<String, Object>> formatMetaDataJson(String tableName, String partitionkey,
			String partitionkeyvalue, String sortkey, String sortkeyvalue, Optional<Item> item) throws RecordNotFound {
		if (item.isPresent()) {
			Map<String, Object> stringObjectMap = item.get().asMap();
			Map<String, Object> result = stringObjectMap.entrySet().stream()
					.filter(entry -> (!partitionkey.equals(entry.getKey()) && !sortkey.equals(entry.getKey())))
					.collect(Collectors.toMap(e -> e.getKey(), e -> {
						// if value is string, remove escape characters and convert to json string
						if (e.getValue() instanceof String) {
							return JsonParser.convertToJson((String) e.getValue());
						}
						// for non-string values return same value.
						return e.getValue();
					}));
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			throw new RecordNotFound(String.format("Record for partitionkey %s = %s, sortkey %s = %s from table %s not found.",
					partitionkey, partitionkeyvalue,sortkey,sortkeyvalue, tableName));
		}
	}
}
