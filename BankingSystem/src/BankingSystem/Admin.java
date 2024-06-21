package BankingSystem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Admin {

	private Connection connection;
	private Scanner scanner;
	Account account=new Account(connection,scanner);

	public Admin(Connection connection, Scanner scanner) {
		this.connection=connection;
		this.scanner=scanner;
	}

	public String login() throws SQLException{
		scanner.nextLine();
		System.out.println("Email: ");
		String email=scanner.nextLine();
		System.out.println("Password: ");
		String passkey=scanner.nextLine();
		String admin_query="SELECT * FROM admin_info where email=? and password=?";
		try {
			PreparedStatement prepstat=connection.prepareStatement(admin_query);
			prepstat.setString(1,email);
			prepstat.setString(2,passkey);
			ResultSet resultSet=prepstat.executeQuery();
			if(resultSet.next())
				print_viewDetails();
			else
				System.out.println("Invalid mail or password");
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void print_viewDetails() {
		String admin_viewquery="SELECT * FROM admin_viewable_details;";
		try {
			PreparedStatement prepstat=connection.prepareStatement(admin_viewquery);
			ResultSet result=prepstat.executeQuery();
			int i=1;
			while(result.next())
			{
				String name=result.getString("name");
				String date=result.getString("acc_opendate");
				String email=result.getString("email");
				String account_number=result.getString("account_number");
				String balance=result.getString("current_balance");
				System.out.println(i+". Name: "+name+" Email: "+email+" Account_number: "+account_number+" Account opened on "+date+" Balance: "+balance);
				i++;
			}
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	

}
