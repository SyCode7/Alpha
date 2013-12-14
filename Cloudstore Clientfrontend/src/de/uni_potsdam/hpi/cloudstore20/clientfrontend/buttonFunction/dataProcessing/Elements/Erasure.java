package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.Elements;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.DataProcessElement;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.DataProcessTask;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.DataProcessingException;
import de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.dataProcessing.ProviderFileContainer;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.UsedCloudstoreConfig;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums.DATA_PROCESS_METHOD;
import de.uni_potsdam.hpi.cloudstore20.meta.helper.FileHelper;

public class Erasure extends DataProcessElement {

	private int status = 0;

	public Erasure(UsedCloudstoreConfig config) {

		super(config);
		// TODO Auto-generated constructor stub
	}

	@Override
	public DataProcessTask doProcessing(DataProcessTask task) throws DataProcessingException {

		// nur STUB!
		File f = task.getOriginalFile();
		int i = 0;
		for (ProviderFileContainer pfc : task.getProviderFileListFor(DATA_PROCESS_METHOD.erasure)) {
			File f_ = new File(System.getProperty("java.io.tmpdir") + "tempFile" + i);
			File f__ = null;
			try {
				f__ = File.createTempFile("tempFile" + i, ".temp");
			} catch (IOException e) {}
			f__.deleteOnExit();
			FileHelper.generateRandomContentFile(f_.getAbsolutePath(), f.length() / 6);
			pfc.addFile(f_);
			i++;
		}

		for (i = 0; i < 100; i++) {
			try {
				Thread.sleep(25l);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.status = i;
		}

		// stub ende!

		// TODO Auto-generated method stub
		return task;
	}

	// Bitte verwenden! Wird u.a. von Uploadoptimierung gebraucht
	public List<File> doErasure(File file, int k, int m) {

		List<File> returnValue = new LinkedList<File>();

		// TODO: Erasure implementieren

		return returnValue;

	}

	@Override
	public int getStatus() {

		return this.status;
	}

}
