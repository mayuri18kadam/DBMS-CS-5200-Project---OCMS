import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

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
		String query="Select c.id, c.name from course c where c.offeredby=?  order by c.id";
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
			System.out.println("id\t"+"name\t"+"\tprofessor");
			while(rs.next())
			{
				int cid = rs.getInt(1);
				System.out.print(cid+"\t");
				System.out.print(rs.getString(2)+"\t\t");
				
				PreparedStatement getProfessors = con.prepareStatement("Select taughtby from professorcourse where teaches=?");
				getProfessors.setInt(1, cid);
				ResultSet rpc = getProfessors.executeQuery();
				
				while(rpc.next())
				{
					
				int pid = rpc.getInt(1);
			    PreparedStatement p2 = con.prepareStatement("Select name from person where id=?" );
			    p2.setInt(1, pid);
			    ResultSet r2 = p2.executeQuery();
			    while(r2.next())
			    {
			    	System.out.print(r2.getString(1)+"\t");
			    	
			    }
				}
				System.out.println();
			}
			
		}
		
	}
	
	
	public void addP(Connection conn, int id) throws SQLException
	{
		Scanner sc = new Scanner(System.in);
		
		System.out.println("You are about to hire a new Professor for "+this.name);
		System.out.println("Here is the list of all existing professors in the database ");
		
		ArrayList<Integer> allProfessors = new ArrayList<>();
		
		String query= "Select p.id , p1.name, p.designation from professor p , person p1 where p.id = p1.id";
		
		PreparedStatement getProfName = conn.prepareStatement(query);
		
		ResultSet rs = getProfName.executeQuery();
		
		while(rs.next())
		{
			int pid = rs.getInt(1);
			
			allProfessors.add(pid);
			
			System.out.println(pid);
			System.out.println(rs.getString(2));
			System.out.println(rs.getString(3));
		}
		
		
		while(true)
		{
			System.out.println("Press 1 to hire existing professor Press 2 to add a new Professor press 3 to exit");
			int choice;
			choice = sc.nextInt();
			
			if(choice==1)
			{
				addExistingProfessor(conn, sc, allProfessors);
			}
			if(choice==2)
			{
				
			}
			if(choice==3)
			{
				return;
			}
			else
			{
				System.out.println("Invalid Choice");
			}
		}
		
		
	}
	
	private void addNewProfessor(Connection conn)
	{
		System.out.println("You are about to add a new Professor");
		
		
		
	}
	
	private void addExistingProfessor(Connection conn , Scanner sc, ArrayList<Integer> allProf) throws SQLException
	{
		String query= "Select p.id , p1.name, p.designation from professor p , person p1 where p.id = p1.id and p.worksfor=?";
		
		Scanner s = new Scanner(System.in);
		
		ArrayList<Integer> currentProfessor= new ArrayList<>();
		
		PreparedStatement getProfName = conn.prepareStatement(query);
		getProfName.setInt(1, this.id);
		ResultSet rs = getProfName.executeQuery();
		
		
		while(rs.next())
		{
			int pid = rs.getInt(1);
			
			currentProfessor.add(pid);	
		}
		
		System.out.println("Enter Professor id you want to hire");
		int pid = sc.nextInt();
		
		if(currentProfessor.contains(pid))
		{
			System.out.println("This professor already works for "+this.name);
		}
		
		if(!allProf.contains(pid))
		{
			System.out.println("No such Professor in the database");
		}
		else
		{
			System.out.println(this.id);
			System.out.println("Assign a designation for a professor, Enter one of ");
			System.out.println("Dean,Professor,Associate_Professor,Lecturer,Visiting Scholar,Director,Associate Director,TA,RA,GA,Part time faculty,Contractor");
			String designtion = s.nextLine();
			
			PreparedStatement p = conn.prepareStatement("INSERT INTO public.professor( id, worksfor, designation) VALUES (?, ?, CAST(? AS designation));");
			
			p.setInt(1, pid);
			p.setInt(2, this.id);
			p.setString(3, designtion);
			
			p.executeUpdate();
			
			System.out.println("Professor inserted successfully");
		}
		
	}
	
	public void addProfessor(Connection conn, int id) throws SQLException
	{
		
		
		Scanner sc = new Scanner(System.in);
		
		PreparedStatement getProfId = conn.prepareStatement("select max(id) from professor");
		ResultSet profid = getProfId.executeQuery();
		
		int pid=0;
		while(profid.next())
		{
			pid=profid.getInt(1) + 1;
		}
		
		System.out.println("Enter Designation ");
		String designantion = sc.nextLine();
		
		PreparedStatement p = conn.prepareStatement("INSERT INTO public.professor( id, worksfor, designation) VALUES (11, ?, CAST(? AS designation));");
		
	//	p.setInt(1, pid);
		p.setInt(1, id);
		p.setString(2, designantion.toString());
		p.executeUpdate();
	}
	
	public void addCourse(Connection conn, int id) throws SQLException
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter name for course");
		
		
		
		String name = sc.nextLine();
		System.out.println("Enter description for course");
		
		String description = sc.nextLine();

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
		}	
			int cid = insertCourse(conn, id, name, description, professors);
			if(cid==0)
			{
				return;
			}
				
				while(true)
				{
					int z=0;
					System.out.println("Press 1 to assign 1 more professor for this course , Any other key to exit");
					 z=sc.nextInt();
					if(z==1)
					{	
						System.out.println("Enter Professor id ");
					 int pid= sc.nextInt();
					 if(validateProfessor(professors, pid))
					 	{	
						 assignProfessor(conn, professors, cid,pid);
					 	}
					 else
					 {
						 System.out.println("Invalid Professor id");
					 }
					}
					else
					{
						return;
					}
				}
			
			
		
		
	}
	
	private static int insertCourse(Connection con, int id, String name, String description, ArrayList<Integer> professors) throws SQLException
	{
		String query = "Select max(id) from course";
		PreparedStatement getId = con.prepareStatement(query);
		
		ResultSet rs = getId.executeQuery();
		int cid=0;
		while(rs.next())
		{
			cid=rs.getInt(1)+1;
		}
		
		System.out.println("\n Enter Professor id to assign a professor for this course");
		Scanner sc = new Scanner(System.in);
		int pid = sc.nextInt();
		
		if(validateProfessor(professors, pid))
		{	
		
		query="INSERT INTO course( id, name, description, offeredby) VALUES (?, ?, ?, ?)";
		PreparedStatement insert = con.prepareCall(query);
		insert.setInt(1, cid);
		insert.setString(2, name);
		insert.setString(3, description);
		insert.setInt(4, id);
		
		insert.executeUpdate();
		System.out.println("Course Successfully added");
		assignProfessor(con, professors, cid, pid);
		
		return cid;
		}
		else
		{
			System.out.println("Invalid Professor id");
			return 0;
		}
	}
	
	private static void assignProfessor(Connection con, ArrayList<Integer> professors, int cid, int pid) throws SQLException
	{
		
		{
			String query="INSERT INTO public.professorcourse( teaches, taughtby) VALUES (?, ?);";
			PreparedStatement insertprof = con.prepareStatement(query);
			insertprof.setInt(1, cid);
			insertprof.setInt(2, pid);
			
			insertprof.executeUpdate();
			
			System.out.println("Professor Successfully added");
		}
	}
	
	private static boolean validateProfessor(ArrayList<Integer> professor , int pid)
	{
		return (professor.contains(pid));
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
		
	//	p.listCourses(c, 1);
		System.out.println("\n\nAdding a new course");
	//	p.addCourse(c, 1);
		
	//	p.addProfessor(c, 1);
		
		p.addP(c, 1);
	}
}
