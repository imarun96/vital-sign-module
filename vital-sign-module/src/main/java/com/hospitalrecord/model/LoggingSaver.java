package com.hospitalrecord.model;

import java.io.Serializable;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@DynamoDBTable(tableName = "loggeraop")
public class LoggingSaver implements Serializable {
	public static final String TABLE_NAME = "loggeraop";
	public static final String LOGID = "logId";
	public static final String METHODNAME = "methodName";
	public static final String DATETIME = "dateTime";
	public static final String CLASSNAME = "className";
	public static final String LOGSTATEMENT = "logStatement";
	@DynamoDBHashKey(attributeName = "logId")
	@DynamoDBAutoGeneratedKey
	private String logId;
	@DynamoDBAutoGeneratedKey
	private String methodName;
	@DynamoDBAutoGeneratedKey
	private String dateTime;
	@DynamoDBAutoGeneratedKey
	private String className;
	@DynamoDBAttribute
	private String logStatement;
}