package changefeatureextractor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

public class Extractor {
	public static void main(String args[]) throws IOException{

		FileWrite fw = new FileWrite("result.csv");
		FileCommit fc = new FileCommit();
		fc.findFileCommit();
		Iterator<String[]> li = fc.filecommit.iterator();
		String filecommit[] = new String[2];
		String fileid, commitid,is_bug_intro;
		String metafeature = "";
		MetaFeature mf = new MetaFeature();
		Hunks hunks = new Hunks();
		Content content = new Content();
		int changeLoc = 0, newLoc = 0;
		while(li.hasNext()){
			filecommit = li.next();
			fileid = filecommit[0];
			commitid = filecommit[1];
			is_bug_intro = filecommit[2];
			metafeature+=fileid+","+commitid+",";
			metafeature += mf.getFeature(commitid);
			changeLoc = hunks.getChangeLoc(fileid,commitid);
			newLoc = content.getNewLoc(fileid,commitid);
			metafeature += changeLoc+","+newLoc+","+is_bug_intro+"\n";
			fw.saveToFile(metafeature);
			metafeature = "";
		}
		fw.close();
	}

}
