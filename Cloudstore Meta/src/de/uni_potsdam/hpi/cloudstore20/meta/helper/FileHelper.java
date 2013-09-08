package de.uni_potsdam.hpi.cloudstore20.meta.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

	public static String getHashMD5(File file) throws HelperException {

		byte[] b;
		try {
			b = FileHelper.createChecksum(file);
		} catch (NoSuchAlgorithmException | IOException e) {
			throw new HelperException(e.getMessage(), e.getCause());
		}
		String result = "";

		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}

	private static byte[] createChecksum(File file) throws NoSuchAlgorithmException, IOException {

		InputStream fis = new FileInputStream(file);

		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;

		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);

		fis.close();
		return complete.digest();
	}

}
