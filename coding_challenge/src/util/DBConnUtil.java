package util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DBConnUtil 
{
	public static Connection getConnection(String connString)
	{
		Connection con=null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Loaded..");
			con=DriverManager.getConnection(("jdbc:mysql://localhost:3306/HexawareDB2?user=root&password=password"));
			System.out.println("Connected...");
		}catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			System.out.println("Driver Loading Failed");
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			System.out.println("Not Connected to Database");
		}
		return con;
	}
	public static void main(String[] args) 
	{
		//String connString=DBPropertyUtil.getConnString("db.properties");
		Connection con=getConnection("jdbc:mysql://localhost:3306/HexawareDB2?user=root&password=password");
		System.out.println(con);
	}
}
