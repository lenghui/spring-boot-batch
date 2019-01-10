package com.controller;

import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.model.People;

@RestController
public class HelloWorld {
	
	@GetMapping("/hello")
	public String hello() {
		return "hello";
	}
}
