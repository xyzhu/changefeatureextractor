package changefeatureextractor;

public class CodeLine {
	public String content;
	String[] patchLines;
	int patchLen;

	public CodeLine(String content, String patch) {
		this.content = content;
		patch = patch.replaceAll("^---.*\n", "");//delete the lines starting with "---"
		patch = patch.replaceAll("^\\+\\+\\+.*\n", "");//delete the lines starting with "+++"
		patchLines = patch.split("\n");
		patchLen = patchLines.length;
	}

	public int countNewLoc() {
		int newloc = 0;
		for(int i=0;i<content.length();i++){
			if((content.charAt(i))=='\n'){
				newloc++;
			}
		}
		return newloc+1;
	}

	public int countAddLoc() {
		int addLoc = 0;
		for(int i=0;i<patchLen;i++){
			if(patchLines[i].startsWith("+"))
				addLoc++;
		}
		return addLoc;
	}

	public int countDeleteLoc() {
		int delLoc = 0;
		for(int i=0;i<patchLen;i++){
			if(patchLines[i].startsWith("-"))
				delLoc++;
		}
		return delLoc;
	}
}
