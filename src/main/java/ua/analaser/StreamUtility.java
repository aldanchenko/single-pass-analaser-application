package ua.analaser;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtility {
		
	private static StreamUtility instance = null;
	
	private String filePath = "test.txt";
	
	private InputStream inputStream;
	
	private int lastChar = -1;
	
	private int count = -1;
	
	private String fileContent;
	
	private boolean isUnget = false;
	
	public int readNextChar() throws IOException {			
		
		if (!isUnget) {
			lastChar = this.inputStream.read();
			count++;
		} else {
			isUnget = false;
		}
				
		return lastChar;
	}
	
	public void ungetc(char ch) throws IOException {
		isUnget = true;
	}
	
	private StreamUtility() {
		/*try {
			this.inputStream = new FileInputStream(this.filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}*/
	}
	
	public void setAsStream(String content) {
		this.inputStream = new ByteArrayInputStream(content.getBytes());
		this.count = -1;
        this.lastChar = -1;
        this.isUnget = false;
	}
	
	public static StreamUtility getInstance() {
		if (instance == null) {
			instance = new StreamUtility();
		}
		
		return instance;
	}
	
	public String readFile(String fileName) throws IOException {
		InputStream inputStream = new FileInputStream(fileName);
		StringBuffer stringBuffer = new StringBuffer();
		
		int ch = 0;
		while ((ch = inputStream.read()) != -1) {
			stringBuffer.append((char) ch);
		}
		
		return stringBuffer.toString();
	}
	
	public String getFileContent() {
		return this.fileContent;
	}
	
	public void setFileContent(String content) {
		this.fileContent = content;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCount() {
		return count;
	}
}
