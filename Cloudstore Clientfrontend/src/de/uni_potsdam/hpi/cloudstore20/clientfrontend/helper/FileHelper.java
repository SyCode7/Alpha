package de.uni_potsdam.hpi.cloudstore20.clientfrontend.helper;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.Settings;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.helper.Exceptions.CloudStoreFileException;

/**
 * A helper class which should not be instantiated. It contains static helper
 * method which are useful when dealing with files or file streams.
 */
public class FileHelper {

	public static long BYTE = 1;
	public static long KB = 1024 * BYTE;
	public static long MB = 1024 * KB;
	public static long GB = 1024 * MB;

	public static boolean generateRandomContentFiles(Map<String, Long> files,
			String folderPath) {

		boolean result = true;
		for (String fileName : files.keySet()) {
			String filePath = folderPath + File.separator + fileName;
			if (new File(filePath).exists())
				continue;
			if (!FileHelper.generateRandomContentFile(filePath,
					files.get(fileName))) {
				result = false;
			}
		}

		return result;
	}

	public static boolean generateRandomContentFile(String filePath,
			long fileSize) {

		RandomAccessFile f = null;
		File file = new File(filePath);

		int cacheSize = 32 * 1024; // 32Kb
		long bytesWritten = 0L;

		try {
			f = new RandomAccessFile(file, "rw");
			f.setLength(fileSize);
			while (bytesWritten < fileSize) {
				int bytesToWrite = (int) Math.min(cacheSize, fileSize
						- bytesWritten);
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

	/**
	 * Copies all bytes from the input stream to the output stream
	 * 
	 * @param is
	 *            the source of the copy operation
	 * @param os
	 *            the destination of the copy operation
	 * @throws IOException
	 */
	public static void copyStream(InputStream is, OutputStream os)
			throws IOException {

		byte[] ioBuf = new byte[16 * 1024];
		int bytesRead;
		while ((bytesRead = is.read(ioBuf)) != -1) {
			os.write(ioBuf, 0, bytesRead);
		}
		os.flush();
		os.close();
	}

	/**
	 * Creates the whole passed directory path
	 * 
	 * @param dir
	 *            Directory to create
	 * @return true on success - false on failure
	 */
	public static void createDir(String dir) {

		new File(dir).mkdirs();
	}

	/**
	 * Get the MD5 hash of a file. Internally the com.google.common.io.Files
	 * library is used.
	 * 
	 * @param file
	 *            the file which should be used to generate the MD5 digest
	 * @return MD5 digest in hex notation
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public static String getHashMD5(java.io.File file) throws IOException,
			NoSuchAlgorithmException {

		HashCode hashCode = Files.hash(file, Hashing.md5());
		BigInteger bigInt = new BigInteger(1, hashCode.asBytes());
		String hashString = bigInt.toString(16);
		// an die Länge von 32bit anpassen
		while (hashString.length() < 32) {
			hashString = "0" + hashString;
		}

		return hashString;
	}

	/**
	 * Get the MD5 hash of a file. Internally the com.google.common.io.Files
	 * library is used.
	 * 
	 * @param file
	 *            the file which should be used to generate the MD5 digest
	 * @param md
	 * @return MD5 or SHA1 digest in hex notation
	 * @throws IOException
	 */
	public static String getHash(java.io.File file, HashFunction hashFunc)
			throws IOException {

		byte[] hash;
		try {
			hash = Files.hash(file, hashFunc).asBytes();
		} catch (IOException e) {
			if (Settings.sysOuts)
				System.err.println("Getting hash caused IOException: "
						+ e.getMessage());

			return "";
		}
		int hashLength = hashFunc.bits() / 4;
		BigInteger bigInt = new BigInteger(1, hash);
		String hashString = bigInt.toString(16);
		while (hashString.length() < hashLength) {
			// führende Nullen vorne hinzufügen
			hashString = "0" + hashString;
		}
		return hashString;
	}

	/**
	 * Method which is able to encrypt a file with AES. The return value
	 * contains the necessary information to decrypt the file so don't lose it!
	 * 
	 * @param fileName
	 *            source file name
	 * @param destinationName
	 *            destination file name, the encrypted file is stored there
	 * @return Object: secret - secret used fo encryption, iv - initial vector
	 *         used for encryption
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidParameterSpecException
	 */
	public static HashMap<String, byte[]> encrypt(String fileName,
			String destinationName) throws IOException,
			NoSuchAlgorithmException, InvalidKeySpecException,
			NoSuchPaddingException, InvalidKeyException,
			InvalidParameterSpecException {

		FileInputStream fis = new FileInputStream(new File(fileName));
		File out = new File(destinationName);
		// create all directories to make sure path exists
		FileHelper.createDir(out.getParent());

		out.createNewFile();

		BufferedOutputStream outStream = new BufferedOutputStream(
				new FileOutputStream(out));

		SecretKeyFactory factory = SecretKeyFactory
				.getInstance("PBKDF2WithHmacSHA1");
		KeySpec spec = new PBEKeySpec("password".toCharArray(),
				"salt".getBytes(), 1024, 256);
		SecretKey tmp = factory.generateSecret(spec);
		SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secret);
		AlgorithmParameters params = cipher.getParameters();

		CipherOutputStream cos = new CipherOutputStream(outStream, cipher);

		copyStream(fis, cos);
		cos.close();
		fis.close();
		byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
		HashMap<String, byte[]> result = new HashMap<String, byte[]>();
		result.put("secret", secret.getEncoded());
		result.put("iv", iv);
		return result;
	}

	/**
	 * Method which is able to decrypt a file which was previously encrypted
	 * with the FileHelper.encrypt method. To successfully work, the method
	 * needs the parameters which are returned from the encrypt method.
	 * 
	 * @param fileName
	 *            the encrypted files name
	 * @param destinationName
	 *            destination file name, the decrypted file is stored there
	 * @param iv
	 *            initial vector which was used to encrypt the file
	 * @param secretEncoded
	 *            the encoded secret which was used to encrypt the file
	 * @throws IOException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidAlgorithmParameterException
	 * @throws InvalidKeyException
	 */
	public static void decrypt(String fileName, String destinationName,
			byte[] iv, byte[] secretEncoded) throws IOException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException {

		FileInputStream fis = new FileInputStream(new File(fileName));
		File out = new File(destinationName);
		// create all directories to make sure path exists
		FileHelper.createDir(out.getParent());
		out.createNewFile();
		BufferedOutputStream outStream = new BufferedOutputStream(
				new FileOutputStream(out));

		SecretKey secret = new SecretKeySpec(secretEncoded, "AES");

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));

		CipherOutputStream cos = new CipherOutputStream(outStream, cipher);
		copyStream(fis, cos);
		cos.close();
		fis.close();
	}

