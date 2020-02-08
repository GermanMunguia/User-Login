package Sql;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * must work on  
 * 	-make a new method to work as homepage, user is brought to home page after using function,
 * 		unique method for each possible without copying sql connection? 
 *  -ask for more info when making account
 *  -method that displays info after logging in successfully
 *  -sending error codes when trying to duplicate primary key
 * 	-delete account method(sql command)
 *  
 *  -GUI  
 */



public class login {
	
	
	public static boolean validate(Connection con, String username, String password) throws SQLException {
		
		if(username.equals("admin") && password.equals("admin")) {
			System.out.println("You have logged in as an admin");
			System.out.println("enter p to print, d to delete, l to logout");
			 admin(con);
		return false; 
		}
		
		String query = "SELECT * FROM login"; 
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(query);
		Boolean access = false; 
		
		while (rs.next()) { 
			if (rs.getString(1).compareTo(username) == 0 && rs.getString(2).compareTo(password) == 0) {
				System.out.println("YOUR ACOUNT"); 
				String email = rs.getString(4); 
				if (email.isBlank()) {
					email = "*no email provided*"; 
				}
				String table = rs.getString(1) + " : " + rs.getString(2) + " : " + rs.getString(3) + " : " + email + " : " + rs.getString(5);
				System.out.println(table); 
				access = true; 
				//have this sent to a method that displays myInfo/lets u change info. 
			}
			}
		if (access == true) return true; 
		System.out.println("ACCOUNT NOT FOUND, TRY AGAIN OR CREATE ACCOUNT"); 
		return false; 
	}
	
	public static void insert(Connection con, String query ) {
		try {
			Statement st = con.createStatement();
			st.executeUpdate(query);
			
			
			
		} catch (SQLException e) {
		
			e.printStackTrace();
		}
		return; 
	}
	
	
	//Must be in between 6-20 chars, Have atLeast one digit & one capital letter
	public static String createPassword() {	
		Scanner sc = new Scanner(System.in);
		String passw = sc.nextLine();
//		System.out.println(passw + "            PASS   > " + passw.length()); 
		if (passw.length() < 6) {
			System.out.println("Password is too short, try again"); 
			passw = createPassword(); 
			return passw; 
		}
		else if (passw.length() > 20) {
			System.out.println("Password is too long, try again"); 
			passw = createPassword(); 
		}	
		boolean upper = false; 
		boolean digit = false; 
		String newPassw = passw;  
		
		while(passw.length() > 0) {
			if( Character.isDigit(passw.charAt(0)) == true) {
				digit = true; 
			}
			else { 
				if (Character.isUpperCase(passw.charAt(0)) == true) {
					upper = true; 
				}
			}
			passw = passw.substring(1); 
		}
		if(upper == true && digit == true ) {
			return newPassw; 
		}
		else {
			System.out.println("Did not meet password requirements"); 
			passw = createPassword(); 
			return passw; 
		} 
	 
	}
	//must be less than 20 VARCHARs and be unique
	public static String createUsername(Connection con) throws SQLException {
		Scanner sc = new Scanner(System.in);
		String usern = sc.nextLine();
		
		if (usern.equals("admin")) {
			System.out.println("That username is reservated, try again"); 
			usern = createUsername(con); 
		}
		
		if (usern.length() > 20) {
			System.out.println("That username is too long, try again"); 
			usern = createUsername(con); 
		}
		
		if (usern.length() == 0) {
			System.out.println("You did not type a username, try again"); 
			usern = createUsername(con); 
		}
		
		if (usern.length() < 4) {
			System.out.println("That username is too short, try again"); 
			usern = createUsername(con); 
		}
		
		String query = "SELECT * FROM login"; 
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(query);
		
		while (rs.next()) { 
			if (rs.getString(1).equals(usern)) {
				System.out.println("That username was already taken, try another"); 
				usern = createUsername(con); 
			} 
		}
		
		return usern; 
	}
	
