package de.uni_potsdam.hpi.cloudstore20.meta.helper;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

public class FileHelper {

	public static boolean generateRandomContentFile(String filePath, long fileSize) {

		RandomAccessFile f = null;
		File file = new File(filePath);

		int cacheSize = 32 * 1024; // 32Kb
		long bytesWritten = 0L;

		try {
			f = new RandomAccessFile(file, "rw");
			f.setLength(fileSize);
			while (bytesWritten < fileSize) {
				int bytesToWrite = (int) Math.min(cacheSize, fileSize - bytesWritten);
				byte[] cache = new byte[bytesToWrite];
				new Random().nextBytes(cache);
				f.write(cache);
				bytesWritten += bytesToWrite;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				f.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

}
