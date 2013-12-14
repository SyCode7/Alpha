package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.implementations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.domain.Blob;
import org.jclouds.openstack.keystone.v2_0.config.CredentialTypes;
import org.jclouds.openstack.keystone.v2_0.config.KeystoneProperties;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProvider;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProviderConfig;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.storageProvider.StorageProviderException;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums.PROVIDER_ENUM;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums.STORAGE_PROVIDER_CONFIG;
import de.uni_potsdam.hpi.cloudstore20.meta.helper.FileHelper;

public abstract class JCloudsProvider extends StorageProvider {

	BlobStoreContext context = null;
	BlobStore store = null;

	public JCloudsProvider(String providerName, String location) throws StorageProviderException {

		super(providerName, location);
		initStore();
	}

	@Override
	public String uploadFile(File file) throws StorageProviderException {

		BlobStore store = this.getStore();
		if (!store.containerExists(this.getRemoteFolderName()))
			if (!store.createContainerInLocation(null, this.getRemoteFolderName()))
				throw new StorageProviderException(this.getCompleteProviderName() + " >> uploadFile: cannot create container!");

		FileInputStream fis = null;
		try {
			class Uploader implements Callable<Boolean> {
				BlobStore store = null;
				String remoteFolder = null;
				Blob blobFile = null;
				public Uploader(BlobStore store, String remoteFolder, Blob blobFile) {
					this.store = store;
					this.remoteFolder = remoteFolder;
					this.blobFile = blobFile;
				}
				@Override
				public Boolean call() throws Exception {
					store.putBlob(remoteFolder, blobFile);
					return true;
				}
			}

			fis = new FileInputStream(file.getAbsoluteFile());
			Blob blobFile = store.blobBuilder(file.getName()).payload(fis).build();

			this.waitAndUpdateStatus(file.length(), fis.getChannel(), new Uploader(store, this.getRemoteFolderName(), blobFile));
			
			return file.getName();
		} catch (FileNotFoundException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (IOException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (InterruptedException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (ExecutionException e) {
			throw new StorageProviderException(this.providerName, e);
		} finally {
			this.closeStream(fis);
		}
	}

	@Override
	public File downloadFile(String fileID) throws StorageProviderException {

		FileOutputStream fos = null;
		InputStream is = null;
		try {
			String remoteFolderName = this.getRemoteFolderName();
			String destPath = downloadFolder + File.separator + fileID;
			if (!store.blobExists(remoteFolderName, fileID)) {
				throw new StorageProviderException(this.getCompleteProviderName() + " >> downloadFile: blob does not exist!");
			}
			Blob fileBlob = this.getBlob(fileID);

			is = fileBlob.getPayload().getInput();
			fos = new FileOutputStream(destPath);

			class Downloader implements Callable<Boolean> {
				InputStream is = null;
				FileOutputStream fos = null;
				public Downloader(InputStream is, FileOutputStream fos) {
					this.is = is;
					this.fos = fos;
				}
				@Override
				public Boolean call() throws Exception {
					FileHelper.copyStream(is, fos);
					return true;
				}
			}
			
			long fileSize = fileBlob.getMetadata().getContentMetadata().getContentLength();
			this.waitAndUpdateStatus(fileSize, fos.getChannel(), new Downloader(is, fos));

			return new File(destPath);
		} catch (FileNotFoundException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (IOException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (InterruptedException e) {
			throw new StorageProviderException(this.providerName, e);
		} catch (ExecutionException e) {
			throw new StorageProviderException(this.providerName, e);
		} finally {
			this.closeStream(is);
			this.closeStream(fos);
		}
	}

	@Override
	public void deleteFile(String fileID) throws StorageProviderException {

		this.getStore().removeBlob(this.getRemoteFolderName(), fileID);
	}

	@Override
	public String getFileHash(String fileID) throws StorageProviderException {

		return store.getBlob(this.getRemoteFolderName(), fileID).getMetadata().getETag();
	}

	@Override
	protected String getRemoteFolderName() {

		return this.remoteFolderName;
	}

	// private methods

	private Blob getBlob(String fileID) {

		return this.getStore().getBlob(this.getRemoteFolderName(), fileID);
	}

	private BlobStore getStore() {

		return this.store;
	}

	private void initStore() {

		String user = StorageProviderConfig.getInstance().get(
				PROVIDER_ENUM.fromString(this.getCompleteProviderName()).getConfigCategory(), STORAGE_PROVIDER_CONFIG.Username);
		String pwd = StorageProviderConfig.getInstance().get(
				PROVIDER_ENUM.fromString(this.getCompleteProviderName()).getConfigCategory(), STORAGE_PROVIDER_CONFIG.Password);

		Properties props = new Properties();
		props.setProperty(KeystoneProperties.CREDENTIAL_TYPE, CredentialTypes.API_ACCESS_KEY_CREDENTIALS);
		context = ContextBuilder.newBuilder(builderString()).overrides(props).credentials(user, pwd)
				.buildView(BlobStoreContext.class);
		store = context.getBlobStore();
	}

	abstract protected String builderString();

}
