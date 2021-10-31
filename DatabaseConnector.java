import java.sql.*;
import java.util.ArrayList;


public class DatabaseConnector {


	private static Connection connect = null;
	private PreparedStatement stmt = null;
	private Statement readMessage = null;


	//opens database to update or delete data from. Also reads all the content for the GUI to use
	public static void openDatabase() {
		//Connection connect = null;
		try {
			String connect_url = "jdbc:sqlite:C:/Users/chris/Desktop/test/notedb.db";
			connect = DriverManager.getConnection(connect_url);
			//System.out.println("Successfully connected to SQLite database.");

		} catch (SQLException e) {
			//Handle Exception
			e.printStackTrace();
		}
	}

	public ArrayList<String> readContent() {
		ArrayList<String> table = new ArrayList<String>();
		openDatabase();

		//reads the data from the tables, stores it in an array, and sends it back to the GUI
		try {
			readMessage = connect.createStatement();
			ResultSet readNames = readMessage.executeQuery("Select NoteName FROM CheapNotes");

			while (readNames.next()) {
				table.add(readNames.getString("NoteName"));
			}

			readNames.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {
			closeDatabase();
		}
		return table;
	}

	//save new file into the database
	public void saveNewFile(String fileTitle, String fileContents) {
		openDatabase();
		try {
			PreparedStatement stmt = connect.prepareStatement
					("insert into CHEAPNOTES(NoteName, NoteContent) VALUES(?,?)");
			stmt.setString(1, fileTitle);
			stmt.setString(2, fileContents);
			//System.out.println("hi");
			stmt.executeUpdate();
			stmt.close();
		}

		catch (Exception e) {
			e.printStackTrace();
			//closeDatabase();
		}
		finally
		{
			closeDatabase();
		}
	}
	
	//update the current file to have the new text
	public void update(String fileName, String fileContents) {
		openDatabase();
		try {
			stmt = connect.prepareStatement
					("UPDATE CheapNotes SET NoteContent = ? WHERE NoteName = ?");
			
			stmt.setString(1, fileContents);
			stmt.setString(2, fileName);
			stmt.executeUpdate();
			stmt.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			closeDatabase();
		}
	}

	//deletes the file from the database
	public void delete(String fileTitle, String fileContents) {
		openDatabase();

		try {
			stmt = connect.prepareStatement("DELETE from CheapNotes WHERE NoteName = ?");
			stmt.setString(1, fileTitle);
			stmt.executeUpdate();
			stmt.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}

		finally {
			closeDatabase();
		}
	}

	//opens the file selected by the user
	public String[] openFile(String fileTitle) {
		openDatabase();
		String savedFile[] = new String[2];

		try {
			stmt = connect.prepareStatement("SELECT NoteName, NoteContent FROM CheapNotes WHERE NoteName = ?");
			stmt.setString(1, fileTitle);

			ResultSet getData = stmt.executeQuery();
			
			savedFile[0] = getData.getString("NoteName");
			savedFile[1] = getData.getString("NoteContent");
			getData.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}

		finally {
			closeDatabase();
		}

		return savedFile;
	}

	//function that closes the database after it's been opened and something has been added or removed
	private static void closeDatabase() {
		try {
			connect.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
