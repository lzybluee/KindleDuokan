import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Vector;


class Main {
	
	static boolean DELETE_FILE = true;

	static String mobiFolder;
	static HashMap<String, Vector<String>> mobiList = new HashMap<String, Vector<String>>();
	static HashMap<String, Long> mobiSizeList = new HashMap<String, Long>();
	
	static String epubFolder;
	static HashMap<String, Vector<String>> epubList = new HashMap<String, Vector<String>>();
	static HashMap<String, Long> epubSizeList = new HashMap<String, Long>();
	static HashMap<String, String> epubNameList = new HashMap<String, String>();

	public static void main(String[] args) {
		File[] drivers = File.listRoots();
		for(File d : drivers) {
			if(new File(d + "DK_BookStore").exists()) {
				mobiFolder = d + "documents\\";
				epubFolder = d + "DK_BookStore\\";
			}
		}
		if(epubFolder == null) {
			System.out.println("No Kindle found!");
			return;
		}

		processMobi();
		processEpub();
	}
	
	static void delete(File file) {
		if(file.isDirectory()) {
			for(File f : file.listFiles()) {
				System.out.println("delete " + f.getAbsolutePath());
				delete(f);
			}
			System.out.println("delete " + file.getAbsolutePath());
			file.delete();
		} else {
			System.out.println("delete " + file.getAbsolutePath());
			file.delete();
		}
	}
	
	static void processMobi() {
		mobiClean(mobiFolder);
		
		System.out.println();
		
		for(Entry<String, Vector<String>> entry : mobiList.entrySet()) {
			String book = entry.getKey();
			Vector<String> v = entry.getValue();
			if(v.size() < 3) {
				if(v.contains(".mobi.dir") && !v.contains(".mobi")) {
					System.out.println(book + ".mobi.dir");
					if(DELETE_FILE) {
						delete(new File(mobiFolder + book + ".mobi.dir"));
					}
				}
				if(v.contains(".sdr") && !v.contains(".mobi")) {
					System.out.println(book + ".sdr");
					if(DELETE_FILE) {
						delete(new File(mobiFolder + book + ".sdr"));
					}
				}
			}
		}
		
		ArrayList<HashMap.Entry<String, Long>> list = new ArrayList<HashMap.Entry<String, Long>>(mobiSizeList.entrySet());
		Collections.sort(list, new Comparator<HashMap.Entry<String, Long>>() {

			@Override
			public int compare(HashMap.Entry<String, Long> o1, HashMap.Entry<String, Long> o2) {
				if(o1.getValue() < o2.getValue()) {
					return 1;
				} else {
					return -1;
				}
			}
		});
		for(HashMap.Entry<String, Long> entry : list) {
			System.out.println(entry.getKey() + ".mobi -> " + String.format("%.2f", entry.getValue() / 1024.0f / 1024.0f) + "MB");
		}
	}
	
	static String addItem(HashMap<String, Vector<String>> map, String name, String type) {
		name = name.substring(0, name.length() - type.length());
		if(!map.containsKey(name)) {
			Vector<String> v = new Vector<String>();
			v.add(type);
			map.put(name, v);
		} else {
			map.get(name).add(type);
		}
		return name;
	}

	static void mobiClean(String path) {
		File folder = new File(path);
		for(File f : folder.listFiles()) {
			String name = f.getAbsolutePath();
			if(name.endsWith(".mobi")) {
				String key = addItem(mobiList, name.substring(mobiFolder.length()), ".mobi");
				mobiSizeList.put(key, f.length());
			} else if(name.endsWith(".mobi.dir")) {
				addItem(mobiList, name.substring(mobiFolder.length()), ".mobi.dir");
			} else if(name.endsWith(".sdr")) {
				addItem(mobiList, name.substring(mobiFolder.length()), ".sdr");
			} else if(f.isDirectory()) {
				mobiClean(f.getAbsolutePath());
			} else {
				if(name.endsWith(".txt") || name.endsWith(".azw") || name.endsWith(".epub")) {
					System.out.println(name.substring(3));
				}
			}
		}
	}
	
	static String getEpubName(File file) {
		String name = "";
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine()) != null) {
				if(line.startsWith("book_title=")) {
					name = line.substring(line.indexOf('=') + 1);
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return name;
	}
	
	static void epubClean(String path) {
		File folder = new File(path);
		for(File f : folder.listFiles()) {
			String name = f.getAbsolutePath();
			if(name.endsWith(".info")) {
				String key = addItem(epubList, name.substring(epubFolder.length()), ".info");
				epubNameList.put(key, getEpubName(f));
			} else if(name.endsWith(".epub")) {
				String key = addItem(epubList, name.substring(epubFolder.length()), ".epub");
				epubSizeList.put(key, f.length());
			} else if(name.endsWith(".epub.dir")) {
				addItem(epubList, name.substring(epubFolder.length()), ".epub.dir");
			} else {
				System.out.println(name);
			}
		}
	}
	
	static void processEpub() {
		epubClean(epubFolder);
		
		System.out.println();
		
		for(Entry<String, Vector<String>> entry : epubList.entrySet()) {
			String book = entry.getKey();
			Vector<String> v = entry.getValue();
			if(v.size() < 3) {
				if(v.contains(".epub.dir") && !v.contains(".epub")) {
					System.out.println(book + ".epub.dir");
					if(DELETE_FILE) {
						delete(new File(epubFolder + book + ".epub.dir"));
					}
				}
				if(v.contains(".info") && !v.contains(".epub")) {
					System.out.println(book + ".info");
					if(DELETE_FILE) {
						delete(new File(epubFolder + book + ".info"));
					}
				}
			}
		}
		
		ArrayList<HashMap.Entry<String, Long>> list = new ArrayList<HashMap.Entry<String, Long>>(epubSizeList.entrySet());
		Collections.sort(list, new Comparator<HashMap.Entry<String, Long>>() {

			@Override
			public int compare(HashMap.Entry<String, Long> o1, HashMap.Entry<String, Long> o2) {
				if(o1.getValue() < o2.getValue()) {
					return 1;
				} else {
					return -1;
				}
			}
		});
		for(HashMap.Entry<String, Long> entry : list) {
			String name = epubNameList.get(entry.getKey());
			System.out.println(entry.getKey() + ".epub [" + name + "] -> " + String.format("%.2f", entry.getValue() / 1024.0f / 1024.0f) + "MB");
		}
	}
}

