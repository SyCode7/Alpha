package de.uni_potsdam.hpi.cloudstore20.clientfrontend.helper;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileOutputStreamProxy extends FileOutputStream {
	  
	private long count = 0;  

	public FileOutputStreamProxy(String name) throws FileNotFoundException {
		super(name);
	}

	public FileOutputStreamProxy(File file) throws FileNotFoundException {
		super(file);
	}

	public FileOutputStreamProxy(FileDescriptor fdObj) {
		super(fdObj);
	}

	public FileOutputStreamProxy(String name, boolean append)
			throws FileNotFoundException {
		super(name, append);
	}

	public FileOutputStreamProxy(File file, boolean append)
			throws FileNotFoundException {
		super(file, append);
	}
	
	public long getCount(){
		return this.count;
	}
	
	
	@Override
	public void write(byte[] b) throws IOException {
		super.write(b);
		
	}
	
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		super.write(b, off, len);
		count+=len;
	
	}
	
	@Override
	public void write(int b) throws IOException {
		super.write(b);
	}

}
