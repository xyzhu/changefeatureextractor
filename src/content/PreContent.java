package content;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PreContent {
	ArrayList<int[]> seps = new ArrayList<int[]>();
	ArrayList<String> preContentLines = new ArrayList<String>();
	String[] contentLines;
	String hunks[];
	String commitid, fileid;

	public PreContent(String commitid, String fileid) {
		this.commitid = commitid;
		this.fileid = fileid;
	}

	public String getContent(String content, String patch) {
		contentLines = content.split("\n");
		patch = patch.replaceAll("^---.*\n", "");//delete the lines starting with "---"
		patch = patch.replaceAll("^\\+\\+\\+.*\n", "");//delete the lines starting with "+++"
		Pattern p = Pattern.compile("@@.*@@");
		Matcher m = p.matcher(patch);
		int startIndex, endIndex;
		String sep;
		hunks = patch.split("@@.*@@.*");//use such patterns to split patch into change hunks
		//add startline number 0 to seps to starting with the first line of the content
		int startlineno[] = new int[2];
		startlineno[0]=0;
		startlineno[1]=0;
		seps.add(startlineno);
		while(m.find()){
			startIndex = m.start();
			endIndex = m.end();
			sep = patch.substring(startIndex, endIndex);//get a splitter
			addSep(sep);
		}
		//add end line number which the length(content)+1 to seps
		int endlineno[] = new int[2];
		//endlineno[0] is the start of the next hunk, so it should be 1 more than the unchanged one
		endlineno[0] = contentLines.length+1;
		endlineno[1] = 0;
		seps.add(endlineno);
		generatePreContent();
		return genContent();
	}

	//transform the string array to a long string that is the precontent
	private String genContent() {
		String c = "";
		int len = preContentLines.size();
		for(int i=0;i<len-1;i++){
			c+=preContentLines.get(i)+"\n";
		}
		c+=preContentLines.get(len-1);
		return c;
	}

	/*
	 * copy the unchanged code from content to precontent
	 * copy the unchanged code and the deleted code in the change hunks to precontent
	 */
	private void generatePreContent() {
		int hunkslen = hunks.length;
		int presep[], cursep[];
		String hunk;
		String oldhunkLines[];
		int unchangeStartNo = 0, unchangeEndNo = 0;
		for(int i=0;i<hunkslen-1;i++){
			presep = seps.get(i);
			unchangeStartNo = presep[1];
			cursep = seps.get(i+1);
			unchangeEndNo = cursep[0]-1;
			for(int j=unchangeStartNo;j<unchangeEndNo;j++){
				preContentLines.add(contentLines[j]);
			}
			hunk = hunks[i+1];// the 0th hunk is null
			oldhunkLines = hunk.split("\n");
			String oldhunkline;
			int oldhunklen = oldhunkLines.length;
			for(int k=1;k<oldhunklen;k++){
				oldhunkline = oldhunkLines[k];
				if(oldhunkline.startsWith("-")){
					oldhunkline = oldhunkline.replaceAll("^-", " ");
				}
				if(oldhunkline.startsWith("+")){
					continue;
				}
				preContentLines.add(oldhunkline);
			}
		}
		presep = seps.get(seps.size()-2);
		cursep = seps.get(seps.size()-1);//
		for(int j=presep[1]+1;j<cursep[0];j++){
			preContentLines.add(contentLines[j-1]);
		}
	}

	/*
	 * get the new start line number and new end line number
	 * from the separator
	 */
	private void addSep(String sep) {
		sep=sep.replaceAll("@|-| ", "");
		String[] linenos = sep.split(",|\\+");
		int lineno[] = new int[2];
		lineno[0] = Integer.valueOf(linenos[2]);
		lineno[1] = lineno[0]+Integer.valueOf(linenos[3])-1;
		seps.add(lineno);

	}

}
