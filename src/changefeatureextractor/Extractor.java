package changefeatureextractor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Extractor {
	public static void main(String args[]) throws IOException{
		String project_name = args[0];//different project use different file name
		boolean get_content = Boolean.valueOf(args[1]);//content is too big, so save it to separate file
		boolean get_bow = Boolean.valueOf(args[2]);
		FileCommit fc = new FileCommit();
		ArrayList<String[]> filecommits = fc.findFileCommit();
		if(!get_content){
			FileWrite fw = new FileWrite("files/"+project_name+".csv");
			Iterator<String[]> li = filecommits.iterator();
			String filecommit[] = new String[4];//used to save one filecommits record
			String fileid, commitid, is_bug_intro;
			String allfeatures = "";
			String metafeature = "";
			String bowfeature = "";
			String content = "";
			String patch = "";
			MetaFeature mf = new MetaFeature();
			Content c = new Content();
			CodeLine cl;
			int newLoc, addLoc, deleteLoc;
			Map<String, Integer> bowmap;
			if(get_bow){
				BowFeature bf = new BowFeature();
				bf.buildFeatureMap(filecommits);
				bowmap = new HashMap<String, Integer>();
			}
			int recordCount = 0;
			while(li.hasNext()){
				filecommit = li.next();
				commitid = filecommit[0];
				fileid = filecommit[1];
				System.out.println(commitid+"***"+fileid);
				is_bug_intro = filecommit[3];
				allfeatures+=fileid+","+commitid+",";//to identify the record with fileid and commitid
				metafeature = mf.getFeature(commitid,fileid);
				allfeatures += metafeature;
				recordCount++;
				content = c.getContent(commitid, fileid);
				patch = c.getPatch(commitid, fileid);
				cl = new CodeLine(content, patch);
				newLoc = cl.countNewLoc();
				addLoc = cl.countAddLoc();
				deleteLoc = cl.countDeleteLoc();
				allfeatures+=newLoc+","+addLoc+","+deleteLoc+",";
				if(get_bow){
					bowmap = BowFeature.maplist.get(recordCount);
					bowfeature = BowFeature.getBowFeature(bowmap);
					allfeatures += bowfeature;
				}
				allfeatures+=is_bug_intro+",";
				fw.saveToFile(allfeatures+"\n");
				allfeatures = "";
			}
			fw.close();
		}
		else{
			Content content = new Content(project_name, filecommits);
			content.save();
		}

	}

}
