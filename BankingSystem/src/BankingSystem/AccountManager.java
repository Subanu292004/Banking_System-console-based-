package BankingSystem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
	
	private Connection connection;
	private Scanner scanner;
	
	public AccountManager(Connection connection, Scanner scanner) {
		this.connection=connection;
		this.scanner=scanner;
	}

	public void debit_money(long acc_number) throws SQLException {
		System.out.println();
		System.out.println("Enter amount to be debited: ");
		int amount=scanner.nextInt();
		System.out.println("Pin: ");
		int pin=scanner.nextInt();
		
		try {
			connection.setAutoCommit(false);
			if(acc_number!=0) {
			String debit_query="select * from account_info where account_number=? and pin=?";
			PreparedStatement prepstat=connection.prepareStatement(debit_query);
			prepstat.setLong(1, acc_number);
			prepstat.setInt(2, pin);
			ResultSet resultSet=prepstat.executeQuery();
			
			if(resultSet.next()) {
				int balance=resultSet.getInt("balance");
				if(amount<=balance)
				{
					String debit_amount="update account_info set balance=balance-? where account_number=?";
					PreparedStatement prepstatement=connection.prepareStatement(debit_amount);
					prepstatement.setInt(1, amount);
					prepstatement.setLong(2,acc_number);
					int affectedRow=prepstatement.executeUpdate();
					if(affectedRow>0)
					{
						System.out.println("Rs."+amount+" has been debited successfully");
						debitDetails(acc_number,amount,balance);
						String balance_update="update admin_viewable_details set current_balance=? where account_number=?";
						PreparedStatement prepstat1=connection.prepareStatement(balance_update);
						prepstat1.setInt(1, balance);
						prepstat1.setLong(2, acc_number);
						prepstat1.executeUpdate();
						connection.commit();
						connection.setAutoCommit(true);
						return;
					}
					else
					{
						System.out.println("Transaction failed");
						connection.rollback();
						connection.setAutoCommit(true);
					}
				}
				else
					System.out.println("Insufficient Balance");
			}
			else
				System.out.println("Invalid Pin");
			}}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		connection.setAutoCommit(true);
	}

	private void debitDetails(long acc_number, int amount, int balance) {
		try {
			String insert_debit="insert into details(date,account_number,balance,type,cur_balance,amount_debitedorcredited)values(now(),?,?,?,?,?)";
			PreparedStatement prepstat=connection.prepareStatement(insert_debit);
			prepstat.setLong(1, acc_number);
			prepstat.setInt(2, balance);
			prepstat.setString(3,"debited");
			prepstat.setInt(4, balance-amount);
			prepstat.setInt(5, amount);
			prepstat.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		
	}

	private void credDetails(long acc_number, int amount, int balance) {
		try {
			String insert_credit="insert into details(date,account_number,balance,type,cur_balance,amount_debitedorcredited)values(now(),?,?,?,?,?)";
			PreparedStatement prepstat=connection.prepareStatement(insert_credit);
			prepstat.setLong(1, acc_number);
			prepstat.setInt(2, balance);
			prepstat.setString(3,"credited");
			prepstat.setInt(4, balance+amount);
			prepstat.setInt(5, amount);
			prepstat.executeUpdate();

				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
	}
 
	public void check_balance(long acc_number) {
		
		System.out.println();
		System.out.println("Enter pin: ");
		int pin=scanner.nextInt();
		try {
			String balance_query="select balance from account_info where account_number=? and pin=?";
			PreparedStatement prepstat=connection.prepareStatement(balance_query);
			prepstat.setLong(1,acc_number);
			prepstat.setInt(2, pin);
			ResultSet resultSet=prepstat.executeQuery();
			
			if(resultSet.next()) {
				int balance=resultSet.getInt("balance");
				
				System.out.println("Balance: "+balance);
			}
			else {
				System.out.println("Invalid pin");
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
	}

	public void credit_money(long acc_number) throws SQLException {
		scanner.nextLine();
        System.out.print("Enter Amount: ");
        int amount = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        int pin = scanner.nextInt();

        try {
            connection.setAutoCommit(false);
            if(acc_number != 0) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM account_info WHERE account_number = ? and pin = ? ");
                preparedStatement.setLong(1, acc_number);
                preparedStatement.setInt(2, pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                	int balance=resultSet.getInt("balance");
                    String credit_query = "UPDATE account_info SET balance = balance + ? WHERE account_number = ?";
                    PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
                    preparedStatement1.setInt(1, amount);
                    preparedStatement1.setLong(2, acc_number);
                    int rowsAffected = preparedStatement1.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Rs."+amount+" credited Successfully");
                        credDetails(acc_number,amount,balance);
                        String balance_update="UPDATE admin_viewable_details SET current_balance = ? where account_number = ?";
						PreparedStatement prepstat2=connection.prepareStatement(balance_update);
						prepstat2.setInt(1, balance);
						prepstat2.setLong(2, acc_number);
						prepstat2.executeUpdate();
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    } else {
                        System.out.println("Transaction Failed!");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                }else{
                    System.out.println("Invalid Security Pin!");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
		
	}

	public void transfer_money(long acc_number) throws SQLException {
		
		scanner.nextLine();
        System.out.print("Enter Receiver Account Number: ");
        int receiver_account_number = scanner.nextInt();
        System.out.print("Enter Amount: ");
        int amount = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        int pin = scanner.nextInt();
        try{
            connection.setAutoCommit(false);
            if(acc_number!=0 && receiver_account_number!=0){
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM account_info WHERE account_number = ? AND pin = ? ");
                preparedStatement.setLong(1, acc_number);
                preparedStatement.setInt(2, pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    int current_balance = resultSet.getInt("balance");
                    if (amount<=current_balance){

                        String debit_query = "UPDATE account_info SET balance = balance - ? WHERE account_number = ?";
                        String credit_query = "UPDATE account_info SET balance = balance + ? WHERE account_number = ?";

                        PreparedStatement creditPreparedStatement = connection.prepareStatement(credit_query);
                        PreparedStatement debitPreparedStatement = connection.prepareStatement(debit_query);

                        creditPreparedStatement.setInt(1, amount);
                        creditPreparedStatement.setLong(2, receiver_account_number);
                        debitPreparedStatement.setInt(1, amount);
                        debitPreparedStatement.setLong(2, acc_number);
                        int rowsAffected1 = debitPreparedStatement.executeUpdate();
                        int rowsAffected2 = creditPreparedStatement.executeUpdate();
                        if (rowsAffected1 > 0 && rowsAffected2 > 0) {
                            System.out.println("Transaction Successful!");
                            System.out.println("Rs."+amount+" Transferred Successfully");
                            
                            
                            String bal="select balance from account_info where account_number=?";
                            PreparedStatement prepstat2=connection.prepareStatement(bal);
                            prepstat2.setLong(1, acc_number);
                            ResultSet rows=prepstat2.executeQuery();
                            int balance=0;
                            if(rows.next())
                            	balance=rows.getInt("balance");
                            String transfer="insert into transfer_info(date,from_account,to_account,amount_transfered,balance_aftertransfer)values(now(),?,?,?,?)";
                            PreparedStatement prepstat3=connection.prepareStatement(transfer);
                            prepstat3.setLong(1, acc_number);
                            prepstat3.setLong(2, receiver_account_number);
                            prepstat3.setInt(3,amount);
                            prepstat3.setInt(4, balance);
                            prepstat3.executeUpdate();
                            
                            
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction Failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else{
                        System.out.println("Insufficient Balance!");
                    }
                }else{
                    System.out.println("Invalid Security Pin!");
                }
            }else{
                System.out.println("Invalid account number");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
	}

	public void printDetails(long account_number) throws SQLException {
		try {
		String print_query="Select date,amount_debitedorcredited,cur_balance,type from details where account_number=?";
		PreparedStatement prepstat2=connection.prepareStatement(print_query);
		prepstat2.setLong(1,account_number);
		ResultSet resultSet=prepstat2.executeQuery();
		String date="";
		while(resultSet.next()) {
			 date=resultSet.getString("date");
		    int amount1=resultSet.getInt("amount_debitedorcredited");
		     int cur_balance=resultSet.getInt("cur_balance");
		     String type=resultSet.getString("type");
		     System.out.println("On "+date+" Rs."+amount1+" has been "+type+" and your balance after being "+type+" is "+cur_balance);
		}
		String transfer_print="select date,from_account,to_account,amount_transfered,balance_aftertransfer from transfer_info where from_account=?";
		PreparedStatement prepstat3=connection.prepareStatement(transfer_print);
		prepstat3.setLong(1,account_number);
		ResultSet resultSet1=prepstat3.executeQuery();
		String date1="";
		while(resultSet1.next()) {
			 date1=resultSet1.getString("date");
		     long to_account=resultSet1.getLong("to_account");
		     int amount1=resultSet1.getInt("amount_transfered");
		     int balance_aftertransfer=resultSet1.getInt("balance_aftertransfer");
		     System.out.println("On "+date1+" Rs."+amount1+" has been transfered to "+to_account+" and your balance after transfering is "+balance_aftertransfer);
		}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void particularDate(String date, long account_number)throws SQLException {
		try {
			if(date==null||date.isEmpty())
			{
				System.out.println("Enter correct date");
			}
			else
			{
			String date_query="Select date,amount_debitedorcredited,cur_balance,type from details where account_number=? and date=?::date";
			PreparedStatement prepstat3=connection.prepareStatement(date_query);
			prepstat3.setLong(1,account_number);
			prepstat3.setString(2,date);
			ResultSet resultSet=prepstat3.executeQuery();
			
			while(resultSet.next()) {
				String date1=resultSet.getString("date");
			    int amount1=resultSet.getInt("amount_debitedorcredited");
			     int cur_balance=resultSet.getInt("cur_balance");
			     String type=resultSet.getString("type");
			     System.out.println("On "+date1+" Rs."+amount1+" has been "+type+" and your balance after "+type+" is "+cur_balance);
			
		     }
			}
			
			//else
				//System.out.println("Couldn't find any details on that date regarding credit or debit");
			
			
			String transfer_print="select date,from_account,to_account,amount_transfered,balance_aftertransfer from transfer_info where from_account=? and date=?::date";
			PreparedStatement prepstat3=connection.prepareStatement(transfer_print);
			prepstat3.setLong(1,account_number);
			prepstat3.setString(2, date);
			ResultSet resultSet1=prepstat3.executeQuery();
			String date1="";
			if(resultSet1.next()) {
			while(resultSet1.next()) {
				 date1=resultSet1.getString("date");
			     long to_account=resultSet1.getLong("to_account");
			     int amount1=resultSet1.getInt("amount_transfered");
			     int balance_aftertransfer=resultSet1.getInt("balance_aftertransfer");
			     System.out.println("On "+date1+" Rs."+amount1+" has been transfered to "+to_account+" and your balance after transfering is "+balance_aftertransfer);
			}
			}
			else
				System.out.println("Couldn't find any details on that date regarding transfer");
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
	}

	public void fromDateToDate(String date, String date1, long account_number)throws SQLException {
		
		try {
			if(date==null||date.isEmpty())
			{
				System.out.println("Enter correct date");
			}
			else
			{
			String dateToDate_query="Select date,amount_debitedorcredited,cur_balance,type from details where account_number=? and  date between ?::date and ?::date ";
			PreparedStatement prepstat3=connection.prepareStatement(dateToDate_query);
			prepstat3.setLong(1, account_number);
			prepstat3.setString(2,date);
			prepstat3.setString(3,date1);
			
			ResultSet resultSet=prepstat3.executeQuery();
			while(resultSet.next()) {
				String date2=resultSet.getString("date");
			    int amount1=resultSet.getInt("amount_debitedorcredited");
			     int cur_balance=resultSet.getInt("cur_balance");
			     String type=resultSet.getString("type");
			     System.out.println("On "+date2+" Rs."+amount1+" has been "+type+" and your balance after "+type+" is "+cur_balance);
			
		}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}

}



