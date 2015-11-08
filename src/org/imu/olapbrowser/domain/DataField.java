package org.imu.olapbrowser.domain;

import java.io.Serializable;

public class DataField implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String columnType;
	private String columnName;
	
	public DataField() {}

	public DataField(String columnType, String columnName) {
		this.columnType = columnType;
		this.columnName = columnName;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
}