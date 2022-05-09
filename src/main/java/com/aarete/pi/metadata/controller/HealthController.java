package com.aarete.pi.metadata.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("health")
@RestController
@Api(tags = "Just to check health check", value = "Health Check")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HealthController {

	@GetMapping("/health-check")
	@ApiOperation(value = "This is just to check if meta-data service is up.")
	public String getHealthCheck() {
		return "Its working fine.";
	}
	
	@GetMapping("/secure-page")
	@ApiOperation(value = "Check page security",notes = "You need to login with the AArete account.")
	public String getSecuredPage() {
		return "Secured Page.";
	}
	
}
