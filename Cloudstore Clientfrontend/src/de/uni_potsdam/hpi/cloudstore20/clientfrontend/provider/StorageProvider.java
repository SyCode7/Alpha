package de.uni_potsdam.hpi.cloudstore20.clientfrontend.provider;

import java.io.File;
import java.io.IOException;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.helper.FileHelper;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.provider.helper.ProviderException;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.provider.helper.ProviderReturnValueBoolean;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.provider.helper.ProviderReturnValueString;

/**
 * Representing an abstract storage provider.
 */
public abstract class StorageProvider {

	protected String providerName;
	protected String standardLocation = "EU";

	protected String location = null;

	public Exception lastError = null;

	public boolean isValid() {

		return this.lastError == null;
	}

	protected String checkLocation(String location) {

		if (location.equals(""))
			return this.standardLocation;
		return location;
	}

	public HashFunction getHashFunction() {

		return Hashing.md5();
	}

	/**
	 * Uploads a file to the storage provider and verifies the integrity of the uploaded data using hashes.
	 * 
	 * @param remoteFolderName
	 *            Folder to put the data in
	 * @param filePath
	 *            Path of the file to upload
	 * @return
	 * @throws ProviderException
	 */
	public ProviderReturnValueString putFileReliable(String remoteFolderName, String filePath) throws ProviderException {

		ProviderReturnValueString remoteID = putFile(remoteFolderName, filePath);
		if (remoteID.getContent() != "") {
			File file = new File(filePath);
			String hash = "";
			try {
				hash = FileHelper.getHash(file, this.getHashFunction());
			} catch (IOException e) {
				e.printStackTrace();
			}
			String hashRemote = this.getHash(remoteFolderName, file.getName());
			if (hash != hashRemote) {
				deleteFile(remoteFolderName, file.getName());
				return null;
			}
			return remoteID;
		}
		return null;
	}

	/**
	 * Downloads a file from the storage provider and verifies the integrity of the downloaded data using hashes.
	 * 
	 * @param remoteFolderName
	 *            Folder to get the data from
	 * @param filePath
	 *            Destination Path of the file to download
	 * @return True on success and false on error
	 * @throws ProviderException
	 */
	public ProviderReturnValueBoolean popFileReliable(String remoteFolderName, String fileName, String destPath)
			throws ProviderException {

		ProviderReturnValueBoolean result = popFile(remoteFolderName, fileName, destPath);
		if (result.getStatus()) {
			File file = new File(destPath);
			String hash = "";
			try {
				hash = FileHelper.getHash(file, this.getHashFunction());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (hash != getHash(remoteFolderName, fileName)) {
				file.delete();
				result.setStatus(false);
				return result;
			}
			return result;
		}
		result.setStatus(false);
		return result;
	}

	/**
	 * Uploads a file to the storage provider
	 * 
	 * @param remoteFolderName
	 *            Folder to put the data in
	 * @param filePath
	 *            Path of the file to upload
	 * @return Filename
	 * @throws ProviderException
	 */
	abstract public ProviderReturnValueString putFile(String remoteFolderName, String filePath) throws ProviderException;

	/**
	 * Downloads a file from the storage provider
	 * 
	 * @param remoteFolderName
	 *            Folder to get the data from
	 * @param fileID
	 *            Filename on the Server
	 * @param destPath
	 *            Destination Path of the file to download
	 * @return True on success and false on error
	 * @throws ProviderException
	 */
	abstract public ProviderReturnValueBoolean popFile(String remoteFolderName, String fileID, String destPath)
			throws ProviderException;

	/**
	 * Deletes a file from the storage provider
	 * 
	 * @param user
	 * @param remoteFolderName
	 *            Remote folder to find the file
	 * @param fileID
	 *            Name of the uploaded file
	 * @return True on success and false on error
	 * @throws ProviderException
	 */
	abstract public boolean deleteFile(String remoteFolderName, String fileID) throws ProviderException;

	/**
	 * Retrieves the remote hash of a file
	 * 
	 * @param remoteFolderName
	 *            Remote folder to find the file
	 * @param fileID
	 *            Name of the uploaded file
	 * @return MD5 hash of the file
	 * @throws ProviderException
	 */
	abstract public String getHash(String remoteFolderName, String fileID) throws ProviderException;

	/**
	 * Removes all files from the storage provider
	 * 
	 * @return True on success and false on error
	 * @throws ProviderException
	 */
	abstract public boolean deleteAllFiles() throws ProviderException;

	/**
	 * Retrieves a list of all files stored in the storage provider
	 * 
	 * @param remoteFolderName
	 *            Remote folder to find the files
	 * @return A list of filenames
	 * @throws ProviderException
	 */
	abstract public String[] listFiles(String remoteFolderName) throws ProviderException;

	abstract public boolean fileExists(String remoteFolder, String fileID);

	public String getProviderName() {

		return this.providerName;
	}

	public void setProviderName(String provName) {

		this.providerName = provName;
	}

	public String getLocation() {

		if(this.location == null) {
			return "";
		}
		return this.location;
	}

	protected ProviderReturnValueString returnValue(String value, long start, long end) {

		ProviderReturnValueString returnValue = new ProviderReturnValueString();
		returnValue.setContent(value);

		return returnValue;

	}

	protected ProviderReturnValueBoolean returnValue(Boolean value, long start, long end) {

		ProviderReturnValueBoolean returnValue = new ProviderReturnValueBoolean();
		returnValue.setStatus(value);

		return returnValue;

	}

	public boolean isSimulateErrors() {

		return false;
	}

	public void setSimulateErrors(boolean value) {

	}
}
