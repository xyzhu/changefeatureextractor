package changefeatureextractor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Extractor {
	public static void main(String args[]) throws IOException{
		FileWrite fw = new FileWrite("word.txt");
		FileCommit fc = new FileCommit();
		ArrayList<String[]> filecommits = fc.findFileCommit();
		Iterator<String[]> li = filecommits.iterator();
		String filecommit[] = new String[2];
		String fileid, commitid,is_bug_intro;
		String features = "";
		String metafeature = "";
		String bowfeature = "";
		MetaFeature mf = new MetaFeature();
		BowFeature bf = new BowFeature();
		bf.buildFeatureMap(filecommits);
		fw.writeFeatureName();
		Map<String, Integer> bowmap = new HashMap<String, Integer>();
		int recordCount = 0;
		while(li.hasNext()){
			filecommit = li.next();
			fileid = filecommit[0];
			commitid = filecommit[1];
			is_bug_intro = filecommit[2];
			features+=fileid+","+commitid+",";
			metafeature = mf.getFeature(fileid, commitid);
			features += metafeature;
			bowmap = BowFeature.maplist.get(recordCount);
			bowfeature = BowFeature.getBowFeature(bowmap);
			features += bowfeature;
			recordCount++;
			features+=is_bug_intro;
			fw.saveToFile(features+"\n");
			features = "";
			bf.printBowFeature();
		}
		fw.close();
	}

}
