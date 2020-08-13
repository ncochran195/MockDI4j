package com.entropicbox.mockdi4j.mock.example;

import com.entropicbox.mockdi4j.example.dao.IExampleDao;

public class MockExampleDao implements IExampleDao
{

	@Override
	public String getExampleData() {
		return "Got data from MockExampleDao";
	}
	
}
