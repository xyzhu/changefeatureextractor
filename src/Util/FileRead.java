package Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileRead {
	File file;
	BufferedReader in;

	public FileRead(String filename){
		File f = new File(filename);
		try {
			in = new BufferedReader(new FileReader(f));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public ArrayList<String[]> readFromFile() throws IOException {
		ArrayList<String[]> complexities = new ArrayList<String[]>();
		String complexity[] = null;
		String line = "";
		// read util the last line
		while ((line = in.readLine()) != null) {
			complexity = line.split(",");
			complexities.add(complexity);
		}
		return complexities;
	}

	public void close() throws IOException {
		in.close();
	}

}
