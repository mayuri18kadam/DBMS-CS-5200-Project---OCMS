import java.sql.*;

public class UniversityPage 
{
	int id;
	Address address;
	String name;
	public void start(Connection con, int id) 
	{
		
		
		
	}
	
	public void buildUniversity(Connection con, int id) throws SQLException
	{
		
		String query = "Select name, adrress from university where id = ?";
		PreparedStatement p = con.prepareStatement(query);
		p.setInt(1, id);
		
		ResultSet rs = p.executeQuery();
		
		if(!rs.isBeforeFirst())
		{
			System.out.println("Invalid id");
			return;
		}
		else
		{
			while(rs.next())
			{
				String name = rs.getString(1);
				String address= rs.getString(2);
				
				System.out.println(name);
			}
		}
		
	}

	public static void main(String args[]) throws ClassNotFoundException, SQLException
	{
		UniversityPage p = new UniversityPage();
		
			 final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
		     final String DB_URL = "jdbc:postgresql://postgres.c9mcz4kq3yti.us-west-2.rds.amazonaws.com:5432/OCMS";

		   // Database credentials
		    final String USER = "postgres";
		    final String PASS = "Akshay1!";
		    
		    Connection c = null;
		         Class.forName("org.postgresql.Driver");	         
		         c = DriverManager.getConnection(DB_URL,USER,PASS);
		      

		p.buildUniversity(c, 1);
	}
}
