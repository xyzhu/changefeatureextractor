package changefeatureextractor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BowFeature {

	String bowfeature;
	public void getFeature(String fileid, String commitid) throws IOException {
		FileWrite fw = new FileWrite("words.txt");
		Content con = new Content();
		String content = con.getContent(fileid, commitid);
		System.out.println(content);
		String[] allwords = content.split("\\s+|\\n|\\t|\\.|;|\\(|\\)|\\{|\\}|\\[|\\]|,|//|\"|@|<|>|\\+\\+|\\-\\-|\\:");
		Map<String, Integer> map = removeWhite(allwords);
		System.out.println(allwords.length);
		Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator();
		Map.Entry<String, Integer> entry;
		while(it.hasNext()){
			entry = (Map.Entry<String, Integer>)it.next();
			fw.saveToFile(entry.getKey()+":"+entry.getValue()+"\n");
		}
		fw.close();
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

}
