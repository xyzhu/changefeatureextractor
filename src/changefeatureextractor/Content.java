package changefeatureextractor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import com.mysql.jdbc.StringUtils;

import Database.DatabaseManager;

public class Content {
	final static Connection conn = DatabaseManager.getConnection();
	static final String findContent = "select content from content where commit_id=? and file_id=?";
	static final String findPatch = "select patch from patches where commit_id=? and file_id=?";
	private static PreparedStatement findContentQuery;
	private static PreparedStatement findPatchQuery;
	String projectname = "";
	ArrayList<String[]> filecommits;
	String commitid, fileid, type;
	String filecommit[] = new String[3];
	FileWrite fw1;
	FileWrite fw2;
	public Content(String project_name, ArrayList<String[]>fcs) {
		projectname = project_name;
		filecommits = fcs;
	}
	public Content() {
	}
	public void save() throws IOException {
		String content, patch, precontent;
		Iterator<String[]> li = filecommits.iterator();
		PreContent preContent;;
		while(li.hasNext()){
			filecommit = li.next();
			commitid = filecommit[0];
			fileid = filecommit[1];
			type = filecommit[2];
			preContent = new PreContent(commitid, fileid);
			content = getContent(commitid, fileid);
			patch = getPatch(commitid, fileid);
			if(patch!=null){			
				fw1 = new FileWrite("files/"+projectname+"_"+commitid+"_"+fileid+".java");
				fw1.saveToFile(content);
				fw1.close();
				if(!type.equals("A")){
					fw2 = new FileWrite("files/"+projectname+"_"+commitid+"_"+fileid+"_pre.java");
					precontent = preContent.getContent(content,patch);
					String lastString = "", preLastString = "";
					int contentLen = content.length();
					int precontentLen = precontent.length();
					lastString = content.substring(contentLen-1, contentLen);
					if(lastString.equals("\r")||lastString.equals("\n")){
						lastString = content.substring(contentLen-2, contentLen);
						preLastString = precontent.substring(precontentLen-1, precontentLen);
						if(lastString.contains("\r")&&!preLastString.equals("\r")){
							precontent += "\r";
						}
						if(lastString.contains("\n")&&!preLastString.equals("\n")){
							precontent += "\n";
						}
					}
					fw2.saveToFile(precontent);
					fw2.close();
				}
			}
			else{
				System.out.println(commitid+"***"+fileid+"---null");
			}
		}		
	}

	public String getContent(String commitid, String fileid){
		final ResultSet contents;
		String content = null;
		try {
			findContentQuery = conn.prepareStatement(findContent);
			findContentQuery.setString(1, commitid);
			findContentQuery.setString(2, fileid);
			contents = findContentQuery.executeQuery();
			while(contents.next()){
				content = contents.getString(1);
			}

		}catch (SQLException e) {
			e.printStackTrace();
		}
		return content;
	}

	public String getPatch(String commitid, String fileid){
		final ResultSet patches;
		String patch = null;
		try {
			findPatchQuery = conn.prepareStatement(findPatch);
			findPatchQuery.setString(1, commitid);
			findPatchQuery.setString(2, fileid);
			patches = findPatchQuery.executeQuery();
			while(patches.next()){
				patch = patches.getString(1);
			}

		}catch (SQLException e) {
			e.printStackTrace();
		}
		return patch;
	}

}
