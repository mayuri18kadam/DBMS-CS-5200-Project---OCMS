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
				this.id=id;
				String name = rs.getString(1);
				String address= rs.getString(2);
				
				this.name=name;
				CommonFunctions cmn = new CommonFunctions();
				String addressSplit[] = cmn.splitAddress(address);
				int zip = Integer.parseInt(addressSplit[3]);
				this.address = new Address(addressSplit[0], addressSplit[1], addressSplit[2],zip);
				
			}
		}
		
	}

	public void listCourses(Connection con, int id) throws SQLException
	{
		String query="Select c.id, c.name, cp.taughtby from course c, professorcourse cp where c.offeredby=? and c.id=cp.teaches order by c.id";
		PreparedStatement p = con.prepareStatement(query);
		p.setInt(1, id);
		
		ResultSet rs = p.executeQuery();
		
		if(!rs.isBeforeFirst())
		{
			System.out.println("No courses offered by "+this.name+" at this time");
		}
		else
		{
			System.out.println(this.name+" offers the following courses at this time ");
			System.out.println("id\t"+"name\t"+"professor");
			while(rs.next())
			{
				System.out.print(rs.getInt(1)+"\t");
				System.out.print(rs.getString(2)+"\t");
				
				int pid= rs.getInt(3);
			    PreparedStatement p2 = con.prepareStatement("Select name from person where id=?" );
			    p2.setInt(1, pid);
			    ResultSet r2 = p2.executeQuery();
			    while(r2.next())
			    {
			    	System.out.print(r2.getString(1)+"\n");
			    	
			    }
				
			}
			
		}
		
	}
	
	public void addCourse(Connection conn, int id)
	{
		
	}
	
	
	// ONLY FOR UNIT TESTING OF UNIVERSITY FUNCTIONALITY
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
		p.listCourses(c, 1);
		
		
	}
}
