package BankingSystem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
 
class User {

	private Connection connection;
	private Scanner scanner;

	public User(Connection connection, Scanner scanner) {
		this.connection=connection;
		this.scanner=scanner;
	}

	public void register() {
		scanner.nextLine();
		System.out.println("Name: ");
		String name=scanner.nextLine();
		System.out.println("Email: ");
		String email=scanner.nextLine();
		System.out.println("Password: ");
		String password=scanner.nextLine();
		if(user_exist(email))
		{
			System.out.println("User Already exist in this mail id");
			return;
		}
		String register_query="INSERT INTO users_info(name,email,password) values(?,?,?);";
		try {
			PreparedStatement prepstat=connection.prepareStatement(register_query);
			prepstat.setString(1,name);
			prepstat.setString(2, email);
			prepstat.setString(3, password);
			int affectedRows=prepstat.executeUpdate();
			if(affectedRows>0)
				System.out.println("Registration successful");
			else
				System.out.println("Registration failed");
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
	}

	private boolean user_exist(String email) {
		String query="SELECT * from users_info where email=?;";
		try {
			PreparedStatement prepstat=connection.prepareStatement(query);
			prepstat.setString(1, email);
			ResultSet resultSet=prepstat.executeQuery();
			if(resultSet.next())
				return true;
			else
				return false;
		}
		catch(SQLException e) {
		    e.printStackTrace();
		}
		return false;
	}

	public String login() {
		scanner.nextLine();
		System.out.println("Email: ");
		String email=scanner.nextLine();
		System.out.println("Password: ");
		String password=scanner.nextLine();
		String login_query="SELECT * FROM users_info where email=? and password=?";
		try {
			PreparedStatement prepstat=connection.prepareStatement(login_query);
			prepstat.setString(1, email);
			prepstat.setString(2, password);
			ResultSet resultSet=prepstat.executeQuery();
			if(resultSet.next())
				return email;
			else
				return null;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return null;
		
	}

}
