package com.aarete.pi.metadata.bean;

import java.util.List;

import lombok.Data;

@Data
public class UserDetailsBean {

	private String userId;//Email id
	private List<String> roleList;
	private String name;
		
}
