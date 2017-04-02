import java.sql.*;
import java.util.*;

public class CommonFunctions 
{
	String[] splitAddress(String address)
	{
		address= address.substring(1, address.length()-1);
		
		String[] split = address.split(",");
		
		return split;
		
	}
	
	void viewFollowup(Connection con, int c_id, int p_id) throws Exception 
	{
		@SuppressWarnings("resource")
		Scanner st = new Scanner(System.in);
		CommonFunctions c = new CommonFunctions();
		
		PreparedStatement viewPost = con.prepareStatement("select f.post as post, f.id as postid, p.name as name from forum f, person p "
				+ "where f.partof=? and f.askedby=p.id order by f.id desc;");
		try
		{
			viewPost.setInt(1, c_id);
			int i=1;
			System.out.println("\nDiscussion Forums");
			ResultSet rs_viewPost = viewPost.executeQuery();			
			if(!rs_viewPost.isBeforeFirst())
			{
				System.out.println("\nThere are no posts added for this course!");
				return;
			} else
			{
				while(rs_viewPost.next())
				{
					int postid = rs_viewPost.getInt("postid");
					System.out.println("\n"+i+". "+rs_viewPost.getString("post")+"\nAsked by"+rs_viewPost.getString("name"));				
					i++;
					System.out.println("\nFollow ups");
					String viewFol = "select fo.comments as comments, p.name as pname from followup fo, person p "
							+ "where exists(select fo.id from forum f where fo.partof=f.id "
							+ "and fo.partof=? and f.partof=?)"
							+ " and fo.askedby=p.id order by fo.id desc;";
					PreparedStatement viewFollowup = con.prepareStatement(viewFol);
					viewFollowup.setInt(1, postid);
					viewFollowup.setInt(2, c_id);
					
					ResultSet rs = viewFollowup.executeQuery();
					
					if(!rs.isBeforeFirst())
					{
						System.out.println("\nThere are no followups added for this course!");
						//return;
					} else
					{
						while(rs.next())
						{
							System.out.println(rs.getString("comments")+"\nReplied by"
									+rs.getString("pname")+"\n");
						}
					}
				}
			}
						
			while(true)
			{
			System.out.println("\nDo you want to \n1) Add new post \n2) Add comment to existing post \n3)exit");
			int choice = st.nextInt();
			switch(choice)
			{
			case 1: addpostForum(con,c_id,p_id);
				break;
			case 2: System.out.println("Enter the post id from above");
					int post_id = st.nextInt();
					addpostFollowup(con,c_id,post_id,p_id);
				break;
			case 3:
				return;
			default: System.out.println("You have entered the wrong option!");
			}
			}
		}
		finally
		{
			viewPost.close();
		}	
	}
	
	void addpostFollowup(Connection con, int c_id, int post_id, int p_id) throws Exception 
	{
		int f_id=0;
		@SuppressWarnings("resource")
		Scanner st = new Scanner(System.in);
		PreparedStatement getID = con.prepareStatement("Select max(id) from followup");
		PreparedStatement insFollowup = con.prepareStatement("insert into followup values(?,?,?)");
		try
		{
			ResultSet rs_getID = getID.executeQuery();
			if(!rs_getID.isBeforeFirst())
			{
				f_id=0;
			}
			while(rs_getID.next())
			{
				f_id=rs_getID.getInt(1) + 1;
			}
			
			System.out.println("Enter post: ");
			String comment = st.nextLine();
			
			insFollowup.setInt(1, f_id);
			insFollowup.setString(2, comment);
			insFollowup.setInt(3, post_id);
			int rs_insFollowup = insFollowup.executeUpdate();
			if(rs_insFollowup<=0)
			{
				System.out.println("post could not be added..!");
			}
			else
			{
				System.out.println("You have added post successfully.");
			}
		}
		finally
		{
			getID.close();
			insFollowup.close();
		}				
	}
	
	void addpostForum(Connection con, int c_id, int p_id) throws Exception 
	{
		int f_id=0, f_id2=0;
		@SuppressWarnings("resource")
		Scanner st = new Scanner(System.in);
		PreparedStatement getID = con.prepareStatement("Select max(id) from forum");
		PreparedStatement getID2 = con.prepareStatement("Select max(id) from followup");
		PreparedStatement insForum = con.prepareStatement("insert into forum values(?,?,?,?)");
		PreparedStatement insFollowup = con.prepareStatement("insert into followup values(?,?,?)");
		try
		{
			ResultSet rs_getID = getID.executeQuery();
			if(!rs_getID.isBeforeFirst())
			{
				f_id=0;
			}
			while(rs_getID.next())
			{
				f_id=rs_getID.getInt(1) + 1;
			}
			
			ResultSet rs_getID2 = getID2.executeQuery();
			if(!rs_getID2.isBeforeFirst())
			{
				f_id2=0;
			}
			while(rs_getID2.next())
			{
				f_id2=rs_getID2.getInt(1) + 1;
			}
			
			System.out.println("Enter post: ");
			String comment = st.nextLine();
			
			insForum.setInt(1, f_id);
			insForum.setString(2, comment);
			insForum.setInt(3, p_id);
			insForum.setInt(4, c_id);
			int rs_insForum = insForum.executeUpdate();
			if(rs_insForum<=0)
			{
				System.out.println("post could not be added..!");
			}
			else
			{
				System.out.println("You have added post successfully.");
			}
			
			insFollowup.setInt(1, f_id2);
			insFollowup.setString(2, comment);
			insFollowup.setInt(3, f_id);
			int rs_insFollowup = insFollowup.executeUpdate();
//			if(rs_insFollowup<=0)
//			{
//				System.out.println("post could not be added..!");
//			}
//			else
//			{
//				System.out.println("You have added post successfully.");
//			}
		}
		finally
		{
			getID.close();
			insForum.close();
		}				
	}
}
