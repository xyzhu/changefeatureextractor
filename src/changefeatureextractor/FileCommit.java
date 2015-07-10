/*
 * This file is to get all the commit and file from content, which are one
 * we want extract features. Get if a commit-file introduces a bug from
 * file_commit
 * zxy-2015.6.30
 */
package changefeatureextractor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Database.DatabaseManager;

public class FileCommit {
	final static Connection conn = DatabaseManager.getConnection(); // for database
	static final String findFileCommit = "select fc.commit_id, fc.file_id, af.action_type from file_commit fc, content c, action_files af "
			+ "where fc.commit_id=c.commit_id and fc.file_id=c.file_id and af.commit_id=c.commit_id and af.file_id=c.file_id ";
	static final String findIsBugIntro = "select is_bug_intro from file_commit where commit_id=? and file_id=?";
	private static PreparedStatement findFileCommitQuery;
	private static PreparedStatement findIsBugIntroQuery;
	ArrayList<String[]> filecommits = new ArrayList<String[]>();
	public ArrayList<String[]> findFileCommit(){
		final ResultSet allFileCommit;
		ResultSet isBugIntro;
		try {
			findFileCommitQuery = conn.prepareStatement(findFileCommit);
			findIsBugIntroQuery = conn.prepareStatement(findIsBugIntro);
			allFileCommit = findFileCommitQuery.executeQuery();
			String record[];
			while (allFileCommit.next()) {
				record = new String[4];
				record[0] = allFileCommit.getString(1);
				record[1] = allFileCommit.getString(2);
				record[2] = allFileCommit.getString(3);

				findIsBugIntroQuery.setString(1,record[0]);
				findIsBugIntroQuery.setString(2, record[1]);
				isBugIntro = findIsBugIntroQuery.executeQuery();
				while(isBugIntro.next()){
					record[3] = Boolean.toString(isBugIntro.getBoolean(1));
				}
				filecommits.add(record);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return filecommits;
	}
}
