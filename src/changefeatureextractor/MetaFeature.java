package changefeatureextractor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import Util.Dates;
import Database.DatabaseManager;

public class MetaFeature {
	final static Connection conn = DatabaseManager.getConnection(); // for database
	static final String findLog = "select author_id, author_date, message from scmlog where id=?";
	static final String findChangeCount = "select count(*) from scmlog where author_date<?";
	static final String findBugCount = "select count(*) from scmlog where author_date<? and is_bug_fix=1";
	private static PreparedStatement findLogQuery;
	private static PreparedStatement findChangeCountQuery;
	private static PreparedStatement findBugCountQuery;

	public String getFeature(String fileid, String commitid) {
		final ResultSet allMetaFeatures;
		ResultSet historyCount;
		int authorid = -1;
		String authordate = null;
		String message;
		int hour = -1;
		int day = -1;
		int changecount = 0;
		int bugcount = 0;
		int logLength = 0;
		Hunks hunks = new Hunks();
		Content content = new Content();
		int changeloc = 0, newloc = 0;
		try {
			findLogQuery = conn.prepareStatement(findLog);
			findLogQuery.setString(1, commitid);
			allMetaFeatures = findLogQuery.executeQuery();
			while (allMetaFeatures.next()) {
				authorid = allMetaFeatures.getInt(1);
				authordate = allMetaFeatures.getString(2);
				message = allMetaFeatures.getString(3);
				if(message.contains("*** empty log message ***"))
					logLength = 0;
				else
					logLength = message.toString().length();
				hour = Dates.toHour(authordate);
				day = Dates.toDay(authordate);
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
		changeloc = hunks.getChangeLoc(fileid,commitid);
		newloc = content.getNewLoc(fileid,commitid);
		return authorid+","+hour+","+day+","+logLength+","+changecount+","+bugcount+","+changeloc+","+newloc;
	}
}
