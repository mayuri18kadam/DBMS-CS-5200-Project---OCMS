
public class CommonFunctions 
{
	String[] splitAddress(String address)
	{
		address= address.substring(1, address.length()-1);
		
		String[] split = address.split(",");
		
		return split;
		
	}
}
