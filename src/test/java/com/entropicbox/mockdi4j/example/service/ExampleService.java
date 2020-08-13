package com.entropicbox.mockdi4j.example.service;

import com.entropicbox.mockdi4j.example.dao.IExampleDao;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExampleService {

	private final IExampleDao exampleDao;
	
	public String getExampleData() {
		return exampleDao.getExampleData();
	}

}
