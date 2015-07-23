package bowfeature;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import content.Content;

public class DelSrcBowFeature extends BowFeature{
	
	public void buildFeatureMap(ArrayList<String[]> filecommits){
		Content con = new Content();
		Iterator<String[]> li = filecommits.iterator();
		String filecommit[] = new String[2];
		String commitid, fileid, delLine;
		while(li.hasNext()){
			filecommit = li.next();
			commitid = filecommit[0];
			fileid = filecommit[1];
			String patch = con.getPatch(commitid, fileid);
			delLine = getDelLine(patch);//get the added lines of code
			Map<String, Integer> splitmap = findSourceSpliter(delLine);
			String[] allwords = delLine.split(spliter);
			Map<String, Integer> map = removeWhite(allwords);
			map.putAll(splitmap);
			maplist.add(map);
			addToMaps(map);
		}
	}


	/*
	 * get the deleted lines of code from patch, that are lines of code starting with -
	 */
	private String getDelLine(String patch) {
		String deletedlines = "";
		String codelines[] = patch.split("\n");
		int len = codelines.length;
		for(int i=0;i<len;i++){
			if(codelines[i].startsWith("-")&&!codelines[i].startsWith("---"))
				deletedlines += codelines[i].substring(1, codelines[i].length());
		}
		return deletedlines;
	}
}
