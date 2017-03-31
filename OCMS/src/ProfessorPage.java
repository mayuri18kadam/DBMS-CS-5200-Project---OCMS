import java.sql.*;
import java.util.Scanner;

public class ProfessorPage 
{
	
	public void start(Connection con, int id) throws Exception 
	{
		Scanner st = new Scanner(System.in);
		System.out.println(st.nextInt());
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
				 System.out.println(rs1.getInt(1)+") "+rs1.getString(2));
			}
			
			System.out.println("Select your course id: ");
			int choice = st.nextInt();
			System.out.println("Here"+choice);
//			System.out.println("You have selected: "+choice);
		}		
		finally
		{			
			getCourseIDName.close();
//			st.close();
		}		
	}
}
