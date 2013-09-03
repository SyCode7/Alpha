package de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config;

import java.util.LinkedList;
import java.util.List;

import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.DataTransmittingClass;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.DataTransmittingException;

public class CloudstoreConfig extends DataTransmittingClass {

	private enum CloudstoreConfigElements {
		dataProcessConfig, erasureConfig;
	}

	private List<DATA_PROCESS_METHOD> methods = new LinkedList<DATA_PROCESS_METHOD>();
	private int k;
	private int m;

	public CloudstoreConfig(String content) throws DataTransmittingException {

		super(content);
		this.parseContent(content);
	}

	private CloudstoreConfig(List<DATA_PROCESS_METHOD> methods, int k, int m) throws DataTransmittingException {

		super("");

		this.m = m;
		this.k = k;
		this.methods = methods;
	}

	private void parseContent(String content) {

		String[] elements = content.split("###");
		for (String element : elements) {

			String temp = element.split("+++")[1].split("---")[0];
			if (element.startsWith(CloudstoreConfigElements.dataProcessConfig.toString())) {

				for (String methodString : temp.split("#")) {
					for (DATA_PROCESS_METHOD method : DATA_PROCESS_METHOD.values()) {
						if (method.toString().equals(methodString)) {
							this.methods.add(method);
							break;
						}
					}

				}

			} else if (element.startsWith(CloudstoreConfigElements.erasureConfig.toString())) {
				this.k = Integer.valueOf(temp.split(":")[0]);
				this.m = Integer.valueOf(temp.split(":")[1]);
			}
		}

	}

	@Override
	public String getClassAsString() throws DataTransmittingException {

		StringBuilder content = new StringBuilder();

		content.append(CloudstoreConfigElements.dataProcessConfig).append("+++");
		boolean first = true;
		for (DATA_PROCESS_METHOD dpm : this.methods) {
			if (first) {
				first = false;
				content.append(dpm);
				continue;
			}
			content.append("#").append(dpm);
		}
		content.append("---");

		content.append("###").append(CloudstoreConfigElements.erasureConfig).append("+++").append(this.k).append(":")
				.append(this.m).append("---");

		return content.toString();
	}

	public List<DATA_PROCESS_METHOD> getMethods() {

		List<DATA_PROCESS_METHOD> temp = new LinkedList<DATA_PROCESS_METHOD>();

		for (DATA_PROCESS_METHOD dpm : this.methods) {
			temp.add(dpm);
		}

		return temp;
	}

	public int getK() {

		return this.k;
	}

	public int getM() {

		return this.m;
	}

	public static CloudstoreConfig loadDefault() {

		List<DATA_PROCESS_METHOD> methods = new LinkedList<DATA_PROCESS_METHOD>();
		methods.add(DATA_PROCESS_METHOD.erasure);
		methods.add(DATA_PROCESS_METHOD.uploadOptimization);

		CloudstoreConfig cc = null;
		try {
			cc = new CloudstoreConfig(methods, 4, 2);
		} catch (DataTransmittingException e) {}

		return cc;

	}

}
