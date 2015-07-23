package bowfeature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class BowFeature {

	public LinkedList<Map<String, Integer>> maplist = new LinkedList<Map<String, Integer>>();;
	public Map<String, Integer> bowmaps = new HashMap<String, Integer>();//consisting features of all the files
	String bowfeature;
	String spliter = "\\s+|\\n|\\t|\\.|;|\\(|\\)|\\{|\\}|\\[|\\]|,|//|\"|@|<|>|\\:|\\#|\\=|\\+|\\-|\\!|'|/|\\?";

	/*
	 * find the number of spliters
	 */
	public Map<String, Integer> findSourceSpliter(String content) {
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
				//tempcontent is the left string after the current spliter, that is to
				//say, new time we find spliter in tempcontent only
				tempcontent = tempcontent.substring(index+spliter.length());
				index = tempcontent.indexOf(spliter);
			}
			//the left if else code is used to subtract number of "++/--" from number of "+/-"
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

	/*
	 * some of the words after splitting contains only whitespace, so
	 * use this method to remove them
	 */
	public Map<String, Integer> removeWhite(String[] allwords) {
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

	/*
	 * all the bowmap of the current file to bowmaps
	 */
	public void addToMaps(Map<String, Integer> bowmap) {
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
	/*
	 * generating the feature vector according to the bowmap and the bowmaps which contains all the words
	 */
	public String getBowFeatureValue(Map<String, Integer> bowmap) {
		String bowf = "";
		Iterator<Map.Entry<String, Integer>> it = bowmaps.entrySet().iterator();
		Map.Entry<String, Integer> entry;
		String key = "";
		while(it.hasNext()){
			entry = (Map.Entry<String, Integer>)it.next();
			key = entry.getKey();
			//if a word in the bowmaps exists in the bowmap, then its number is the corresponding key
			//else, its number is 0
			if(bowmap.get(key)!=null)
				bowf += ","+bowmap.get(key);
			else
				bowf += ","+0;
		}
		return bowf;
	}

	public void buildFeatureMap(ArrayList<String[]> filecommits){};

}
