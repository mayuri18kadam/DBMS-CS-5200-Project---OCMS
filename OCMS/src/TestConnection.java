import java.sql.*;
import java.util.Scanner;

public class TestConnection 
{
   // JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:postgresql://postgres.c9mcz4kq3yti.us-west-2.rds.amazonaws.com:5432/OCMS";

   // Database credentials
   static final String USER = "postgres";
   static final String PASS = "Akshay1!";
   
   public static void main( String args[] ) throws SQLException
   {
	   @SuppressWarnings("resource")
	   Scanner s = new Scanner(System.in);
	   Connection c = null;
	   try 
	   {
		 Class.forName("org.postgresql.Driver");	         
		 c = DriverManager.getConnection(DB_URL,USER,PASS);
		 
		 loginRegister(c);
		 
		 	   } catch ( Exception e ) 
		   {
			   System.err.println(e.getClass().getName()+": "+ e.getMessage());
		       System.exit(0);
		   }
	       finally
	       {
	    	   c.close();
	       }
	 }
   
   public static void loginRegister(Connection con) throws Exception
   {
	   Scanner s = new Scanner(System.in);
	   System.out.println("Enter your choice \n1) Login \n2) Register \n3) Exit \n");
		 int choice = s.nextInt();
		 switch(choice)
		 {
		 case 1:
			 LoginPage lPage = new LoginPage();
			 lPage.login(con);
		     break;
		 case 2:
			 RegisterPage rPage = new RegisterPage();
			 rPage.register(con);
		     break;
		 case 3:
			 System.out.println("You are exiting the system.");
			 System.exit(0);
		 default:
			 System.out.println("Sorry you have entered the wrong choice!");
		     } 
   }
}