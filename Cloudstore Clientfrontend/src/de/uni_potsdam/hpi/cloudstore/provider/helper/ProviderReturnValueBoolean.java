package de.uni_potsdam.hpi.cloudstore.provider.helper;

public class ProviderReturnValueBoolean extends ProviderReturnValue {

	private boolean status = false;

	public void setStatus(boolean status) {

		this.status = status;
	}

	public boolean getStatus() {

		return this.status;
	}
}
