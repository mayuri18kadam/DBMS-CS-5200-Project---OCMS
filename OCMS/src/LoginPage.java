import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class LoginPage 
{
	void login(Connection con) throws SQLException
	{
		Scanner sc = new Scanner(System.in);
		String user="";
		String pass="";
		String type = "";
		int id=0;
		
			System.out.println("Enter Username: ");
			user=sc.next();
			System.out.println("Enter Password: ");
			pass=sc.next();
			System.out.println();
			PreparedStatement p = con.prepareStatement("Select id, type from login where username=? AND password=?");
			try
			{
				p.setString(1, user);
				p.setString(2, pass);
				ResultSet rs = p.executeQuery();
				if(!rs.isBeforeFirst())
				{
					System.out.println("Invalid Login");
					return;
				}
				while(rs.next())
				{
					id = rs.getInt(1);
					type=rs.getString(2);
				}
			}
			finally
			{
				p.close();
				sc.close();
			}
			if(type.equalsIgnoreCase("Student"))
			{				
				StudentPage sPage = new StudentPage();
				sPage.start(con, id);
			}
			else if(type.equalsIgnoreCase("Professor"))
			{
				ProfessorPage pPage = new ProfessorPage();
	        	pPage.start(con, id);
			}
			else
			{
				UniversityPage uPage = new UniversityPage();
	        	uPage.start(con, id);
			}
	}
}
