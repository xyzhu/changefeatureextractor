package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import content.Content;
import bowfeature.AddSrcBowFeature;
import bowfeature.DelSrcBowFeature;
import bowfeature.MessageBowFeature;
import bowfeature.PathBowFeature;
import bowfeature.SrcBowFeature;
import metafeature.FileCommit;
import Util.CmdLineParser;
import Util.FileRead;
import Util.FileWrite;

public class Extractor {

	public static int featureno = 0;
	FileCommit fc = new FileCommit();
	ArrayList<String[]> filecommits = fc.findFileCommit();
	public SrcBowFeature sbf = null;
	public MessageBowFeature mbf = null;
	public PathBowFeature pbf = null;
	public AddSrcBowFeature abf = null;
	public DelSrcBowFeature dbf = null;
	public FeatureName fn = new FeatureName();
	public FeatureValue fv = new FeatureValue();
	String featurename = "";
	public ArrayList<String[]> complexities = null;
	public ArrayList<String[]> complexities_pre = null;
	String projectname = "";
	Boolean get_complexity, get_sbow, get_abow,get_dbow, get_mbow, get_pbow, write_name;

	public Extractor(String pname, boolean complexity, boolean sbow, boolean abow, boolean dbow, boolean mbow, boolean pbow, boolean wname){
		projectname = pname;
		get_complexity = complexity;
		get_sbow = sbow;
		get_abow = abow;
		get_dbow = dbow;
		get_mbow = mbow;
		get_pbow = pbow;
		write_name = wname;
	}

	public static void main(String args[]) throws IOException{
		/**
		 * Command line parsing
		 */
		CmdLineParser parser = new CmdLineParser();
		CmdLineParser.Option project_opt = parser.addStringOption('t', "project");
		CmdLineParser.Option savecontent_opt = parser.addBooleanOption('c', "content");
		CmdLineParser.Option srcfeature_opt = parser.addBooleanOption('s', "srcfeature");
		CmdLineParser.Option addsrcfeature_opt = parser.addBooleanOption('a',"addsrcfeature");
		CmdLineParser.Option delsrcfeature_opt = parser.addBooleanOption('d',"delsrcfeature");
		CmdLineParser.Option messagefeature_opt = parser.addBooleanOption('m', "messagefeature");
		CmdLineParser.Option pathfeature_opt = parser.addBooleanOption('p', "pathfeature");
		CmdLineParser.Option complexity_opt = parser.addBooleanOption('f', "complexity");
		CmdLineParser.Option featurename_opt = parser.addBooleanOption('n', "featurename");
		CmdLineParser.Option savefeaturename_opt = parser.addBooleanOption('o',"savefeaturename");

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
		Boolean get_abow = (Boolean)parser.getOptionValue(addsrcfeature_opt, false);
		Boolean get_dbow = (Boolean)parser.getOptionValue(delsrcfeature_opt, false);
		Boolean get_mbow = (Boolean)parser.getOptionValue(messagefeature_opt, false);
		Boolean get_pbow = (Boolean)parser.getOptionValue(pathfeature_opt, false);
		Boolean get_complexity = (Boolean)parser.getOptionValue(complexity_opt, false);
		Boolean write_name = (Boolean)parser.getOptionValue(featurename_opt,false);
		Boolean savefeaturename = (Boolean)parser.getOptionValue(savefeaturename_opt, false);

		Extractor ext = new Extractor(projectname, get_complexity, get_sbow, get_abow, get_dbow, get_mbow, get_pbow, write_name);
		if(savecontent){
			ext.saveContent();
		}
		else if(savefeaturename){
			ext.saveFeatureName();
		}
		else
			ext.extract();

	}
	public void saveContent() throws IOException {
		Content content = new Content(projectname, filecommits);
		content.save();
	}
	public void extract() throws IOException {
		FileWrite fw = new FileWrite("files/"+projectname+".csv");
		Iterator<String[]> li = filecommits.iterator();
		String filecommit[] = new String[4];//used to save one filecommits record
		String commitid, fileid, is_bug_intro;
		init();
		if(write_name){
			featurename = extractFeatureName();
			featurename+=", is_bug_intro";
			fw.saveToFile(featurename+"\n");
		}
		int recordCount = 0;//used to find record in the map
		String featurevalue = "";
		while(li.hasNext()){
			filecommit = li.next();
			commitid = filecommit[0];
			fileid = filecommit[1];
			is_bug_intro = filecommit[3];
			featurevalue += commitid+","+fileid;
			featurevalue += extractOneFile(commitid, fileid, recordCount);
			featurevalue += ","+is_bug_intro;
			recordCount++;
			fw.saveToFile(featurevalue+"\n");
			featurevalue = "";
		}
		fw.close();
	}

