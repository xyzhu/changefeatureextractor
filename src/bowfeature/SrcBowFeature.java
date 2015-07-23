package bowfeature;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import content.Content;

public class SrcBowFeature extends BowFeature{
	
	public void buildFeatureMap(ArrayList<String[]> filecommits){
		Content con = new Content();
		Iterator<String[]> li = filecommits.iterator();
		String filecommit[] = new String[2];
		String commitid, fileid;
		while(li.hasNext()){
			filecommit = li.next();
			commitid = filecommit[0];
			fileid = filecommit[1];
			String content = con.getContent(commitid, fileid);
			Map<String, Integer> splitmap = findSourceSpliter(content);
			String[] allwords = content.split(spliter);
			Map<String, Integer> map = removeWhite(allwords);
			map.putAll(splitmap);
			maplist.add(map);
			addToMaps(map);
		}
	}
	//	/*
//	 * all the bowmap of the current file to bowmaps
//	 */
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
//			if(sbowmaps.get(word)!=null){
//				times += sbowmaps.get(word);
//				sbowmaps.replace(word, times);
//			}
//			else
//				sbowmaps.put(word, times);
//
//		}
//	}
	
//	/*
//	 * find the number of spliters
//	 */
//	private Map<String, Integer> findSpliter(String content) {
//		String tempcontent = content;
//		Map<String, Integer> map = new HashMap<String, Integer>();
//		String s[] = {".",";","(",")","{","}","[","]","//","\"","@","<",">","++","--","+","-","!",":",",","#","=","'","/","?"};
//	String spliter;
//		int num = 0;
//		int index = 0;
//		int numTwoAdd = 0;
//		int numTwoSub = 0;
//		int numTwoSlash = 0;
//		for(int i = 0;i< s.length;i++){
//			spliter = s[i];
//			index = tempcontent.indexOf(spliter);
//			while(index!=-1){
//				num++;
//				//tempcontent is the left string after the current spliter, that is to
//				//say, new time we find spliter in tempcontent only
//				tempcontent = tempcontent.substring(index+spliter.length());
//				index = tempcontent.indexOf(spliter);
//			}
//			//the left if else code is used to subtract number of "++/--" from number of "+/-"
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
//			tempcontent = content;
//		}
//		return map;
//	}
//	
//	/*
//	 * some of the words after splitting contains only whitespace, so
//	 * use this method to remove them
//	 */
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
//	/*
//	 * generating the feature vector according to the bowmap and the bowmaps which contains all the words
//	 */
//	public static String getBowFeature(Map<String, Integer> bowmap) {
//		String bowf = "";
//		Iterator<Map.Entry<String, Integer>> it = sbowmaps.entrySet().iterator();
//		Map.Entry<String, Integer> entry;
//		String key = "";
//		while(it.hasNext()){
//			entry = (Map.Entry<String, Integer>)it.next();
//			key = entry.getKey();
//			//if a word in the bowmaps exists in the bowmap, then its number is the corresponding key
//			//else, its number is 0
//			if(bowmap.get(key)!=null)
//				bowf += ","+bowmap.get(key);
//			else
//				bowf += ","+0;
//		}
//		return bowf;
//	}

}
