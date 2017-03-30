import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class UniversityPage 
{
	public enum designation {Dean,Professor,Associate_Professor,Lecturer,Visiting_Scholar,Director,Associate_Director,TA,RA,GA,Part_time_faculty,Contractor}
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
	
	public void addProfessor(Connection conn, int id) throws SQLException
	{
		
		PreparedStatement p = conn.prepareStatement("INSERT INTO public.professor( id, worksfor, designation) VALUES (10, 1, CAST(? AS designation));");
		
		p.setString(1, "Associate Director");
		p.executeUpdate();
	}
	
	public void addCourse(Connection conn, int id) throws SQLException
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter name for course");
		
		String name = sc.next();
		
		System.out.println("Enter description for course");
		String description = sc.next();
		
		System.out.println("\nSelect a professor to teach this course ");
		System.out.println("\nHere are a list of professors who are available ");
				
		String query="Select id from professor where worksfor=? order by id";
		PreparedStatement p = conn.prepareStatement(query);
		p.setInt(1, id);
		
		ArrayList<Integer> professors = new ArrayList<>();
		
		ResultSet rs = p.executeQuery();
		if(!rs.isBeforeFirst())
		{
			System.out.println("No Professors available to take up new course");
			return;
		}
		else
		{
			while(rs.next())
			{
				professors.add(rs.getInt(1));
		
			}
		}	
		
		for(int i=0;i<professors.size(); i++)
		{
			PreparedStatement p2 = conn.prepareStatement("Select name, designation from person p, professor p1 where p.id=? and p.id = p1.id");
			p2.setInt(1, professors.get(i));
			
			ResultSet r2 = p2.executeQuery();
			
			while(r2.next())
			{
				System.out.println("\n"+professors.get(i)+"\t"+r2.getString(1)+"\t"+r2.getString(2));
				//System.out.println();
			}
			
				System.out.println("\n Enter Professor id to assign a professor for this course");
				int pid = sc.nextInt();
				
				if(!professors.contains(pid))
				{
					System.out.println("Invalid Professor Selected / No such professor");
					return;
				}
				else
				{
					query="INSERT INTO public.professorcourse( teaches, taughtby) VALUES (?, ?);";
				}
				
			
			
		}
		
	}
	
	private static void insertCourse(Connection con, int id, String name, String description) throws SQLException
	{
		String query = "Select max(id) from course";
		PreparedStatement getId = con.prepareStatement(query);
		
		ResultSet rs = getId.executeQuery();
		int cid=0;
		while(rs.next())
		{
			cid=rs.getInt(1)+1;
		}
		
		query="INSERT INTO course( id, name, description, offeredby) VALUES (?, ?, ?, ?)";
		PreparedStatement insert = con.prepareCall(query);
		insert.setInt(cid, 1);
		insert.setString(2, name);
		insert.setString(3, description);
		insert.setInt(4, id);
		
		insert.executeUpdate();
		
	}
	
	private static void assignProfessor(Connection con, ArrayList<Integer> professors, int id)
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
		
		p.addCourse(c, 1);
	}
}
