package com.hp.core.configuration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ModifyConfiguration {
	private static ArrayList<File> filesList = new ArrayList<File>();
	
	public static void main(String[] args) {
		long a = System.currentTimeMillis();
		// retrieve all file in a list 
		String dir = "C:/dev/workspaces/trunk/project/Config/src/main/java";
		String[] fileEndWiths = new String[]{"config.properties"};
		refreshFileList(dir, fileEndWiths);
		
		replace("record.edit.lock.enabled=${record.edit.lock.enabled}", 
				"editlock.enabled=${editlock.enabled}");
		
		System.out.println(System.currentTimeMillis() - a);

	}

	public static void refreshFileList(String strPath, String[] fileEndWiths) {
		File dir = new File(strPath);
		File[] files = dir.listFiles();
		if (files == null)
			return;
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				refreshFileList(files[i].getAbsolutePath());

			} else {
				String strFileName = files[i].getAbsolutePath().toLowerCase();

				if (null == fileEndWiths) {
					System.out.println("---" + files[i].getAbsolutePath());
					filesList.add(files[i]);
				} else {
					for (String fileEndWith : fileEndWiths) {
						if (strFileName.endsWith(fileEndWith.trim())) {
							System.out.println("---" + files[i].getAbsolutePath());
							filesList.add(files[i]);						
						} 
					}
				}
				
			}

		}

	}

	
	public static void refreshFileList(String strPath) {
		File dir = new File(strPath);
		File[] files = dir.listFiles();
		if (files == null)
			return;
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				refreshFileList(files[i].getAbsolutePath());

			} else {
				String strFileName = files[i].getAbsolutePath().toLowerCase();

				if (strFileName.endsWith(".properties")
						|| strFileName.endsWith(".xml")) {
					System.out.println("---" + files[i].getAbsolutePath());
					filesList.add(files[i]);
				}
			}

		}

	}

	public static void replace(String target, String replacement) {
		for (File file : filesList) {
			BufferedReader bufread = null;
			BufferedWriter bufwriter = null;
			String read;
			String readStr = "";
			
			System.out.println(" read " + file.getAbsolutePath());
			FileReader fileread = null;
			try {
				fileread = new FileReader(file);
				bufread = new BufferedReader(fileread);
				final String LINE = System.getProperty("line.separator"); 
				while ((read = bufread.readLine()) != null) {
					if (null == readStr || "".equals(readStr.trim())) {
						readStr = readStr + read;
					} else {
						readStr = readStr + LINE + read;
					}
				}
			} catch (Exception d) {
				d.printStackTrace();
				System.out.println(d.getMessage());
			} finally {
				try {
					if (null != bufread)
						bufread.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (readStr.contains(target)) {
			System.out.println(" replace string [" + target + "] to string [" + replacement + "]");
			readStr = readStr.replace(target, replacement);
			} else {
				System.out.println(" not exist string [" + target + "]");
				continue;
			}
			
		    FileWriter filewriter = null;
			try {
				filewriter = new FileWriter(file, false);
				bufwriter=new BufferedWriter(filewriter);
				System.out.println(" writer " + file.getAbsolutePath());
				bufwriter.write(readStr);				
				bufwriter.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (null != filewriter) {
						filewriter.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		     
		    
		      
		}
	}
}
