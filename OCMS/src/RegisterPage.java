import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Scanner;

public class RegisterPage 
{
	
	public void register(Connection con) throws Exception 
	{
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter your choice: \n1) Student \n2) University \n3) Go Back");
		int choice = sc.nextInt();
		
		@SuppressWarnings("unused")
		int id=0;
		
		switch(choice)
		{
		case 1:
			id = registerStudent(con);
			break;
		case 2:
			id = registerUniversity(con);
			break;
		case 3:
			return;
	    default:
	    	System.out.println("Sorry you have entered the wrong choice!");
		}
	}

	private int registerUniversity(Connection con) throws Exception 
	{
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		int id=0;
		PreparedStatement getID = con.prepareStatement("Select max(id) from login");
		PreparedStatement insLogin = con.prepareStatement("insert into Login values(?,?,?,'University')");
		PreparedStatement insUniv = con.prepareStatement("insert into University values(?,?,(?,?,?,?))");
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
			
			System.out.println("Enter Username: ");
			String uname = sc.nextLine();
			System.out.println("Enter password: ");
			String pswd = sc.nextLine();
			System.out.println();
			
			insLogin.setInt(1, id);
			insLogin.setString(2, uname);
			insLogin.setString(3, pswd);
			int rs_insLogin = insLogin.executeUpdate();
			if(rs_insLogin<=0)
			{
				System.out.println("Login registeration failed!");
			}
			else
			{
				System.out.println("You have created your Login information.");
			}
			
			System.out.println("Enter name as Firstname  MiddleName LastName: ");
			String name = sc.nextLine();
			System.out.println("Enter Street: ");
			String street = sc.nextLine();
			System.out.println("Enter City: ");
			String city = sc.nextLine();
			System.out.println("Enter State: ");
			String state = sc.nextLine();
			System.out.println("Enter Zipcode: ");
			int zp = sc.nextInt();
			System.out.println();
			
			insUniv.setInt(1, id);
			insUniv.setString(2, name);
			insUniv.setString(3, street);
			insUniv.setString(4, city);
			insUniv.setString(5, state);
			insUniv.setInt(6, zp);
			int rs_insUniv = insUniv.executeUpdate();
			if(rs_insUniv<=0)
			{
				System.out.println("University registeration failed!");
			}
			else
			{
				System.out.println("You have registered successfully!");
			}
			
			UniversityPage uPage = new UniversityPage();
        	uPage.start(con, id);
		}
		finally
		{
			getID.close();
			insLogin.close();
			insUniv.close();
//			sc.close();
		}
		return id;		
	}

	public int registerProfessor(Connection con, int uniID) throws Exception 
	{
		
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		int id=0;
		PreparedStatement getID = con.prepareStatement("Select max(id) from login");
		PreparedStatement insLogin = con.prepareStatement("insert into Login values(?,?,?,'Professor')");
		PreparedStatement insPerson = con.prepareStatement("insert into Person values(?,?,?,(?,?,?,?))");
		PreparedStatement selectDesg = con.prepareStatement("SELECT enum_range(NULL::designation)");
		PreparedStatement insProf = con.prepareStatement("INSERT INTO public.professor( id, worksfor, designation) VALUES (?, ?, CAST(? AS designation));");

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
			
			System.out.println("Enter Username: ");
			String uname = sc.nextLine();
			System.out.println("Enter password: ");
			String pswd = sc.nextLine();
			System.out.println();
			
			insLogin.setInt(1, id);
			insLogin.setString(2, uname);
			insLogin.setString(3, pswd);
			int rs_insLogin = insLogin.executeUpdate();
			if(rs_insLogin<=0)
			{
				System.out.println("Login registeration failed!");
			}
			else
			{
				System.out.println("You have created your Login information.");
			}
			
			System.out.println("Enter name: ");
			String name = sc.nextLine();
			System.out.println("Enter email: ");
			String email = sc.nextLine();
			System.out.println("Enter Street: ");
			String street = sc.nextLine();
			System.out.println("Enter City: ");
			String city = sc.nextLine();
			System.out.println("Enter State: ");
			String state = sc.nextLine();
			System.out.println("Enter Zipcode: ");
			int zp = sc.nextInt();
			System.out.println();
			
			insPerson.setInt(1, id);
			insPerson.setString(2, name);
			insPerson.setString(3, email);
			insPerson.setString(4, street);
			insPerson.setString(5, city);
			insPerson.setString(6, state);
			insPerson.setInt(7, zp);
			int rs_insPerson = insPerson.executeUpdate();
			if(rs_insPerson<=0)
			{
				System.out.println("Person insert failed!");
			}
			else
			{
				System.out.println("Enter more details..");
			}
			
			//System.out.println("Enter University id that you work for: ");
			//int univ_id = sc.nextInt();
			
			ResultSet rs_selectDesg = selectDesg.executeQuery();
			if(!rs_selectDesg.isBeforeFirst())
			{
				System.out.println("ERROR!!! There are no pre-fed designations is the system. Please contact the system admin!");
			}
			while(rs_selectDesg.next())
			{
				System.out.println(rs_selectDesg.getString(1));
			}
			System.out.println("Enter designation: ");
			
//			@SuppressWarnings("resource")
//			Scanner sc1 = new Scanner(System.in);
			
			String desg = sc.nextLine();
			System.out.println();
			
			insProf.setInt(1, id);
			insProf.setInt(2, uniID);
//			insProf.setString(3, "Dean");
			insProf.setString(3, desg);
			
			System.out.println("Desg ids "+desg);
			int rs_insProf = insProf.executeUpdate();
			if(rs_insProf<=0)
			{
				System.out.println("Professor insert failed!");
			}
			else
			{
				System.out.println("You have registered successfully!");
			}
			
			//ProfessorPage pPage = new ProfessorPage();
        	//pPage.start(con, id);
		}
		finally
		{
			getID.close();
			insLogin.close();
			insPerson.close();
			selectDesg.close();
			insProf.close();
		}
		return id;		
	}

	private int registerStudent(Connection con) throws Exception
	{	
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		int id=0;
		PreparedStatement getID = con.prepareStatement("Select max(id) from login");
		PreparedStatement insLogin = con.prepareStatement("insert into Login values(?,?,?,'Student')");
		PreparedStatement insPerson = con.prepareStatement("insert into Person values(?,?,?,(?,?,?,?))");
		PreparedStatement insStu = con.prepareStatement("insert into Student values(?)");
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
			
			System.out.println("Enter Username: ");
			String uname = sc.nextLine();
			System.out.println("Enter password: ");
			String pswd = sc.nextLine();
			System.out.println();
			
			insLogin.setInt(1, id);
			insLogin.setString(2, uname);
			insLogin.setString(3, pswd);
			int rs_insLogin = insLogin.executeUpdate();
			if(rs_insLogin<=0)
			{
				System.out.println("Login registeration failed!");
			}
			else
			{
				System.out.println("You have created your Login information.");
			}
			
			System.out.println("Enter name: ");
			String name = sc.nextLine();
			System.out.println("Enter email: ");
			String email = sc.nextLine();
			System.out.println("Enter Street: ");
			String street = sc.nextLine();
			System.out.println("Enter City: ");
			String city = sc.nextLine();
			System.out.println("Enter State: ");
			String state = sc.nextLine();
			System.out.println("Enter Zipcode: ");
			int zp = sc.nextInt();
			System.out.println();
			
			insPerson.setInt(1, id);
			insPerson.setString(2, name);
			insPerson.setString(3, email);
			insPerson.setString(4, street);
			insPerson.setString(5, city);
			insPerson.setString(6, state);
			insPerson.setInt(7, zp);
			int rs_insPerson = insPerson.executeUpdate();
			if(rs_insPerson<=0)
			{
				System.out.println("Person insert failed!");
			}
			else
			{
				System.out.println("Person registration successful");
			}
			
			insStu.setInt(1, id);
			int rs_insStu = insStu.executeUpdate();
			if(rs_insStu<=0)
			{
				System.out.println("Student insert failed!");
			}
			else
			{
				System.out.println("You have registered successfully!");
			}
			
			StudentPage sPage = new StudentPage();
        	sPage.start(con, id);
		}
		finally
		{
			getID.close();
			insLogin.close();
			insPerson.close();
			insStu.close();
//			sc.close();
		}
		return id;		
	}

}
