package changefeatureextractor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.StringUtils;

import Database.DatabaseManager;

public class Content {
	final static Connection conn = DatabaseManager.getConnection();
	static final String findNewLoc = "select content from content where file_id=? and commit_id=?";
	private static PreparedStatement findNewLocQuery;

	public int getNewLoc(String fileid, String commitid) {
		final ResultSet contents;
		String content = null;
		int newloc = 0;
		try {
			findNewLocQuery = conn.prepareStatement(findNewLoc);
			findNewLocQuery.setString(1, fileid);
			findNewLocQuery.setString(2, commitid);
			contents = findNewLocQuery.executeQuery();
			while(contents.next()){
				content = contents.getString(1);
			}
			for(int i=0;i<content.length();i++){
				if((content.charAt(i))=='\n'){
					newloc++;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newloc+1;
	}

}
