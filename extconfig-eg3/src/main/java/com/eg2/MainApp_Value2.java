package com.eg2;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

public class MainApp_Value2 {

	public static void main(String[] args) throws SQLException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.scan("com.eg2");
		context.refresh();
		System.out.println("Refreshing the spring context");
		DBConnection dbConnection = context.getBean(DBConnection.class);

		dbConnection.printDBConfigs();
		
		// close the spring context
		context.close();
	}
}


