import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import javax.swing.JOptionPane;

public class StudentPage 
{
	//Connection con = null;
	int id;
	String studName;
	
	public void start(Connection con, int id) throws Exception
	{
		this.id = id;
		
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
	
	public void viewMyCourses(Connection con, int id) throws Exception {
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
		
	public void viewAllMyCourses(Connection con, int id) throws Exception 
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
		
	public void viewActiveCourses(Connection con, int id) throws Exception 
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
	
	public void viewCompletedCourses(Connection con, int id) throws Exception 
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
	
	public void viewAllCourses(Connection con, int id) throws Exception 
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
	
	public void navigation(Connection con, int id) throws Exception
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("\n1) Go back\n"
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
	
	public void gotoCourse(Connection con, int id, String cid) throws Exception
	{
		Scanner sc = new Scanner(System.in);
		
		String course = "Select c.name as courseName, c.cid as courseId, c.description as description, c.id as course, "
				+ "u.name as univName, p.name as profName, pr.designation as designation "
				+ "from course c, university u, professor pr, professorcourse pc, person p "
				+ "where c.offeredby = u.id and pc.teaches=c.id and pc.taughtby=pr.id and pr.id=p.id and c.cid=?;";
				
		PreparedStatement ps = null;		
		ps = con.prepareStatement(course);
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
			
			if(rs.next())
			{
				pcourseId = rs.getInt("course");
				courseName = rs.getString("courseName");
				
				System.out.println("\n" + courseName);
				System.out.println("About the course:  " + rs.getString("description"));
				System.out.println("Offered by:  " + rs.getString("univName"));
				System.out.println("Taught by:  " + rs.getString("profName") + " ," + rs.getString("designation"));
				
				while(rs.next())
				{
					System.out.println("Taught by:  " + rs.getString("profName") + " ," + rs.getString("designation"));
				}
				
				TreeMap<Integer, Integer> lectrsMapping = new TreeMap<Integer, Integer>();
				lectrsMapping = viewLectures(con,id,cid,pcourseId,"allLectrs");
				//viewLectures(con,id,cid,pcourseId,"allLectrs");
						
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
			
			PreparedStatement ps3 = null;	
			ps3 = con.prepareStatement(enroll);
			ps3.setInt(1, pcourseId);
			ps3.setInt(2, id);
			ps3.setString(3, "Active");
			ps3.setInt(4, pcourseId);
			ps3.setInt(5, id);

			int count = ps3.executeUpdate();
			
			if(count>0)
			{
				System.out.println("Welcome to the course " + courseName
				+ ". You can now access the course materials.\n");
				System.out.println("1) Start Learning \n2) Go to the Home Page "
						+ "\n3) Sign Out");
				
				int ch = sc.nextInt();
				
				switch(ch)
				{
				case 1:
					courseHome(con, id, cid, pcourseId);
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
				courseHome(con,id,cid,pcourseId);
			}
			
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
	
	public void courseHome(Connection con, int id, String cid, int pcourseId) throws Exception
	{		
		Scanner sc = new Scanner(System.in);
		System.out.println("\nCourse Home\n");
		System.out.println("1) View Lectures\n2) Discussion Forums");
		int ch = sc.nextInt();
		
		switch(ch)
		{
		case 1:
			TreeMap<Integer, Integer> lectrsMapping = new TreeMap<Integer, Integer>();
			lectrsMapping = viewLectures(con,id,cid,pcourseId,"studLectr");
			viewLectrsChoice(con, cid, id, pcourseId, lectrsMapping);
			break;
		case 2:
		//	discussionForum(con, id, cid, pcourseId);
			CommonFunctions c = new CommonFunctions();
			c.viewFollowup(con, 2, id);
			break;
		default:
			System.out.println("Invalid input");
			System.out.println("Please choose from the below options");
			courseHome(con, id, cid, pcourseId);
			break;
		}
				
	}
	
	public void discussionForum(Connection con, int id, String cid, int pcourseId)
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("\nDescription");
		System.out.println("Welcome to the course discussion forums! "
				+ "Ask questions, debate ideas, and find classmates who share your goals.");
		System.out.println("1) View Discussion Forums\n2) Create a new thread");
		int ch = sc.nextInt();
		
		switch(ch)
		{
		case 1:
			viewDiscussionForum(con, id, cid, pcourseId);
			break;
		case 2:
			createThread(con, id, cid, pcourseId);
			break;
		default:
			System.out.println("Invalid input");
			System.out.println("Please choose from the below options");
			discussionForum(con, id, cid, pcourseId);
			break;
		}
	}
	
	public void viewDiscussionForum(Connection con, int id, String cid, int pcourseId)
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("Discussion Forums");
		//String viewdf = "Select f.post, p.name, "
	}
	
	public void createThread(Connection con, int id, String cid, int pcourseId)
	{
		System.out.println("Create a new thread");
	}
	
 	public int fetchLecture(TreeMap<Integer,Integer> lectrsMapping)
	{
		Scanner sc = new Scanner(System.in);
		int lnum = sc.nextInt();
		int lid=0;
		if(lectrsMapping.containsKey(lnum))
		{
			lid = lectrsMapping.get(lnum);
		} else
		{
			System.out.println("You have entered the wrong lecture number.");
			System.out.println("Please choose the valid lecture number");
			fetchLecture(lectrsMapping);
		}
		return lid;
	}
	
	public TreeMap<Integer, Integer> viewLectures(Connection con, int id, String cid, int pcourseId, String cond) throws SQLException
	{
		TreeMap<Integer, Integer> sViewsViewedby = new TreeMap<Integer, Integer>();
		TreeMap<Integer, Integer> lectrsMap = new TreeMap<Integer, Integer>();
		
		String lectr = "Select l.id as lid, l.topic as topic, l.lecturetype as type, l.topicdescription as desc "
				+ "from lecture l, course c "
				+ "where c.cid=? and l.partof=c.id order by l.id;";
		
		String lectrCount = "select count(l.id) from lecture l where l.partOf=?;";
		
		PreparedStatement ps1 = con.prepareStatement(lectr);
		ps1.setString(1, cid);
		
		PreparedStatement ps2 = con.prepareStatement(lectrCount);
		ps2.setInt(1, pcourseId);
		
		ResultSet rs1 = ps2.executeQuery();
		
		int count = 0;
		if(rs1.next())
		{
			count = rs1.getInt(1);
			System.out.print("\nTotal lectures: " + count);
		}
		
		rs1 = ps1.executeQuery();
		
		if(!rs1.isBeforeFirst())
		{
			System.out.println("There are no lectures added in this course.");
		}
		else
		{
			int i=1;
			System.out.println("\n Syllabus\n");
			while(rs1.next())
			{
				for(int k=0; k<count; k++ )
				{
					lectrsMap.put(i, rs1.getInt("lid"));
				}
				
				if(cond.equals("studLectr"))
				{
					String slectr = "Select sl.views as views, sl.viewedby as viewedby from studentlecture sl "
							+ "where sl.viewedby=? and sl.views=?;";
					PreparedStatement ps3 = con.prepareStatement(slectr);
					ps3.setInt(1, id);
					ps3.setInt(2, rs1.getInt("lid"));
					
					ResultSet rs = ps3.executeQuery();
					
					while(rs.next())
					{
						sViewsViewedby.put(rs.getInt("views"), rs.getInt("viewedby"));
					}			
				}
				System.out.println(i +". " + rs1.getString("type") +": "
						+ rs1.getString("topic"));
				System.out.println(rs1.getString("desc"));
				
				if(cond.equals("studLectr"))
				{
					System.out.print("Status: ");
					if(sViewsViewedby.containsKey(rs1.getInt("lid")))
					{
						System.out.println("Completed.");
					} else
					{
						System.out.println("Pending");
					}
				}
				System.out.print("\n");
				i++;
			}
			
		}
		return lectrsMap;
	}
	
	public void viewLectrsChoice(Connection con, String cid, int id, int pcourseId, TreeMap<Integer, Integer> lectrsMapping) throws Exception
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("Do you want to view lectures?[y,n]");
		String ch = sc.next();
		
		if(ch.equals("y") || ch.equals("Y"))
		{
			System.out.println("\nEnter the lecture number which you would want to watch/read:");
			
			int lid = fetchLecture(lectrsMapping);
			
			/*for(Map.Entry<Integer,Integer> entry : lectrsMapping.entrySet()) {
			  Integer key = entry.getKey();
			  Integer value = entry.getValue();
	
			  System.out.println(key + " => " + value);
			}*/		
			
			String lectrViewed = "Insert into studentlecture(views,viewedby)"
					+ "select ?,?"
					+ "where not exists (Select * from studentlecture where views=? and viewedby=?);";
			PreparedStatement ps = con.prepareStatement(lectrViewed);
			ps.setInt(1, lid);
			ps.setInt(2, id);
			ps.setInt(3, lid);
			ps.setInt(4, id);
			
			int count = ps.executeUpdate();
			
			if(count>0)
			{
				//infoBox("Thank you for viewing this lecture", "Viewed");
				System.out.println("Thank you for viewing this lecture");
				
			} else
			{
				System.out.println("Thank you for viewing this lecture again");
			}
			courseHome(con, id, cid, pcourseId);
		} else if(ch.equals("n") || ch.equals("N"))
		{
			navigation(con, id);
		}
		sc.close();
	}
	
 	public static void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }
	
	// ONLY FOR UNIT TESTING OF STUDENTS FUNCTIONALITY
//		public static void main(String args[]) throws Exception
//		{
//			StudentPage sp = new StudentPage();
//			
//				 final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
//			     final String DB_URL = "jdbc:postgresql://postgres.c9mcz4kq3yti.us-west-2.rds.amazonaws.com:5432/OCMS";
//
//			   // Database credentials
//			    final String USER = "postgres";
//			    final String PASS = "Akshay1!";
//			    
//			    Connection c = null;
//			    Class.forName("org.postgresql.Driver");	         
//			    c = DriverManager.getConnection(DB_URL,USER,PASS);
//			     
//			//sp.start(c, 5);
//			  //sp.viewAllCourses(c, 1);
//			 //sp.viewMyCourses(c, 1);
//			  //sp.gotoCourse(c, 3, "CS 5320");
//			    //sp.viewLectures(c, 4, "CS 5320", 2, "studLectr");
//			    //sp.viewLectures(c, 4, "CS 5320", 2, "allLectrs");
//			    sp.courseHome(c, 4, "CS 5320", 2);
//			
//			
//		}

}

