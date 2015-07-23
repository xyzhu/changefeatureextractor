package main;

import java.util.ArrayList;

import content.Content;
import metafeature.CodeLine;
import metafeature.MetaFeature;

public class FeatureValue {

	public String getMetaValue(String commitid, String fileid) {
		String metavalue = "";
		MetaFeature mf = new MetaFeature();
		Content c = new Content();
		CodeLine cl;
		metavalue += mf.getFeature(commitid,fileid);
		String content = c.getContent(commitid, fileid);
		String patch = c.getPatch(commitid, fileid);
		cl = new CodeLine(content, patch);
		int newLoc = cl.countNewLoc();
		int addLoc = cl.countAddLoc();
		int deleteLoc = cl.countDeleteLoc();
		metavalue +=","+newLoc+","+addLoc+","+deleteLoc;
		return metavalue;

	}

	public String getComplexityValue(ArrayList<String[]> complexities,
			ArrayList<String[]> complexities_pre, String commitid, String fileid) {
		String featurevalue = "";
		String complexity[] = null, complexity_pre[] = null, filenamesplits[] = null;
		String filename = "";
		@SuppressWarnings("null")
		int len = 0;
		String cid, fid;
		for(int i=1;i<complexities.size();i++){
			complexity = complexities.get(i);
			//to get the commit id and file id of the current record
			filename = complexity[1];
			filenamesplits = filename.split("_|\\.");
			cid = filenamesplits[1];
			fid = filenamesplits[2];
			len = complexity.length;
			if(cid.equals(commitid)&&fid.equals(fileid)){
				break;
			}
		}
		boolean newfile = true;//the file is not newly added, so it has previous file content
		for(int i=1;i<complexities_pre.size();i++){
			complexity_pre = complexities_pre.get(i);
			filename = complexity_pre[1];
			filenamesplits = filename.split("_|\\.");
			cid = filenamesplits[1];
			fid = filenamesplits[2];
			len = complexity_pre.length;
			if(cid.equals(commitid)&&fid.equals(fileid)){
				newfile = false;
				break;
			}
		}
		for(int j=2;j<len;j++){
			featurevalue += ","+complexity[j];
			double delta = 0;
			int deltaint = 0;
			if(!newfile){
				delta = (Double.valueOf(complexity_pre[j])-Double.valueOf(complexity[j]));
				if(delta%1==0){
					deltaint = (int)delta;
					featurevalue += ","+deltaint;
				}
				else
					featurevalue += ","+delta;
			}
			else{
				delta = -Double.valueOf(complexity[j]);
				if(delta%1==0){
					deltaint = (int)delta;
					featurevalue += ","+deltaint;
				}
				else
					featurevalue += ","+delta;
			}		
			newfile = true;
		}			
		return featurevalue;
	}

}