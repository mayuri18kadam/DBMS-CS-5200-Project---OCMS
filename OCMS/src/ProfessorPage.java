import java.sql.*;
import java.util.*;

public class ProfessorPage 
{
	Connection con=null;
	int id = 0;
	
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

	private void delLectures(int c_id) throws Exception 
	{
		viewLectures(c_id);
		@SuppressWarnings("resource")
		Scanner st = new Scanner(System.in);
		System.out.println("Enter the ocurse id which is to be deleted: ");
		int id = st.nextInt();
		PreparedStatement delID = con.prepareStatement("delete from Lecture where id=?");	
		try
		{
			
			delID.setInt(1, id);
			int rs_delID = delID.executeUpdate();
			if(rs_delID<=0)
			{
				System.out.println("Lecture could not be deleted..!");
			}
			else
			{
				System.out.println("You have deleted lecture successfully.");
			}
		}
		finally
		{
			delID.close();
		}		
	}

	private void addLectures(int c_id) throws Exception 
	{
		@SuppressWarnings("resource")
		Scanner st = new Scanner(System.in);
		PreparedStatement getID = con.prepareStatement("Select max(id) from Lecture");
		PreparedStatement selectfrmt = con.prepareStatement("SELECT enum_range(NULL::filetype)");
//		PreparedStatement insLecture = con.prepareStatement("insert into Lecture values(?,?,?,?,CAST(? AS filetype),CAST(? AS lectureType),?)");	
		PreparedStatement insLecture = con.prepareStatement("insert into Lecture values(?,?,?,?,CAST(? AS filetype),'Reading',?)");
		try
		{
			ResultSet rs_getID = getID.executeQuery();
			if(!rs_getID.isBeforeFirst())
			{
				id=0;
			}
			while(rs_getID.next())
			{
				 id=rs_getID.getInt(1) + 1;
			}
			
			System.out.println("Enter topic: ");
			String topic = st.nextLine();
			System.out.println("Enter filename: ");
			String filename = st.nextLine();
			
			ResultSet rs_selectfrmt = selectfrmt.executeQuery();
			if(!rs_selectfrmt.isBeforeFirst())
			{
				System.out.println("ERROR!!! There are no pre-fed filetypes is the system. Please contact the system admin!");
			}
			while(rs_selectfrmt.next())
			{
				System.out.println(rs_selectfrmt.getString(1));
			}
			
			System.out.println("Enter filetype from above: ");
			String filetype = st.nextLine();
//			System.out.println("Enter lecture type ('Reading' / 'Video'): ");
//			String lectype = st.nextLine();
			System.out.println("Enter topic description: ");
			String topicdesc = st.nextLine();
			System.out.println();
			
			insLecture.setInt(1, id);
			insLecture.setInt(2, c_id);
			insLecture.setString(3, topic);
			insLecture.setString(4, filename);
			insLecture.setString(5, filetype.toString());
//			insLecture.setString(6, lectype.toString());
			insLecture.setString(6, topicdesc);
			int rs_insLecture = insLecture.executeUpdate();
			if(rs_insLecture<=0)
			{
				System.out.println("Lecture could not be added..!");
			}
			else
			{
				System.out.println("You have added lecture successfully.");
			}
		}
		finally
		{
			getID.close();
			insLecture.close();
		}
		
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
				System.out.println((rs_view.getInt(1)+") "+rs_view.getString(2)+"\t -> \t"+rs_view.getString(3)));
			}
		}
		finally
		{
			view.close();
		}
	}
}
