package cftExtractorRecode.exporters;

import java.io.File;

public abstract class FileExporter extends Exporter{
	
	/**
	 * Path of resulting file
	 */
	protected String path;
	
	/**
	 * File object of path
	 */
	protected File file;
	
	/**
	 * @param path - sets a new path
	 */
	public void setPath(String path) {
		this.path = path;
		this.file = new File(path);
	}
	
	/**
	 * @param file - sets a new path using File object
	 */
	public void setFile(File file) {
		this.file = file;
		this.path = file.getAbsolutePath();
	}
	/**
	 * @return - current path used
	 */
	public String getPath() {
		return this.path;
	}
	
	/**
	 * @return - current path used as File object
	 */
	public File getFile() {
		if(file == null) {
			this.file = new File(this.path);
		}
		
		return this.file;
	}
}
