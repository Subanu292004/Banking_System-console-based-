 package BankingSystem;
import java.util.*;
import java.sql.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
public class Account {
	
	private Connection connection;
	private Scanner scanner;
	

	AccountManager accountManager=new AccountManager(connection,scanner);
	public Account(Connection connection, Scanner scanner) {
		this.connection=connection;
		this.scanner=scanner;
	}
	
	public long open_account(String email) throws ParseException {
		
		if(!account_exist(email)) {
			String open_acc_query="insert into account_info(account_number,name,email,balance,pin,aadhaar,phn_no,dob,account_type) values(?,?,?,?,?,?,?,?,?)";
			scanner.nextLine();
			System.out.println("Enter full name: ");
			String name=scanner.nextLine();
			System.out.println("Enter date of birth: ");
			String dob1=scanner.nextLine();
			LocalDate dob = LocalDate.parse(dob1);
			
			
			System.out.println("Enter your Aadhaar number:  ");
			long aadhaar=scanner.nextLong();
			System.out.println("Enter your phone number:  ");
			long phn_no=scanner.nextLong();
			System.out.println("Account type:  ");
			System.out.println("1. Savings  ");
			System.out.println("2. Current  ");
			System.out.println("3. Minor  ");
			int opt=scanner.nextInt();
			String account_type="";
			
			if(opt==1)
				account_type="Savings";
			else if(opt==2)
				account_type="Current";
			else if(opt==3)
				account_type="Current";
			//else
			//	System.out.println("Enter valid choice");
			
			
			System.out.println("Enter initial amount: ");
			int balance=scanner.nextInt();
			System.out.println("Enter 4 digit pin: ");
			int pin=scanner.nextInt();
			

			String admin_view_query="insert into admin_viewable_details(name,email,account_number,acc_openDate) values(?,?,?,now())";
			try {
				long account_number=generate_accountno();
				PreparedStatement prepstat=connection.prepareStatement(open_acc_query);
				prepstat.setLong(1, account_number);
				prepstat.setString(2, name);
				prepstat.setString(3, email);
				prepstat.setInt(4, balance);
				prepstat.setInt(5, pin);
				prepstat.setLong(6, aadhaar);
				prepstat.setLong(7, phn_no);
				prepstat.setObject(8, dob);
				//prepstat.setString(8, dob1);
				prepstat.setString(9, account_type);
				
				
				PreparedStatement prepstat1=connection.prepareStatement(admin_view_query);
				prepstat1.setLong(3, account_number);
				prepstat1.setString(1, name);
				prepstat1.setString(2, email);
				int affectedRow=prepstat.executeUpdate();
				prepstat1.executeUpdate();
				if(affectedRow>0)
				   return account_number;
				else
				   throw new RuntimeException("Account Creation failed!!");
			   }
			catch(SQLException e){
				e.printStackTrace();
			}
			
		}
		 throw new RuntimeException("Account Already Exist");
		
	}

	private long generate_accountno() {
		
		try {
			
		Statement statement=connection.createStatement();
		ResultSet resultSet=statement.executeQuery("select account_number from account_info order by account_number desc limit 1");
		if(resultSet.next())
		{
			long acc_no=resultSet.getLong("account_number");
			return acc_no+1;
		}
		else
			return 1000000100;
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public boolean account_exist(String email) {
		
		String accountno_query="select account_number from account_info where email=?";
		try {

			PreparedStatement prepstat=connection.prepareStatement(accountno_query);
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

	public long getAccountNumber(String email) {
		
		String acc_query="select account_number from account_info where email=?";
		try {
			PreparedStatement prepstat=connection.prepareStatement(acc_query);
		    prepstat.setString(1, email);	
		    ResultSet resultSet=prepstat.executeQuery();
		    while(resultSet.next())
		    	return resultSet.getLong("account_number");
		    
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	
	

}