	public void init() throws IOException {
		if(get_complexity){
			complexities = new ArrayList<String[]>();
			complexities_pre = new ArrayList<String[]>();
			FileRead fr1 = new FileRead("files/Content.csv");
			FileRead fr2 = new FileRead("files/ContentPre.csv");
			complexities = fr1.readFromFile();
			complexities_pre = fr2.readFromFile();
		}
		if(get_sbow){
			sbf = new SrcBowFeature();
			sbf.buildFeatureMap(filecommits);
		}
		if(get_abow){
			abf = new AddSrcBowFeature();
			abf.buildFeatureMap(filecommits);
		}
		if(get_dbow){
			dbf = new DelSrcBowFeature();
			dbf.buildFeatureMap(filecommits);
		}
		if(get_mbow){
			mbf = new MessageBowFeature();
			mbf.buildFeatureMap(filecommits);
		}
		if(get_pbow){
			pbf = new PathBowFeature();
			pbf.buildFeatureMap(filecommits);
		}
	}

	public void saveFeatureName() throws IOException{
		init();
		if(get_sbow)
			saveName("_srcfeature", sbf.bowmaps);
		if(get_abow)
			saveName("_addfeature", abf.bowmaps);
		if(get_dbow)
			saveName("_delfeature", dbf.bowmaps);
		if(get_mbow)
			saveName("_messagefeature",mbf.bowmaps);
		if(get_pbow)
			saveName("_pathfeature", pbf.bowmaps);
	}
	public void saveName(String filename, Map<String, Integer> bmps) throws IOException{
		FileWrite fwriter = new FileWrite("files/"+projectname+filename);
		Map<String, Integer> bowmaps = bmps;
		Iterator<Map.Entry<String, Integer>> it = bowmaps.entrySet().iterator();
		int size = bowmaps.size();
		String key = "", name = "";
		for(int i=0;i<size;i++){
			key = it.next().getKey();
			name += i+", "+key+"\n";
		}
		fwriter.saveToFile(name);
		fwriter.close();
	}

	public String extractFeatureName(){
		String name = "";
		name = fn.getMetaName();
		if(get_complexity)
			name += fn.getComplexityName(complexities, complexities_pre);
		if(get_sbow)
			name += fn.getBowFeatureName(sbf.bowmaps);	
		if(get_abow)
			name += fn.getBowFeatureName(abf.bowmaps);
		if(get_dbow)
			name += fn.getBowFeatureName(dbf.bowmaps);
		if(get_mbow)
			name += fn.getBowFeatureName(mbf.bowmaps);
		if(get_pbow)
			name += fn.getBowFeatureName(pbf.bowmaps);
		return name;
	}

	public String getFeatureName(){
		return featurename;
	}
	public String extractOneFile(String commitid, String fileid, int recordCount){
		String featurevalue = "";
		featurevalue += fv.getMetaValue(commitid, fileid);
		//find complexity and complexity delta for the give commit id and file id
		if(get_complexity){
			featurevalue += fv.getComplexityValue(complexities, complexities_pre, commitid, fileid);
		}
		if(get_sbow){
			featurevalue += sbf.getBowFeatureValue(sbf.maplist.get(recordCount));
		}
		if(get_abow){
			featurevalue += abf.getBowFeatureValue(abf.maplist.get(recordCount));
		}
		if(get_dbow){
			featurevalue += dbf.getBowFeatureValue(dbf.maplist.get(recordCount));
		}
		if(get_mbow){
			featurevalue += mbf.getBowFeatureValue(mbf.maplist.get(recordCount));
		}
		if(get_pbow){
			featurevalue += pbf.getBowFeatureValue(pbf.maplist.get(recordCount));
		}
		return featurevalue;
	}
}

