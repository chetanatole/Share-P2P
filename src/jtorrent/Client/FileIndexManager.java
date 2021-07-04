package jtorrent.Client;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

import org.javatuples.Pair;

public class FileIndexManager {

	String rootDirectoryPath = null;
	ArrayList<String> addedMerkleRoots, removedMerkleRoots, addedFileNames, currentList;
	File indexFile;
	File rootDirectory;
	ObjectOutputStream fileWriter;

	public FileIndexManager(String username) {
		this.rootDirectoryPath = System.getProperty("user.home") + "/.P2P/" + username;

		rootDirectory = new File(rootDirectoryPath);
		if (!(rootDirectory.exists() && rootDirectory.isDirectory())) {
			rootDirectory.mkdirs();
		}
		try {
			ArrayList<String> l = new ArrayList<String>();
			this.indexFile = new File(Paths.get(this.rootDirectoryPath, "indexFile.ser").toString());
			if (!this.indexFile.exists()) {
				this.indexFile.createNewFile();
				fileWriter = new ObjectOutputStream(new FileOutputStream(this.indexFile, false));
				fileWriter.writeObject(l);
				fileWriter.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void CheckForChanges() {
		// TODO: test metafile copy
		try {
			String files[] = rootDirectory.list();
			this.currentList = new ArrayList<String>(Arrays.asList(files));
			this.currentList.remove("indexFile.ser");
			ObjectInputStream fileReader = new ObjectInputStream(new FileInputStream(this.indexFile));
			@SuppressWarnings({ "unchecked" })
			List<String> prevList = (List<String>) fileReader.readObject();
			this.addedMerkleRoots = new ArrayList<String>(this.currentList);
			this.addedMerkleRoots.removeAll(prevList);

			this.removedMerkleRoots = new ArrayList<String>(prevList);
			this.removedMerkleRoots.removeAll(this.currentList);

			ObjectOutputStream fileWriter = new ObjectOutputStream(new FileOutputStream(this.indexFile, false));
			fileWriter.writeObject(this.currentList);

			fileReader.close();
			fileWriter.close();
			getNewFileNames();

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Pair<String, Integer>> getDashboardData() {
		ArrayList<Pair<String, Integer>> dashBoardData = new ArrayList<>();
		for (String merkleRoot : this.currentList) {
			File merkleRootFolder = Paths.get(this.rootDirectoryPath, merkleRoot).toFile();
			Integer fileSizeMB = merkleRootFolder.list().length - 1;
			String fileName = null;
			for (String file : merkleRootFolder.list()) {
				if (file.contains(".metadata")) {
					fileName = file.substring(0, file.length() - 9);
				}
			}
		//	System.out.println("pair 1" + fileName);
			//System.out.println("pair 2" + fileSizeMB);
			dashBoardData.add(Pair.with(fileName, Integer.valueOf(fileSizeMB)));
		}
		return dashBoardData;
	}

	public void getNewFileNames() {
		this.addedFileNames = new ArrayList<String>();
		for (String merkleRoot : this.addedMerkleRoots) {
			File directory = Paths.get(this.rootDirectoryPath, merkleRoot).toFile();
			String[] filelist = directory.list();
			for (String file : filelist) {
				if (file.contains(".metadata")) {
					this.addedFileNames.add(file.substring(0, file.length() - 9));
				}
			}
		}
	}

	// TODO: refactor to use pairs
	public ArrayList<String> getAddedMerkleRoots() {
		return this.addedMerkleRoots;
	}

	public ArrayList<String> getRemovedMerkleRoots() {
		return this.removedMerkleRoots;
	}

	public ArrayList<String> getAddedFileNames() {
		return this.addedFileNames;
	}
}
