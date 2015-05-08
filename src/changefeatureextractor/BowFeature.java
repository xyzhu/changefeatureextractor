package changefeatureextractor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class BowFeature {
	public static LinkedList<Map<String, Integer>> maplist = new LinkedList<Map<String, Integer>>();;
	public static Map<String, Integer> bowmaps = new HashMap<String, Integer>();
	String bowfeature;
	public void buildFeatureMap(ArrayList<String[]> filecommits) throws IOException{
		Content con = new Content();
		Iterator<String[]> li = filecommits.iterator();
		String filecommit[] = new String[2];
		String fileid, commitid;
		while(li.hasNext()){
			filecommit = li.next();
			fileid = filecommit[0];
			commitid = filecommit[1];
			String content = con.getContent(fileid, commitid);
			//System.out.println(content);
			Map<String, Integer> splitmap = findSpliter(content);
			String[] allwords = content.split("\\s+|\\n|\\t|\\.|;|\\(|\\)|\\{|\\}|\\[|\\]|,|//|\"|@|<|>|\\:|\\#|\\=|\\+|\\-|\\!|'|/|\\?");
			Map<String, Integer> map = removeWhite(allwords);
			map.putAll(splitmap);
			maplist.add(map);
			addToMaps(map);
		}
	}
	private void addToMaps(Map<String, Integer> bowmap) {
		Iterator<Map.Entry<String, Integer>> it;
		Map.Entry<String, Integer> entry;
		String word;
		int times;
		it = bowmap.entrySet().iterator();
		while(it.hasNext()){
			entry = (Map.Entry<String, Integer>)it.next();
			word = entry.getKey();
			times = bowmap.get(word);
			if(bowmaps.get(word)!=null){
				times += bowmaps.get(word);
				bowmaps.replace(word, times);
			}
			else
				bowmaps.put(word, times);

		}
	}
	private Map<String, Integer> findSpliter(String content) {
		String tempcontent = content;
		Map<String, Integer> map = new HashMap<String, Integer>();
		String s[] = {".",";","(",")","{","}","[","]","//","\"","@","<",">","++","--","+","-","!",":",",","#","=","'","/","?"};
		String spliter;
		int num = 0;
		int index = 0;
		int numTwoAdd = 0;
		int numTwoSub = 0;
		int numTwoSlash = 0;
		for(int i = 0;i< s.length;i++){
			spliter = s[i];
			index = tempcontent.indexOf(spliter);
			while(index!=-1){
				num++;
				tempcontent = tempcontent.substring(index+spliter.length());
				index = tempcontent.indexOf(spliter);
			}
			if(spliter.equals("++"))
				numTwoAdd = num;
			else if(spliter.equals("--"))
				numTwoSub = num;
			else if(spliter.equals("//"))
				numTwoSlash = num;
			else if(spliter.equals("+"))
				num -= numTwoAdd*2;
			else if(spliter.equals("-"))
				num -= numTwoSub*2;
			else if(spliter.equals("/"))
				num -= numTwoSlash*2;
			map.put(spliter,num);
			num = 0;
			tempcontent = content;
		}
		return map;
	}
	private Map<String, Integer> removeWhite(String[] allwords) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		String word;
		int times;
		for(int i = 0;i < allwords.length; i++){
			word = allwords[i];
			if(!word.equals("")){
				if(map.get(word)!=null){
					times = map.get(word);
					times++;
					map.replace(word, times);
				}
				else
					map.put(word, 1);

			}
		}
		return map;
	}
	public void printBowFeature() throws IOException {
		Iterator<Map.Entry<String, Integer>> it;
		Map.Entry<String, Integer> entry;
		it = bowmaps.entrySet().iterator();
		while(it.hasNext()){
			entry = (Map.Entry<String, Integer>)it.next();
			//			System.out.println(entry.getKey()+"---"+entry.getValue());
		}
	}
	public static String getBowFeature(Map<String, Integer> bowmap) {
		String bowf = "";
		Iterator<Map.Entry<String, Integer>> it = bowmaps.entrySet().iterator();
		Map.Entry<String, Integer> entry;
		String key = "";
		while(it.hasNext()){
			entry = (Map.Entry<String, Integer>)it.next();
			key = entry.getKey();
			if(bowmap.get(key)!=null)
				bowf += bowmap.get(key)+",";
			else
				bowf += 0+",";
		}
		return bowf;
	}

}
