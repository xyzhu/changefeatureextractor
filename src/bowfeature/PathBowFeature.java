package bowfeature;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import database.DatabaseManager;

public class PathBowFeature extends BowFeature{

	final static Connection conn = DatabaseManager.getConnection(); // for database
	static final String findPath = "select current_file_path from actions where commit_id=? and file_id=?";
	private static PreparedStatement findPathQuery;

	public void buildFeatureMap(ArrayList<String[]> filecommits){
		Iterator<String[]> li = filecommits.iterator();
		String filecommit[] = new String[2];
		String fileid, commitid;
		ResultSet paths;
		String path = null;
		while(li.hasNext()){
			filecommit = li.next();
			commitid = filecommit[0];
			fileid = filecommit[1];
			try {
				findPathQuery = conn.prepareStatement(findPath);
				findPathQuery.setString(1, commitid);
				findPathQuery.setString(2, fileid);
				paths = findPathQuery.executeQuery();
				path = "";
				while (paths.next()) {
					path = paths.getString(1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			Map<String, Integer> splitmap = findSpliter(path);
			String[] allwords = path.split("/|.java|_|(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])");
			Map<String, Integer> map = removeWhite(allwords);
			map.putAll(splitmap);
			maplist.add(map);
			addToMaps(map);
		}
	}
//	private void addToMaps(Map<String, Integer> bowmap) {
//		Iterator<Map.Entry<String, Integer>> it;
//		Map.Entry<String, Integer> entry;
//		String word;
//		int times;
//		it = bowmap.entrySet().iterator();
//		while(it.hasNext()){
//			entry = (Map.Entry<String, Integer>)it.next();
//			word = entry.getKey();
//			times = bowmap.get(word);
//			if(pbowmaps.get(word)!=null){
//				times += pbowmaps.get(word);
//				pbowmaps.replace(word, times);
//			}
//			else
//				pbowmaps.put(word, times);
//
//		}
//	}
	
	private Map<String, Integer> findSpliter(String content) {
		String tempcontent = content;
		Map<String, Integer> map = new HashMap<String, Integer>();
		String spliter = "/";
		int num = 0;
		int index = 0;
		index = tempcontent.indexOf(spliter);
		while(index!=-1){
			num++;
			tempcontent = tempcontent.substring(index+spliter.length());
			index = tempcontent.indexOf(spliter);
		}
		map.put(spliter,num);
		return map;
	}
	
//	private Map<String, Integer> removeWhite(String[] allwords) {
//		Map<String, Integer> map = new HashMap<String, Integer>();
//		String word;
//		int times;
//		for(int i = 0;i < allwords.length; i++){
//			word = allwords[i];
//			if(!word.equals("")){
//				if(map.get(word)!=null){
//					times = map.get(word);
//					times++;
//					map.replace(word, times);
//				}
//				else
//					map.put(word, 1);
//
//			}
//		}
//		return map;
//	}
//
//	public static String getBowFeature(Map<String, Integer> bowmap) {
//		String bowf = "";
//		Iterator<Map.Entry<String, Integer>> it = pbowmaps.entrySet().iterator();
//		Map.Entry<String, Integer> entry;
//		String key = "";
//		while(it.hasNext()){
//			entry = (Map.Entry<String, Integer>)it.next();
//			key = entry.getKey();
//			if(bowmap.get(key)!=null)
//				bowf += ","+bowmap.get(key);
//			else
//				bowf += ","+0;
//		}
//		return bowf;
//	}

}
