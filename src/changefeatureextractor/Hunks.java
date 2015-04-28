package changefeatureextractor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Database.DatabaseManager;

public class Hunks {
	final static Connection conn = DatabaseManager.getConnection();
	static final String findChangeLoc = "select old_start_line,old_end_line,new_start_line,new_end_line from hunks where file_id=? and commit_id=?";
	private static PreparedStatement findChangeLocQuery;
	public int getChangeLoc(String fileid, String commitid) {
		int oldstart,oldend,newstart,newend,changeloc=0;
		final ResultSet changeLocs;
		try {
			findChangeLocQuery = conn.prepareStatement(findChangeLoc);
			findChangeLocQuery.setString(1, fileid);
			findChangeLocQuery.setString(2, commitid);
			changeLocs = findChangeLocQuery.executeQuery();
			while(changeLocs.next()){
				oldstart = changeLocs.getInt(1);
				oldend = changeLocs.getInt(2);
				newstart = changeLocs.getInt(3);
				newend = changeLocs.getInt(4);
				if(oldstart!=0&&oldend!=0){
					changeloc+= oldend-oldstart+1;
				}
				if(newstart!=0&&newend!=0){
					changeloc+= newend-newstart+1;
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return changeloc;
	}

}
