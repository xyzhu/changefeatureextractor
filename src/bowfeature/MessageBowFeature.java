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

public class MessageBowFeature extends BowFeature{

	final static Connection conn = DatabaseManager.getConnection(); // for database
	static final String findMessage = "select message from scmlog where id=?";
	private static PreparedStatement findMessageQuery;
	

	public void buildFeatureMap(ArrayList<String[]> filecommits){
		Iterator<String[]> li = filecommits.iterator();
		String filecommit[] = new String[2];
		String commitid;
		ResultSet messages;
		String message = null;
		while(li.hasNext()){
			filecommit = li.next();
			commitid = filecommit[0];
			try {
				findMessageQuery = conn.prepareStatement(findMessage);
				findMessageQuery.setString(1, commitid);
				messages = findMessageQuery.executeQuery();
				message = "";
				while (messages.next()) {
					message = messages.getString(1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			Map<String, Integer> map = new HashMap<String, Integer>();;
			if(!message.contains("*** empty log message ***")){
				Map<String, Integer> splitmap = findSourceSpliter(message);
				String[] allwords = message.split(spliter);
				map = removeWhite(allwords);
				map.putAll(splitmap);
				addToMaps(map);
			}
			else
				map.put("@@", -1);
			maplist.add(map);
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
//			if(mbowmaps.get(word)!=null){
//				times += mbowmaps.get(word);
//				mbowmaps.replace(word, times);
//			}
//			else
//				mbowmaps.put(word, times);
//
//		}
//	}
//	private Map<String, Integer> findSpliter(String message) {
//		String tempmessage = message;
//		Map<String, Integer> map = new HashMap<String, Integer>();
//		String s[] = {".",";","(",")","{","}","[","]","//","\"","@","<",">","++","--","+","-","!",":",",","#","=","'","/","?"};
//		String spliter;
//		int num = 0;
//		int index = 0;
//		int numTwoAdd = 0;
//		int numTwoSub = 0;
//		int numTwoSlash = 0;
//		for(int i = 0;i< s.length;i++){
//			spliter = s[i];
//			index = tempmessage.indexOf(spliter);
//			while(index!=-1){
//				num++;
//				tempmessage = tempmessage.substring(index+spliter.length());
//				index = tempmessage.indexOf(spliter);
//			}
//			if(spliter.equals("++"))
//				numTwoAdd = num;
//			else if(spliter.equals("--"))
//				numTwoSub = num;
//			else if(spliter.equals("//"))
//				numTwoSlash = num;
//			else if(spliter.equals("+"))
//				num -= numTwoAdd*2;
//			else if(spliter.equals("-"))
//				num -= numTwoSub*2;
//			else if(spliter.equals("/"))
//				num -= numTwoSlash*2;
//			map.put(spliter,num);
//			num = 0;
//			tempmessage = message;
//		}
//		return map;
//	}
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
//		Iterator<Map.Entry<String, Integer>> it = mbowmaps.entrySet().iterator();
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
