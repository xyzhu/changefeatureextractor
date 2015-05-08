package changefeatureextractor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Database.DatabaseManager;

public class FileCommit {
	final static Connection conn = DatabaseManager.getConnection(); // for database
	static final String findFileCommit = "select file_id, commit_id from content";
	static final String findIsBugIntro = "select is_bug_intro from file_commit where file_id=? and commit_id=?";
	private static PreparedStatement findFileCommitQuery;
	private static PreparedStatement findIsBugIntroQuery;
	ArrayList<String[]> filecommits = new ArrayList<String[]>();
	public ArrayList<String[]> findFileCommit(){
		final ResultSet allFileCommit;
		ResultSet isBugIntro;
		try {
			findFileCommitQuery = conn.prepareStatement(findFileCommit);
			findIsBugIntroQuery = conn.prepareStatement(findIsBugIntro);
			//findFileCommitQuery.setInt(1, 10);
			allFileCommit = findFileCommitQuery.executeQuery();
			String record[];
			while (allFileCommit.next()) {
				record = new String[3];
				record[0] = allFileCommit.getString(1);
				record[1] = allFileCommit.getString(2);

				findIsBugIntroQuery.setString(1,record[0]);
				findIsBugIntroQuery.setString(2, record[1]);
				isBugIntro = findIsBugIntroQuery.executeQuery();
				while(isBugIntro.next()){
					record[2] = Boolean.toString(isBugIntro.getBoolean(1));
				}
				filecommits.add(record);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return filecommits;
	}
}