	/**
	 * Method to delete the whole content of an given folder.
	 * 
	 * @param folder
	 *            the folder the whole content should be deleted
	 */
	public static void deleteFolderContent(File folder) {

		deleteFolderContent(folder, true);
	}

	public static void deleteFolderContent(File folder, boolean recursive) {

		File[] content = folder.listFiles();
		if (content == null)
			return;
		for (File f : content) {
			if (!f.isDirectory()) {
				f.delete();
			} else {
				if (recursive) {
					FileHelper.deleteFolderContent(f, true);
					f.delete();
				}
			}
		}
	}

	public static String extractFilenameWithoutExtension(String fileName) {

		int index = fileName.lastIndexOf('.');
		if (index > 0 && index <= fileName.length() - 2) {
			return fileName.substring(0, index);
		} else {
			return fileName;
		}
	}

	public static void copyFile(File in, File out) throws IOException {

		FileInputStream fis = new FileInputStream(in);
		FileOutputStream fos = new FileOutputStream(out);

		FileChannel inChannel = fis.getChannel();
		FileChannel outChannel = fos.getChannel();
		inChannel.transferTo(0, inChannel.size(), outChannel);

		inChannel.close();
		outChannel.close();

		fis.close();
		fos.close();

	}

	public static Map<String, File> split(File origin, File targetFolder,
			int size) throws CloudStoreFileException {

		FileInputStream fis;
		Map<String, File> returnValue = new HashMap<String, File>();
		try {
			fis = new FileInputStream(origin);

			int bufferSize = 4096 * 1024;
			int temp = size;
			int i = 0;
			byte[] buffer = new byte[bufferSize];
			String filename = targetFolder.getAbsolutePath() + File.separator
					+ origin.getName() + ".remaining";
			FileOutputStream fos = new FileOutputStream(filename);
			while (temp > bufferSize) {

				i = fis.read(buffer);
				if (!(i == -1)) {
					fos.write(buffer, 0, i);
					temp -= bufferSize;
				}
			}

			buffer = new byte[temp];
			i = fis.read(buffer);
			if (!(i == -1)) {
				fos.write(buffer, 0, i);
			}
			fos.flush();
			fos.close();
			returnValue.put("remaining", new File(filename));

			byte[] ioBuf = new byte[bufferSize];

			filename = targetFolder.getAbsolutePath() + File.separator
					+ origin.getName() + ".cutted";
			fos = new FileOutputStream(filename);

			while ((i = fis.read(ioBuf)) != -1)
				fos.write(ioBuf, 0, i);

			fos.flush();
			fos.close();

			returnValue.put("cutted", new File(filename));

			fis.close();

		} catch (IOException e) {
			throw new CloudStoreFileException(e.getMessage());
		}

		return returnValue;

	}

}
