import java.sql.*;
import java.util.*;

public class ProfessorPage 
{
	Connection con=null;
	int id = 0;
	List<Lecture> l = new LinkedList<Lecture>();
	
	public void start(Connection con, int id) throws Exception 
	{
		this.con = con;
		this.id = id;
		
		@SuppressWarnings("resource")
		Scanner st = new Scanner(System.in);
		
		TreeMap<Integer, String> courseIDName = new TreeMap<Integer, String>();
		PreparedStatement getCourseIDName = con.prepareStatement("Select id, name from course, professorcourse"
				+ " where name is not null"
				+ " and course.id = professorcourse.teaches"
				+ " and professorcourse.taughtby = ?");
		try
		{
			getCourseIDName.setInt(1,id);
			ResultSet rs1 = getCourseIDName.executeQuery();			
			if(!rs1.isBeforeFirst())
			{
				throw new Exception("There is no course registered");
			}
			while(rs1.next())
			{
				courseIDName.put(rs1.getInt(1), rs1.getString(2));
			}
			for(Integer i : courseIDName.keySet())
			{
				System.out.println(i+") "+courseIDName.get(i));
			}
			
			System.out.println("Select your course id: ");
			int c_id = st.nextInt();
			System.out.println("You have selected course no: "+c_id);
			
			System.out.println("Choose your option for course no "+c_id+": \n1) View Lectures \n2) Add Lectures \n3) Delete Lectures");
			int choice = st.nextInt();
			
			switch(choice)
			{
			case 1: viewLectures(c_id);
			break;
			case 2: addLectures(c_id);
			break;
			case 3: delLectures(c_id);
			break;
			default: System.out.println("You have entered the wrong option!");
			}
		}		
		finally
		{			
			getCourseIDName.close();
		}		
	}

	private void delLectures(int c_id) 
	{
				
	}

	private void addLectures(int c_id) 
	{
				
	}

	private void viewLectures(int c_id) throws Exception 
	{
		PreparedStatement view = con.prepareStatement("Select id, topic, filename from lecture where partof=?");	
		try
		{
			view.setInt(1, c_id);
			ResultSet rs_view = view.executeQuery();			
			if(!rs_view.isBeforeFirst())
			{
				throw new Exception("There are no lectures added for this course!");
			}
			while(rs_view.next())
			{
				Lecture lec = new Lecture(rs_view.getInt(1), rs_view.getString(2), rs_view.getString(3));
				System.out.println(lec.id+") "+lec.topic+"\t -> \t"+lec.filename);
				l.add(lec);
			}
			for(Lecture ltr : l)
			{
				System.out.println(ltr.id+") "+ltr.topic+"\t -> \t"+ltr.filename);
			}
		}
		finally
		{
			view.close();
		}
	}
}
