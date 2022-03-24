package cn.eric.jdktools.file.jar;

import java.io.*;
import java.util.*;
import java.util.jar.*;

public class FatJarCreator {
	private final static String ASSETS_PREFIX = "assets/";

	private Set<String> entryName = new HashSet<>();

	/**
	 * @param jarsPath  JAR路径的数组
	 * @param output  新文件的合并路径
	 * @return result
	 */
	public boolean create(ArrayList<String> jarsPath, String assetPath, String output) {
		boolean result = true;
		//Manifest manifest = getManifest();

		FileOutputStream fos = null;
		JarOutputStream jos = null;
		try {
			fos = new FileOutputStream(output);
			//jos = new JarOutputStream(fos, manifest);
			addFilesFromJars(jarsPath, jos);
			if(null != assetPath){
				addAssets(new File(assetPath), ASSETS_PREFIX, jos);
			}else{
				System.out.println("!!!Path of assets is null!!!");
			}
			System.out.println("Create fat jar success");
		} catch (IOException e1) {
			result = false;
			e1.printStackTrace();
		} finally {
			try {
				if(null != jos){
					jos.close();
				}
				if(null != fos){
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}


	/**
	 * @param files  JAR文件的路径
	 * @param output  新文件的合并路径
	 * @return result
	 */
	public boolean createByFile(List<File> files, String assetPath, String output) {
		boolean result = true;
		Manifest manifest = getManifest();

		FileOutputStream fos = null;
		JarOutputStream jos = null;
		try {
			fos = new FileOutputStream(output);
			jos = new JarOutputStream(fos, manifest);
			addFilesFromJars(files, jos);
			if(null != assetPath){
				addAssets(new File(assetPath), ASSETS_PREFIX, jos);
			}else{
				System.out.println("!!!Path of assets is null!!!");
			}
			System.out.println("Create fat jar success");
		} catch (IOException e1) {
			result = false;
			e1.printStackTrace();
		} finally {
			try {
				if(null != jos){
					jos.close();
				}
				if(null != fos){
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	private Manifest getManifest() {
		Manifest manifest = new Manifest();
		Attributes attribute = manifest.getMainAttributes();
		attribute.putValue("Manifest-Version", "1.0");
		attribute.putValue("Created-By","Youku fat jar plugin");
		attribute.putValue("Main-Class","com.clickpaas.cid.component.project.common.generate.Params");
		return manifest;
	}

	private void addFilesFromJars(ArrayList<String> jarsPath, JarOutputStream out) throws IOException {
		String jarPath = "";
		for (int i = 0; i < jarsPath.size(); i++) {
			jarPath = jarsPath.get(i);
			if(!new File(jarPath).exists()){
				System.out.println("!!!Create fat jar failed!!!");
				System.out.println("Can't find "+jarPath);
				break;
			}
			System.out.println("JarName=" + jarPath);

			JarFile jarFile = new JarFile(jarPath);
			Enumeration<?> entities = jarFile.entries();

			while (entities.hasMoreElements()) {
				JarEntry entry = (JarEntry) entities.nextElement();

				if (entry.isDirectory()) {
					continue;
				}

				InputStream in = jarFile.getInputStream(entry);
				copyData2Jar(in, out, entry.getName());
			}

			jarFile.close();
		}
	}


	private void addFilesFromJars(List<File> jarsFiles, JarOutputStream out) throws IOException {
		File file;
		for (int i = 0; i < jarsFiles.size(); i++) {
			file = jarsFiles.get(i);
			if(!file.exists()){
				System.out.println("!!!Create fat jar failed!!!");
				System.out.println("Can't find " + file.getAbsolutePath());
				break;
			}
			System.out.println("JarName=" + file.getAbsolutePath());

			JarFile jarFile = new JarFile(file);
			Enumeration<?> entities = jarFile.entries();

			while (entities.hasMoreElements()) {
				JarEntry entry = (JarEntry) entities.nextElement();
				//System.out.println(entry.getName());
				if (entry.isDirectory() || entry.getName().toLowerCase().contains("manifest.mf")) {
					continue;
				}

				InputStream in = jarFile.getInputStream(entry);
				copyData2Jar(in, out, entry.getName());
			}

			jarFile.close();
		}
	}

	private void addAssets(File dir, String jarEntryPrefix, JarOutputStream out) throws IOException{
		if(!dir.exists() || !dir.isDirectory()){
			System.out.println("dir is not exist or is not a directory");
			return;
		}

		File[] files = dir.listFiles();
		for(File file : files){
			addAssetFile(file, jarEntryPrefix, out);
		}
	}

	private void addAssetFile(File file, String jarEntryPrefix, JarOutputStream out) throws IOException{
		System.out.println("asset file name is "+file.getName());

		if (file.exists() && file.isFile()) {
			InputStream in = new FileInputStream(file);
			copyData2Jar(in, out, jarEntryPrefix+file.getName());
		} else {
			addAssets(file, jarEntryPrefix+file.getName()+"/", out);
		}
	}

	private void copyData2Jar(InputStream in, JarOutputStream out, String newEntryName) throws IOException{
		if(entryName.add(newEntryName)) {
			int bufferSize;
			byte[] buffer = new byte[1024];

			out.putNextEntry(new JarEntry(newEntryName));

			while ((bufferSize = in.read(buffer, 0, buffer.length)) != -1) {
				out.write(buffer, 0, bufferSize);
			}

			in.close();
			out.closeEntry();
		}
	}
}
