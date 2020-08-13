package com.entropicbox.mockdi4j.example.controller;

import com.entropicbox.mockdi4j.example.service.ExampleService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExampleController {

	private final ExampleService exampleService;
	
	public String getExampleData()
	{
		return exampleService.getExampleData();
	}
}
