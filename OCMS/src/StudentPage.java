import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class StudentPage 
{
	//Connection con = null;
	int id;
	String studName;
	
	public void start(Connection con, int id) throws SQLException
	{
		System.out.println(id);
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter your choice: \n1) View My Courses"
				+ " \n2) View All Courses"
				+ " \n3) Sign Out \n");
		int choice = sc.nextInt();
				
		switch(choice)
		{
		case 1:
			viewMyCourses(con, id);
			break;
		case 2:
			viewAllCourses(con, id);
			break;
		case 3:
			System.out.println("You have been signed out!");
			System.exit(0);
			break;
		default:
			System.out.println("Sorry! you have entered the wrong choice!");
		}
		
		sc.close();
		
	}
	
	public void viewMyCourses(Connection con, int id) throws SQLException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Which of your courses do you want to see?"
				+ " : \n1) View All My Courses"
				+ " \n2) View Active Courses"
				+ " \n3) View Completed Courses \n");
		int choice = sc.nextInt();
				
		switch(choice)
		{
		case 1:
			viewAllMyCourses(con, id);
			break;
		case 2:
			viewActiveCourses(con, id);
			break;
		case 3:
			viewCompletedCourses(con,id);
			break;
		default:
			System.out.println("Sorry! you have entered the wrong choice!/n");
			viewMyCourses(con,id);
		}
		
		sc.close();
	}
		
	public void viewAllMyCourses(Connection con, int id) throws SQLException 
	{				
		fetchName(con,id);
		Scanner sc = new Scanner(System.in);
		
		String myCourse = "Select c.name as courseName, u.name as univName, sc.status as scStatus"
				+ " from course c, university u, student s, studentcourse sc "
				+ "where s.id = ? and sc.takenby = s.id and sc.takes = c.id and c.offeredby = u.id;";
		PreparedStatement ps = null;
		
		ps = con.prepareStatement(myCourse);
		ps.setInt(1,id);
		
		ResultSet rs = ps.executeQuery();		
				
		if(!rs.isBeforeFirst())
		{
			System.out.println("No courses ever enrolled.");
		}
		else
		{
			System.out.println("My courses:");
			
			while(rs.next())
			{				
				System.out.println(rs.getString("courseName") + " offered by " + rs.getString("univName") +"\tstatus- "+ rs.getString("scStatus"));
				
			}
		}
		navigation(con,id);
		sc.close();
		}
		
	public void viewActiveCourses(Connection con, int id) throws SQLException 
	{
		fetchName(con,id);
		Scanner sc = new Scanner(System.in);
		
		String activeCourse = "Select c.name as courseName, u.name as univName, sc.status as scStatus"
				+ " from course c, university u, student s, studentcourse sc "
				+ "where s.id = ? and sc.takenby = s.id and sc.takes = c.id and c.offeredby = u.id and sc.status=?;";
		PreparedStatement ps = null;
		
		ps = con.prepareStatement(activeCourse);
		ps.setInt(1,id);
		ps.setString(2, "Active");
		
		ResultSet rs = ps.executeQuery();		
				
		if(!rs.isBeforeFirst())
		{
			System.out.println("No active courses.");
		}
		else
		{
			System.out.println("My Active Courses:");
			
			while(rs.next())
			{				
				System.out.println(rs.getString("courseName") + " offered by " + rs.getString("univName") + "\tstatus- " + rs.getString("scStatus"));
				
			}
		}
		navigation(con,id);
		sc.close();
	}
	
	public void viewCompletedCourses(Connection con, int id) throws SQLException 
	{
		fetchName(con,id);
		Scanner sc = new Scanner(System.in);
		
		String completedCourse = "Select c.name as courseName, u.name as univName, sc.status as scStatus"
				+ " from course c, university u, student s, studentcourse sc "
				+ "where s.id = ? and sc.takenby = s.id and sc.takes = c.id and c.offeredby = u.id and sc.status=?;";
		PreparedStatement ps = null;
		
		ps = con.prepareStatement(completedCourse);
		ps.setInt(1,id);
		ps.setString(2, "Completed");
		
		ResultSet rs = ps.executeQuery();		
				
		if(!rs.isBeforeFirst())
		{
			System.out.println("No completed courses.");
		}
		else
		{
			System.out.println("My Completed Courses:");
			
			while(rs.next())
			{				
				System.out.println(rs.getString("courseName") + " offered by " + rs.getString("univName") + "\tstatus- " + rs.getString("scStatus"));
				
			}
		}
		navigation(con,id);
		sc.close();
	}
	
	public void viewAllCourses(Connection con, int id) throws SQLException 
	{	
		Scanner sc = new Scanner(System.in);
		
		String allCourses = "Select c.name as courseName, c.cid as courseId, u.name as univName, p.name as professorName "
				+ "from course c, university u, professor pr, professorcourse pc, person p "
				+ "where c.offeredby = u.id and pc.teaches=c.id and pc.taughtby=pr.id and pr.id=p.id;";
		PreparedStatement ps = null;		
		ps = con.prepareStatement(allCourses);

		ResultSet rs = ps.executeQuery();
				
		if(!rs.isBeforeFirst())
		{
			System.out.println("No courses to offer.");
		}
		else
		{
			System.out.println("All Courses:");
			
			while(rs.next())
			{				
				System.out.println("\n" + rs.getString("courseName") + "\nCourse Id - " +
						rs.getString("courseId") + "\noffered by " + rs.getString("univName")
						+ "\ntaught by" + rs.getString("professorName"));				
			}
		}
		System.out.println("\nDo you want to enroll or know more about any courses listed?[y/n]");
		String ans = sc.next();
		
		if(ans.equals("y") || ans.equals("Y"))
		{
			sc.nextLine();
			System.out.println("Enter the course id of the course to see its details");
			String cid = sc.nextLine();
			//System.out.println(cid);
			gotoCourse(con,id,cid);
		} else if(ans.equals("n") || ans.equals("N"))
			{
				System.out.println("\n1) Go to the Home Page \n"
						+ "2) Sign out\n");
				int choice = sc.nextInt();
				
				switch(choice)
				{
				case 1:
					start(con, id);
					break;
				case 2:
					System.out.println("You have been signed out!");
					System.exit(0);
				default:
					System.out.println("Sorry! you have entered the wrong choice!");
					//navigation(con,id);
				}
			} else
			{
				System.out.println("Invalid input.");
			}

		sc.close();
	}
	
	public void fetchName(Connection con, int id) throws SQLException 
	{
		String studName = "Select p.name from person p, student s "
				+ "where s.id=p.id and s.id = ?;";
		PreparedStatement ps0 =con.prepareStatement(studName);
		ps0.setInt(1, id);
		
		ResultSet rs = ps0.executeQuery();
		
		if(!rs.isBeforeFirst())
		{
			System.out.println("You have been signed out.");
			System.exit(0);
		}
		else
		{
			while (rs.next()) {
				System.out.println("Student " + rs.getString(1) +" courses");
				Scanner sc = new Scanner(System.in);
			}
		}
	}
	
	public void navigation(Connection con, int id) throws SQLException
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("\n1) Go back\n "
				+ "2) Go to the Home Page \n"
				+ "3) Sign out\n");
		int choice = sc.nextInt();
				
		switch(choice)
		{
		case 1:
			viewMyCourses(con, id);
			break;
		case 2:
			start(con, id);
			break;
		case 3:
			System.out.println("You have been signed out!");
			System.exit(0);
		default:
			System.out.println("Sorry! you have entered the wrong choice!");
			navigation(con,id);
		}
		sc.close();
	}
	
	public void gotoCourse(Connection con, int id, String cid) throws SQLException
	{
		Scanner sc = new Scanner(System.in);
		
		String allCourses = "Select c.name as courseName, c.cid as courseId, c.description as description, c.id as course, "
				+ "u.name as univName, p.name as profName, pr.designation as designation "
				+ "from course c, university u, professor pr, professorcourse pc, person p "
				+ "where c.offeredby = u.id and pc.teaches=c.id and pc.taughtby=pr.id and pr.id=p.id and c.cid=?;";
		
		PreparedStatement ps = null;		
		ps = con.prepareStatement(allCourses);
		ps.setString(1, cid);

		ResultSet rs = ps.executeQuery();
		int pcourseId = 0;
		String courseName = null;
				
		if(!rs.isBeforeFirst())
		{
			System.out.println("No course of specified search found.");
		}
		else
		{
			//System.out.println("All Courses:");
			
			while(rs.next())
			{				
				System.out.println("\n" + rs.getString("courseName"));
				System.out.println("About the course:  " + rs.getString("description"));
				System.out.println("Offered by:  " + rs.getString("univName"));
				System.out.println("Taught by:  " + rs.getString("profName") + " ," + rs.getString("designation"));
				pcourseId = rs.getInt("course");
				courseName = rs.getString("courseName");
			}			
		}
		//System.out.println(pcourseId);
		System.out.println("\nDo you want to enroll now to this course?[y/n]");
		String ans = sc.next();
		
		if(ans.equals("y") || ans.equals("Y"))
		{
			//sc.nextLine();
			String enroll = "Insert into studentcourse(takes,takenby,status) "
					+ "select ?,?,?"
					+ "where not exists (Select * from studentcourse where takes=? and takenby=?);";
			
			PreparedStatement ps1 = null;	
			ps1 = con.prepareStatement(enroll);
			ps1.setInt(1, pcourseId);
			ps1.setInt(2, id);
			ps1.setString(3, "Active");
			ps1.setInt(4, pcourseId);
			ps1.setInt(5, id);

			int count = ps1.executeUpdate();
			
			if(count>0)
			{
				System.out.println("Welcome to the course " + courseName
				+ ". You can now access the course materials.");
				System.out.println("\n1) Start Learning \n2)Go to Home Page "
						+ "\n3) Sign Out");
				
				int ch = sc.nextInt();
				
				switch(ch)
				{
				case 1:
					courseHome(con, id, cid);
					break;
				case 2:
					start(con, id);
					break;
				case 3:
					System.out.println("You have been signed out!");
					System.exit(0);
					break;
				default:
					System.out.println("Sorry! you have entered the wrong choice!");
				}
			} else
			{
				System.out.println("You are already enrolled for this course.");
			}
			//System.out.println(id +"\n" + pcourseId);	
			} else if(ans.equals("n") || ans.equals("N"))
			{
				System.out.println("\n1) Go to the Home Page \n"
						+ "2) Sign out\n");
				int choice = sc.nextInt();
				
				switch(choice)
				{
				case 1:
					start(con, id);
					break;
				case 2:
					System.out.println("You have been signed out!");
					System.exit(0);
				default:
					System.out.println("Sorry! you have entered the wrong choice!");
					//navigation(con,id);
				}
			} else
			{
				System.out.println("Invalid input.");
			}

		sc.close();

	}
	
	public void courseHome(Connection con, int id, String cid)
	{
		System.out.println("Course Home");
	}
	
	// ONLY FOR UNIT TESTING OF STUDENTS FUNCTIONALITY
		public static void main(String args[]) throws ClassNotFoundException, SQLException
		{
			StudentPage sp = new StudentPage();
			
				 final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
			     final String DB_URL = "jdbc:postgresql://postgres.c9mcz4kq3yti.us-west-2.rds.amazonaws.com:5432/OCMS";

			   // Database credentials
			    final String USER = "postgres";
			    final String PASS = "Akshay1!";
			    
			    Connection c = null;
			    Class.forName("org.postgresql.Driver");	         
			    c = DriverManager.getConnection(DB_URL,USER,PASS);
			     
			//sp.start(c, 5);
			  //sp.viewAllCourses(c, 1);
			 //sp.viewMyCourses(c, 1);
			         sp.gotoCourse(c, 1, "CS 4560");
			
			
		}

}
