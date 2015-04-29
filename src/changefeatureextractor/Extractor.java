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
		String features = "";
		String metafeature = "";
		String bowfeature = "";
		MetaFeature mf = new MetaFeature();
		BowFeature bf = new BowFeature();
		while(li.hasNext()){
			filecommit = li.next();
			fileid = filecommit[0];
			commitid = filecommit[1];
			is_bug_intro = filecommit[2];
			features+=fileid+","+commitid+",";
			metafeature = mf.getFeature(fileid, commitid);
			features += metafeature;
			features+=is_bug_intro;
			bf.getFeature(fileid,commitid);
			features += bowfeature;
			fw.saveToFile(features);
		}
		fw.close();
	}

}