	//SocialS must have 4 digits
	public static String createSocial() {
		Scanner sc = new Scanner(System.in);
		String social = sc.nextLine();	
//		System.out.println(social + "  SOCIAL");
		if (social.length() != 4) {
			System.out.println("You did not type 4 digits, try again"); 
			social = createSocial(); 
		}
		//String returned
		String newSocial = social; 
		//check non-integers		
		while(social.length() > 0) {	
			if(Character.isDigit( social.charAt(0) ) == false) {
				System.out.println("You did not type your last 4 digits correctly, try again");	
				social = createSocial();  
			}
			social = social.substring(1); 
		}	
		return newSocial; 
	}
	
	public static String createDoB() {
		Scanner sc = new Scanner(System.in);
		String date = sc.nextLine();	
		
		if (date.length() != 10) {
			System.out.println("You did not enter a date with the correct format of YYYY-MM-DD, try again"); 
			date = createDoB();
			return date; 
		}
		String newDate = date; 
		
		int count = 0; 
		while (date.length() > 0) {
			//check for digits
			if(Character.isDigit( date.charAt(0) ) == false && count != 4 && count != 7) {
				System.out.println("You did not enter a date with the correct format of YYYY-MM-DD, try again");	
				date = createDoB();  
				return date; 
			}
			//check for dashes
			if (count == 4 || count == 7) {
				
				if (date.charAt(0) != '-') {
					System.out.println("You did not enter a date with the correct format of YYYY-MM-DD, try again");
					date = createDoB();  
					return date; 
				}
			}
			//check months <= 12
			if (count == 5) {
				String month = String.valueOf(date.charAt(0)); 
			    month += String.valueOf(date.charAt(1));
			    int value = Integer.parseInt(month); 
				if(value > 12) {
					System.out.println("You did not enter a date with the correct format of YYYY-MM-DD, the month specificed was greater than 12, try again");
					date = createDoB();  
					return date; 
				} 
			}	
			//check days <= 28, 28 was chosen since Feb only has 28 days, therefore into of 02/29||30 = sql crash.
			if (count == 8) {
				String day = String.valueOf(date.charAt(0)); 
			    day += String.valueOf(date.charAt(1));
			    int value = Integer.parseInt(day); 
				if(value > 28) {
					System.out.println("You did not enter a date with the correct format of YYYY-MM-DD, the day specified was greater than 28, try again");
					date = createDoB();  
					return date; 
				} 
			}	
			if (count == 0) {
				String old = String.valueOf(date.charAt(0)); 
			    old += String.valueOf(date.charAt(1));
			    old += String.valueOf(date.charAt(2));
			    old += String.valueOf(date.charAt(3));
			    int value = Integer.parseInt(old); 
			    if (value < 1900) System.out.println("WOW u are old."); 
			    if (value > 2019) System.out.println("You are from the future! This program was made in 2019.");
			   
			}
			
		count++;
		date = date.substring(1); 
		}
		
		
		
		return newDate; 
	}
	
	//admin allows to delete any account with X user name or age range, display all data, 
	public static void admin(Connection con) throws SQLException {
		Scanner sc = new Scanner(System.in);
		String choice = sc.nextLine(); 
		choice = choice.toLowerCase(); 
		
		//print
		if (choice.equals("p")) {
			//change to choice = adminPrint
			adminPrint(con);  
			return; 
		}
		//delete
		if (choice.equals("d")) {
			adminDelete(con); 
			return; 
		}
		//logout
		if (choice.equals("l")) {  
			System.out.println("Logging out"); 
			return; 
		}
		
		else {
			System.out.println("You did not make any of the choices provided, try again");
			 admin(con);
			 return; 
		}
	}
	
