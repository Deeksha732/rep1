package dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBHandler {
	public static Connection connect() {
		Connection connection = null;
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/HolidayPackage","postgres","deeksha732");
		}catch(Exception e) {
			System.out.println(e);
		}
		return connection;
	}
	
	
	

	

}
