package bowfeature;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import content.Content;

public class AddSrcBowFeature extends BowFeature{

	public void buildFeatureMap(ArrayList<String[]> filecommits){
		Content con = new Content();
		Iterator<String[]> li = filecommits.iterator();
		String filecommit[] = new String[2];
		String commitid, fileid, addLine;
		Map<String, Integer> map;
		while(li.hasNext()){
			filecommit = li.next();
			commitid = filecommit[0];
			fileid = filecommit[1];
			String patch = con.getPatch(commitid, fileid);
			addLine = getAddLine(patch);//get the added lines of code
			Map<String, Integer> splitmap = findSourceSpliter(addLine);
			String[] allwords = addLine.split(spliter);
			map = removeWhite(allwords);
			map.putAll(splitmap);
			maplist.add(map);
			addToMaps(map);
		}
	}

	/*
	 * get the added lines of code from patch, that are lines of code starting with +
	 */
	private String getAddLine(String patch) {
		String addedlines = "";
		String codelines[] = patch.split("\n");
		int len = codelines.length;
		for(int i=0;i<len;i++){
			if(codelines[i].startsWith("+")&&!codelines[i].startsWith("+++"))
				addedlines += codelines[i].substring(1, codelines[i].length());
		}
		return addedlines;
	}
}