	public static void adminPrint(Connection con) throws SQLException {
		
		String query = "SELECT * FROM login"; 
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(query);
		Boolean access = false; 
		
		newLine(); 
		System.out.println("Username : Password : Social : Email : Date of Birth");
		
		while (rs.next()) {  
			System.out.println();
			String email = rs.getString(4); 
			if (email.isBlank()) {
				email = "*no email provided*"; 
			}
			String table = rs.getString(1) + " : " + rs.getString(2) + " : " + rs.getString(3) + " : " + email + " : " + rs.getString(5);
			System.out.println(table);  
		}
		
		
		System.out.println("enter p to print, d to delete, l to logout");
		 admin(con);
		 return; 
	}
	
	//delete accounts based on attributes
	public static void adminDelete(Connection con) throws SQLException {
		
		System.out.println("press u to delete based of username, e to delete all acounts under the age of 18 (2001-01-01), c to cancel");	
		Scanner sc = new Scanner(System.in);
		String input = sc.nextLine();
			if(input.equals("u")) {
				System.out.println("enter the username of the account you want to delete");	
				Scanner scUsern = new Scanner(System.in);
				String username = scUsern.nextLine();
				
				String query = "DELETE FROM login WHERE username = '" + username + "' ;";
				Statement st = con.createStatement();
				int count = st.executeUpdate(query);
				//count = 0 if nothing was affected
				if(count == 0) {
					System.out.println("There is no account with that name, no account was deleted");
					adminDelete(con); 
					return; 
				}
				else {
					System.out.println("You deleted the account with username : " + username);
					System.out.println("enter p to print, d to delete, l to logout");
					admin(con); 
					return; 
				}
				
			}
			else if(input.equals("e")) {
				String query = "DELETE FROM login WHERE Dob > '2001-01-01' ;";
				Statement st = con.createStatement();
				int count = st.executeUpdate(query);
				System.out.println("You deleted " + count + " underage accounts");
				System.out.println("enter p to print, d to delete, l to logout");
				admin(con);
				return; 
			}
			else if(input.equals("c")) {
				System.out.println("enter p to print, d to delete, l to logout");
				admin(con);
				return; 
			}

			else {
				System.out.println("You did not input one of the options, try again"); 
				adminDelete(con); 
				return; 
			}
	}
	
	public static void newLine() {
		System.out.println();
		System.out.println();
		System.out.println();
	}
	//display info after validated/inserted
	public static void loggedIn(Connection con, String username, String password) throws SQLException {
		
		
			System.out.println("l to log out, e to edit info");	
			Scanner sc = new Scanner(System.in);
			String logoutEdit = sc.nextLine();
			logoutEdit = logoutEdit.toLowerCase(); 
			if (logoutEdit.equals("l")) {
				System.out.println("You have logged out");	
				return;
			}
			else if (logoutEdit.equals("e")) {
				System.out.println("Info is being edited");
				userEdit(con, username, password); 
				return; 
			}
			else {
			System.out.println("You did not input a correct option, try again");
				loggedIn(con, username,password); 
			}
		
		return; 
	}
	
