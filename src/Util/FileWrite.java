package Util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class FileWrite {
	File file;
	BufferedWriter out;
	
	public FileWrite(String filename) {
		file = new File (filename);
		try {
			out = new BufferedWriter(new FileWriter(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveToFile(String feature) throws IOException {
		out.write(feature);
	}

	public void close() throws IOException {
		out.close();
	}

//	public void writeFeatureName() {
//		String featurename = "";
//		Iterator<Map.Entry<String, Integer>> it;
//		Map.Entry<String, Integer> entry;
//		it = SrcBowFeature.sbowmaps.entrySet().iterator();
//		while(it.hasNext()){
//			entry = (Map.Entry<String, Integer>)it.next();
//			featurename += entry.getKey()+",";
//		}
//		try {
//			out.write("fileid,commitid,authorid,hour,day,logLength,changecount,bugcount,changeloc,newloc,"+featurename+"is_bug_intro\n");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

}
