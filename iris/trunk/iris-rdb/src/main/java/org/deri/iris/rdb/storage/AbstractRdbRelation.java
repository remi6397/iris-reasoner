package org.deri.iris.rdb.storage;

import java.sql.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractRdbRelation implements IRdbRelation {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private Connection connection;

	public AbstractRdbRelation(Connection connection) {
		this.connection = connection;
	}

	protected Connection getConnection() {
		return connection;
	}

}
