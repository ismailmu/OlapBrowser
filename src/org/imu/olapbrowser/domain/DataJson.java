package org.imu.olapbrowser.domain;

import java.io.Serializable;
import java.util.List;

public class DataJson implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<DataField> field;
	private List<List<Object>> content;
	private List<List<Object>> contentAll;
	private Boolean isValid;
	private String message;
	public DataJson(){}
	
	public DataJson(List<DataField> field,List<List<Object>> content, List<List<Object>> contentAll) {
		this.field = field;
		this.content = content;
		this.contentAll = contentAll;
	}

	public List<DataField> getField() {
		return field;
	}

	public void setField(List<DataField> field) {
		this.field = field;
	}

	public List<List<Object>> getContent() {
		return content;
	}

	public void setContent(List<List<Object>> content) {
		this.content = content;
	}

	public List<List<Object>> getContentAll() {
		return contentAll;
	}

	public void setContentAll(List<List<Object>> contentAll) {
		this.contentAll = contentAll;
	}

	public Boolean IsValid() {
		return isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}