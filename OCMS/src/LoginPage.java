import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class LoginPage 
{
	void login(Connection con) throws SQLException
	{
		System.out.println("Enter your choice \n1) to login as Student \n2) to login as Professor \n3) to login as  a University Person\n");
		Scanner sc = new Scanner(System.in);
		
		int choice = sc.nextInt();
		String user="";
		String pass="";
		
		
		switch(choice)
		{
		case 1:
			System.out.println("Enter Username\n");
			user=sc.next();
			System.out.println("Enter Pass\n");
			pass=sc.next();
			
			PreparedStatement p = con.prepareStatement("Select id from login where username=? AND password=? AND type=?");
			
			p.setString(1, user);
			p.setString(2, pass);
			p.setString(3, "Student");
			ResultSet rs = p.executeQuery();
			
			int sid=0;
			
			if(!rs.isBeforeFirst())
			{
				System.out.println("Invalid Login");
				return;
			}
			while(rs.next())
			{
				 sid=rs.getInt(1);
			}
			System.out.println(sid);
			p.close();
			
			 p = con.prepareStatement("Select name from Person where id=?");
			p.setInt(1, sid);
			
			rs = p.executeQuery();
			String sname="";
			while(rs.next())
			{
				 sname=rs.getString(1);
			}
			System.out.println(sname);
		}
		
	}
}
