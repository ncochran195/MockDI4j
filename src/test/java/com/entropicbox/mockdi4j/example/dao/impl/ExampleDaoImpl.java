package com.entropicbox.mockdi4j.example.dao.impl;

import com.entropicbox.mockdi4j.example.dao.IExampleDao;

public class ExampleDaoImpl implements IExampleDao {

	@Override
	public String getExampleData() {
		return "Got data from ExampleDaoImpl";
	}

}
