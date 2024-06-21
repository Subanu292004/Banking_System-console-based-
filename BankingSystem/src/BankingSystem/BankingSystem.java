package BankingSystem;
import java.util.*;
import java.sql.*;
import java.text.ParseException;
public class BankingSystem {
	private static final String url="jdbc:postgresql://localhost:5432/login";
	private static final String username="postgres";
	private static final String password="102@2023";
	public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException {
        try{
            Class.forName("com.mysql.jdbc.Driver");
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try { 
        	Connection connection=DriverManager.getConnection(url,username,password);
        	Scanner scanner=new Scanner(System.in);
        	User user=new User(connection,scanner);
        	Account account=new Account(connection,scanner);
        	AccountManager accountManager=new AccountManager(connection,scanner);
        	Admin admin=new Admin(connection,scanner);
            
        	String email;
        	long account_number;
        	while(true) {
        	System.out.println("Welcome to Banking System");
        	System.out.println();
        	System.out.println("1. Register");
        	System.out.println("2. Login");
        	System.out.println("3. Exit");
        	int choice=scanner.nextInt();
        	switch(choice)
        	{
        	case 1:
        		user.register();
        		break;
        		
        	case 2:
        		System.out.println();
        		System.out.println("1. User");
        		System.out.println("2. Admin");
        		int choice2=scanner.nextInt();
        		if(choice2==1)
        		{
        		email=user.login();
        		if(email!=null)
        		{
        		System.out.println();
        		System.out.println("User logged in!!");
        		if(!account.account_exist(email))
        		{
        			System.out.println();
        			System.out.println("1. Open bank account");
        			System.out .println("2. Exit");
        			if(scanner.nextInt()==1)
        			{
        				account_number=account.open_account(email);
        				System.out.println();
        				System.out.println("Your account has been created successfully");
        				System.out.println("Your acccount number is: "+account_number);
        			}
        			else
        		      break;
        		}
        			
        			account_number=account.getAccountNumber(email);
        			int choice1=0;
        			while(choice!=6) {
        				System.out.println();
        			System.out.println("1. Debit money");
        			System.out.println("2. Credit money");
        			System.out.println("3. Transfer money");
        			System.out.println("4. Check balance");
        			System.out.println("5. Print statement");
        			System.out.println("6. Exit");
        			 choice1=scanner.nextInt();
        			
        			switch(choice1) {
        			case 1:
        				 accountManager.debit_money(account_number);
        				 break;
        			case 2:
        				accountManager.credit_money(account_number);
        				break;
        			case 3:
        				accountManager.transfer_money(account_number);
        				break;
        			case 4:
        				accountManager.check_balance(account_number);
        				break;
        			case 5:
        				System.out.println("1. Get details on particular date");
        				System.out.println("2. Get details from a particular date to a particular date");
        				System.out.println("3. Get complete detail");
        				int opt=scanner.nextInt();
        				if(opt==1)
        				{
        					scanner.nextLine();
        					System.out.println("Enter date: eg(2024-02-14) yyyy-mm-dd");
        					String date=scanner.nextLine();
        					accountManager.particularDate(date,account_number);
        				}
        				else if(opt==2)
        				{
        					scanner.nextLine();
        					System.out.println("Enter From date: eg(2024-02-14) yyyy-mm-dd");
        					String date=scanner.nextLine();
        					System.out.println("Enter Till date: ");
        					String date1=scanner.nextLine();
        					accountManager.fromDateToDate(date,date1,account_number);
        				}
        				else if(opt==3)
        				accountManager.printDetails(account_number);
        				else
        					System.out.println("Invalid choice");
        				break;
        			case 6:
        				System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!");
                        System.out.println("Exiting System!");
                        return;
        				
        			default:
        				System.out.println("Invalid choice");
        				break;
        			}
        			}
        			
        		}
        		else
        			System.out.println("Incorrect mail or password");
        		}
        		else if(choice2==2)
        		{
        			
        			email=admin.login();
        		}
        	
        	case 3:
        		System.out.println();
        		System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!");
                System.out.println("Exiting System!");
                return;
        		
        	}
        	}
        } catch(SQLException e){
        	e.printStackTrace();
        }
	}
	
}
