package changefeatureextractor;

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
			out.write("fileid,commitid,authorid,hour,day,logLength,changecount,bugcount,changeloc,newloc,is_bug_intro\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveToFile(String metafeature) throws IOException {
		out.write(metafeature);
	}

	public void close() throws IOException {
		out.close();
	}

}
