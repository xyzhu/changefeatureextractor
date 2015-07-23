package metafeature;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DatabaseManager;
import Util.Dates;

public class MetaFeature {
	final static Connection conn = DatabaseManager.getConnection(); // for database
	static final String findLog = "select author_id, author_date, message, action_type from scmlog, action_files where scmlog.id=action_files.commit_id and scmlog.id=? and action_files.file_id=?";
	static final String findChangeCount = "select count(*) from scmlog where author_date<?";
	static final String findBugCount = "select count(*) from scmlog where author_date<? and is_bug_fix=1";
	private static PreparedStatement findLogQuery;
	private static PreparedStatement findChangeCountQuery;
	private static PreparedStatement findBugCountQuery;

	public String getFeature(String commitid, String fileid) {
		final ResultSet allMetaFeatures;
		ResultSet historyCount;
		int authorid = -1;
		String authordate = null;
		String message;
		String type = "";
		String hour = "";
		String day = "";
		int changecount = 0;
		int bugcount = 0;
		int logLength = 0;
		try {
			findLogQuery = conn.prepareStatement(findLog);
			findLogQuery.setString(1, commitid);
			findLogQuery.setString(2, fileid);
			allMetaFeatures = findLogQuery.executeQuery();
			while (allMetaFeatures.next()) {
				authorid = allMetaFeatures.getInt(1);
				authordate = allMetaFeatures.getString(2);
				message = allMetaFeatures.getString(3);
				type = allMetaFeatures.getString(4);
				if(message.contains("*** empty log message ***"))
					logLength = 0;
				else
					logLength = message.toString().length();
				hour = Dates.toHourString(authordate);
				day = Dates.toDayString(authordate);
				findChangeCountQuery = conn.prepareStatement(findChangeCount);
				findChangeCountQuery.setString(1, authordate);
				historyCount = findChangeCountQuery.executeQuery();
				while(historyCount.next()){
					changecount = historyCount.getInt(1);
				}
				findBugCountQuery = conn.prepareStatement(findBugCount);
				findBugCountQuery.setString(1, authordate);
				historyCount = findBugCountQuery.executeQuery();
				while(historyCount.next()){
					bugcount = historyCount.getInt(1);
				}
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ","+authorid+","+hour+","+day+","+logLength+","+changecount+","+bugcount+","+type;
	}
}
