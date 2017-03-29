import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.*;


public class TestConnection 
{
		// JDBC driver name and database URL

	   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:postgresql://postgres.c9mcz4kq3yti.us-west-2.rds.amazonaws.com:5432/OCMS";

	   //  Database credentials
	   static final String USER = "postgres";
	   static final String PASS = "Akshay1!";
	   public static void main( String args[] ) throws SQLException
	     {
	       Connection c = null;
	       try 
	       {
	         Class.forName("org.postgresql.Driver");
	        
	         
	         c = DriverManager
	                 .getConnection(DB_URL,USER,PASS);
	         
	         System.out.println("Opened database successfully");

	         
	         PreparedStatement p = c.prepareStatement("Select * from person");
	         
	         ResultSet rs = p.executeQuery();
	         
	         
	         
	         while(rs.next())
	         {
	        	 System.out.println(rs.getInt(1));
	        	 System.out.println(rs.getString(2));
	        	 System.out.println(rs.getString(3));
	        	 System.out.println(rs.getString(4)+"\n");
	        	 
	         }
	         
	         LoginPage page = new LoginPage();
	         page.login(c);
	        
	        
	        
	       } catch ( Exception e ) 
	       {
	         System.err.println( e.getClass().getName()+": "+ e.getMessage() );
	         System.exit(0);
	       }
	       finally
	       {
	    	   c.close();
	       }
	       
	     }
	}