	public static void userEdit(Connection con, String username, String password) throws SQLException {
		System.out.println("p to edit password, s for social security, e for email, d for date of birth, c to cancel");	
		Scanner sc = new Scanner(System.in);
		String input = sc.nextLine();
		
		if (input.equals("p")) {
			System.out.println("Enter a new password, it Must contain at least one uppercase letter and digit");	
			String newPassw = createPassword(); 
			System.out.println(newPassw);
 			String query = "UPDATE login SET password='" + newPassw + "' WHERE username='" +username + "'";
			Statement st = con.createStatement();
			st.executeUpdate(query);
			System.out.println("Your new passoword is : " + newPassw); 
			password = newPassw; 
		}
		
		else if (input.equals("s")) {
			System.out.println("Enter the last four digits of your correct social security number");	
			
			String newSocial = createSocial(); 
			System.out.println(newSocial);
 			String query = "UPDATE login SET socialS='" + newSocial + "' WHERE username='" +username + "'";
			Statement st = con.createStatement();
			st.executeUpdate(query);
			System.out.println("Your social security is : " + newSocial);
		}
		
		else if (input.equals("e")) {	
			System.out.println("Enter your new email");	
			Scanner scEmail = new Scanner(System.in);
			String newEmail = scEmail.nextLine(); 
 			String query = "UPDATE login SET email='" + newEmail + "' WHERE username='" +username + "'";
			Statement st = con.createStatement();
			st.executeUpdate(query);
			System.out.println("Your new email is : " + newEmail);	
		}
		
		else if (input.equals("d")) {
			System.out.println("Enter your correct date of Birth, YYYY-MM-DD Format");	
			
			String newDate = createDoB(); 
			System.out.println(newDate);
 			String query = "UPDATE login SET DoB='" + newDate + "' WHERE username='" +username + "'";
			Statement st = con.createStatement();
			st.executeUpdate(query);
			System.out.println("Your new date of birth is : " + newDate);
	
		}
		//go to validate
		else if(input.equals("c")) {
			validate(con,username,password); 
			loggedIn(con,username,password); 
			return; 
		}
		
		//try again, call this method
		else {
			System.out.println("You did not make once of the choices, try again");
		}
		while (true) {
		System.out.println("e to make another edit, r to return to previous page, l to logout");
		Scanner scRepeat = new Scanner(System.in);
		String inputRepeat = scRepeat.nextLine();
		inputRepeat = inputRepeat.toLowerCase(); 
		
		
		if (inputRepeat.equals("e")) {
			userEdit(con, username, password); 
			break;
		}
		
		else if (inputRepeat.equals("r")) {
			validate(con, username, password);
			loggedIn(con,username,password); 
			break;
		}
		
		else if (inputRepeat.equals("l")) {
		return; 
		}
		else {
			System.out.println("You did not make a choice, try again"); 
		}
		}
	}
	

	
	public static void main(String[] args) throws Exception {
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/project1"; 
		String username = "root"; 
		String password = "RUpassw";
		
		Class.forName(driver);
		Connection con = DriverManager.getConnection(url,username,password);
//		Statement st1 = con.createStatement();
		
		System.out.println("l to log in, c to create account, q to quit");
		Scanner sc = new Scanner(System.in);
		String choice = sc.nextLine(); 
		choice = choice.toLowerCase(); 
		
	// 	chose login option 
		if (choice.equals("l")) {
			
			System.out.println("enter YOUR username");		
			Scanner sc1 = new Scanner(System.in);
			String usern = sc1.nextLine();
			System.out.println("enter YOUR password");		
			Scanner sc2 = new Scanner(System.in);
			String passw = sc2.nextLine();
			
			boolean access = validate(con, usern, passw);
			//if fails
			if (access == false) {
				login.main(args);
			}
			else {
			//if logs in 
				loggedIn(con, usern,passw);
				login.main(args);
			}
		}
	//  chose create option 	
		if (choice.equals("c")) {
		//username
		System.out.println("enter a username, must be at least 4 characters long");		
		String usern = createUsername(con); 
		//password
		System.out.println("enter a password, Must contain at least one uppercase letter and digit");	
		String passw = createPassword(); 
		//social
		System.out.println("enter the last 4 digits of your Social Security number");
		String socialS = createSocial();
		//email
		System.out.println("enter your email, press enter to skip (Optional)");		
		Scanner sc4 = new Scanner(System.in);
		String email = sc4.nextLine();
		//DoB
		System.out.println("enter your date of birth, Must use format YYYY-MM-DD (Including Dashes)");		
		String Dob = createDoB(); 
		
		String querySc = "INSERT INTO login VALUES('"+usern+"','"+passw+"','"+socialS+"','"+email+"','"+Dob+"')";
		insert(con, querySc);
		System.out.println("Your account was added, you can now log in at any time");
		login.main(args);
		}		
		
		//quit
		if (choice.equals("q")) {
			System.out.println("SHUTTING DOWN");
			return; 
		}
		//no correct choice
		if (!choice.equals("q") && !choice.equals("l") && !choice.equals("c") ) {
			System.out.println("You did not enter one of the choices, try again");
			login.main(args);
		}
	}

}
