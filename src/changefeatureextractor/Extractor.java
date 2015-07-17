package changefeatureextractor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Util.CmdLineParser;

public class Extractor {
	public static void main(String args[]) throws IOException{
		/**
		 * Command line parsing
		 */
		CmdLineParser parser = new CmdLineParser();
		CmdLineParser.Option project_opt = parser.addStringOption('t', "project");
		CmdLineParser.Option savecontent_opt = parser.addBooleanOption('c', "content");
		CmdLineParser.Option srcfeature_opt = parser.addBooleanOption('s', "srcfeature");
		CmdLineParser.Option messagefeature_opt = parser.addBooleanOption('m', "messagefeature");
		CmdLineParser.Option pathfeature_opt = parser.addBooleanOption('p', "pathfeature");
		CmdLineParser.Option complexity_opt = parser.addBooleanOption('f', "complexity");
		CmdLineParser.Option featurename_opt = parser.addBooleanOption('n', "featurename");
		try {
			parser.parse(args);
		} catch (CmdLineParser.OptionException e) {
			System.err.println(e.getMessage());
			//            printUsage();
			System.exit(2);
		}

		String projectname = (String) parser.getOptionValue(project_opt, null);
		Boolean savecontent = (Boolean) parser.getOptionValue(savecontent_opt, false);
		Boolean get_sbow = (Boolean)parser.getOptionValue(srcfeature_opt, false);
		Boolean get_mbow = (Boolean)parser.getOptionValue(messagefeature_opt, false);
		Boolean get_pbow = (Boolean)parser.getOptionValue(pathfeature_opt, false);
		Boolean get_complexity = (Boolean)parser.getOptionValue(complexity_opt, false);
		Boolean write_name = (Boolean)parser.getOptionValue(featurename_opt,false);

		FileCommit fc = new FileCommit();
		ArrayList<String[]> filecommits = fc.findFileCommit();
		if(!savecontent){
			int featureno = 0;
			FileWrite fw = new FileWrite("files/"+projectname+".csv");
			FileRead fr1 = new FileRead("files/Content.csv");
			FileRead fr2 = new FileRead("files/ContentPre.csv");
			Iterator<String[]> li = filecommits.iterator();
			String filecommit[] = new String[4];//used to save one filecommits record
			String commitid, fileid, is_bug_intro;
			String allfeatures = "";
			if(write_name)
				allfeatures = "commit,file,author,hour,day,loglen,changecount,bugcount,type,newloc,addloc,deleteloc";
			String metafeature = "";
			String srcbowfeature = "", messagebowfeature = "", pathbowfeature = "";
			String content = "";
			String patch = "";
			MetaFeature mf = new MetaFeature();
			Content c = new Content();
			CodeLine cl;
			int newLoc, addLoc, deleteLoc;
			ArrayList<String[]> complexities = new ArrayList<String[]>();
			ArrayList<String[]> complexities_pre = new ArrayList<String[]>();
			if(get_complexity){
				complexities = fr1.readFromFile();
				complexities_pre = fr2.readFromFile();
				if(write_name){
					String[] complexity_name = complexities.get(0);
					for(int i=2;i<complexity_name.length;i++){
						allfeatures += ","+complexity_name[i];
						allfeatures += ","+complexity_name[i]+"_delta";
					}
				}
			}
			Map<String, Integer> srcbowmap, messagebowmap, pathbowmap, bowmaps;
			if(get_sbow){
				SrcBowFeature sbf = new SrcBowFeature();
				bowmaps = sbf.buildSrcFeatureMap(filecommits);
				if(write_name){
					Iterator<Map.Entry<String, Integer>> it = bowmaps.entrySet().iterator();
					Map.Entry<String, Integer> entry;
					String key = "";
					while(it.hasNext()){
						entry = (Map.Entry<String, Integer>)it.next();
						key = entry.getKey();
						featureno++;
						allfeatures += ", fff"+featureno;
					}
				}
			}
			if(get_mbow){
				MessageBowFeature mbf = new MessageBowFeature();
				bowmaps = mbf.buildMessageFeatureMap(filecommits);
				if(write_name){
					Iterator<Map.Entry<String, Integer>> it = bowmaps.entrySet().iterator();
					Map.Entry<String, Integer> entry;
					String key = "";
					while(it.hasNext()){
						entry = (Map.Entry<String, Integer>)it.next();
						key = entry.getKey();
						featureno++;
						allfeatures += ", fff"+featureno;
					}
				}
			}
			if(get_pbow){
				PathBowFeature pbf = new PathBowFeature();
				bowmaps = pbf.buildPathFeatureMap(filecommits);
				if(write_name){
					Iterator<Map.Entry<String, Integer>> it = bowmaps.entrySet().iterator();
					Map.Entry<String, Integer> entry;
					String key = "";
					while(it.hasNext()){
						entry = (Map.Entry<String, Integer>)it.next();
						key = entry.getKey();
						featureno++;
						allfeatures += ", fff"+featureno;
					}
				}
			}

			if(write_name)
				allfeatures += ",is_bug_intro\n";
			int srcCount = 0, messageCount = 0, pathCount = 0;
			while(li.hasNext()){
				filecommit = li.next();
				commitid = filecommit[0];
				fileid = filecommit[1];
				//				System.out.println(commitid+"***"+fileid);
				is_bug_intro = filecommit[3];
				allfeatures+=commitid+","+fileid;//to identify the record with fileid and commitid
				metafeature = mf.getFeature(commitid,fileid);
				allfeatures += metafeature;
				content = c.getContent(commitid, fileid);
				patch = c.getPatch(commitid, fileid);
				cl = new CodeLine(content, patch);
				newLoc = cl.countNewLoc();
				addLoc = cl.countAddLoc();
				deleteLoc = cl.countDeleteLoc();
				allfeatures+=","+newLoc+","+addLoc+","+deleteLoc;
				//find complexity and complexity delta for the give commit id and file id
				if(get_complexity){
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
						allfeatures += ","+complexity[j];
						double delta = 0;
						int deltaint = 0;
						if(!newfile){
							delta = (Double.valueOf(complexity_pre[j])-Double.valueOf(complexity[j]));
							if(delta%1==0){
								deltaint = (int)delta;
								allfeatures += ","+deltaint;
							}
							else
								allfeatures += ","+delta;
						}
						else{
							delta = -Double.valueOf(complexity[j]);
							if(delta%1==0){
								deltaint = (int)delta;
								allfeatures += ","+deltaint;
							}
							else
								allfeatures += ","+delta;
						}
					}					
					newfile = true;
				}
				if(get_sbow){
					srcbowmap = SrcBowFeature.maplist.get(srcCount);
					srcbowfeature = SrcBowFeature.getBowFeature(srcbowmap);
					allfeatures += srcbowfeature;
				}
				if(get_mbow){
					messagebowmap = MessageBowFeature.maplist.get(messageCount);
					messagebowfeature = MessageBowFeature.getBowFeature(messagebowmap);
					allfeatures += messagebowfeature;
				}
				if(get_pbow){
					pathbowmap = PathBowFeature.maplist.get(pathCount);
					pathbowfeature = PathBowFeature.getBowFeature(pathbowmap);
					allfeatures += pathbowfeature;
				}
				srcCount++;
				messageCount++;
				pathCount++;
				allfeatures += ","+is_bug_intro;
				fw.saveToFile(allfeatures+"\n");
				allfeatures = "";
			}
			fw.close();
		}
		else{
			Content content = new Content(projectname, filecommits);
			content.save();
		}

	}

//	private static String getname(String key, String source) {
//		String name = key;
//		String signs[] = {".", ";", "(", ")","{","}","[","]",",","//","\"","@","<",">",":","#","=","+","-","!","'","/","?"};
//		//String names[] = {"dot","semicolon","lbrace","rbrace","lbracket","rbracket","lsbracket","rsbracket","comma","dslash","quote","at","colon","pound","equal","add","subtract","exclamination","squote","slash","question"};
//		int len = signs.length;
//		for(int i=0;i<len;i++){
//			if(key.equals(signs[i])){
//				name = source+source+source+i;
//				return name;
//			}
//		}
//		if(key.equals("++"))
//			name = source+source+source+len;
//		if(key.equals("--"))
//			name = source+source+source+(len+1);
//		return name;
//	}
}

