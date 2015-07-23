package main;

import java.util.ArrayList;
import java.util.Map;

public class FeatureName {

	public String getComplexityName(ArrayList<String[]> complexities,
			ArrayList<String[]> complexities_pre) {
		String name = "";
		String[] complexity_names = complexities.get(0);
		for(int i=2;i<complexity_names.length;i++){
			name += ","+complexity_names[i];
			name += ","+complexity_names[i]+"_delta";
		}
		return name;
	}

	public String getMetaName() {
		return "commit,file,author,hour,day,loglen,changecount,bugcount,type,newloc,addloc,deleteloc";
	}

	public String getBowFeatureName(Map<String, Integer> bowmaps) {
		String name = "";
//		Iterator<Map.Entry<String, Integer>> it = bowmaps.entrySet().iterator();
		int size = bowmaps.size();
		for(int i=0;i<size;i++){
			Extractor.featureno++;
			name += ", fff"+Extractor.featureno;
		}
		return name;
	}

}
