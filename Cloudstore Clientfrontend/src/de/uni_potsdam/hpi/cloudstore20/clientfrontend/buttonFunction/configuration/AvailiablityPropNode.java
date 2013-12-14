package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.configuration;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.enums.PROVIDER_ENUM;

public class AvailiablityPropNode {

	public static int powOfTen = 24;

	private BigInteger prob;
	private List<String> usedProv = new LinkedList<String>();
	private int succeeded = 0;

	public AvailiablityPropNode() {

		this.prob = new BigInteger("1").multiply(new BigInteger("10").pow(powOfTen));

	}

	private AvailiablityPropNode(BigInteger prob, List<String> usedProv, int success) {

		for (String prov : usedProv) {
			this.usedProv.add(prov);
		}
		this.prob = new BigInteger(prob.toString());
		this.succeeded = success;
	}

	public AvailiablityPropNode clone() {

		return new AvailiablityPropNode(this.prob, this.usedProv, this.succeeded);
	}

	public BigInteger getProb() {

		return this.prob;
	}

	public int getSuccessfull() {

		return this.succeeded;
	}

	public void addNewProvider(PROVIDER_ENUM provider, boolean successfull) {

		int firstExpo = 4;
		int value = (int) (InMemDatabase.getInstance().getAvailability(provider) * Math.pow(10, firstExpo));
		if (!successfull) {
			value = ((int) Math.pow(10, firstExpo)) - value;
		} else {
			this.succeeded += 1;
		}

		BigInteger val = new BigInteger(String.valueOf(value)).multiply(new BigInteger("10").pow(powOfTen - firstExpo));
		this.prob = this.prob.multiply(val).divide(new BigInteger("10").pow(powOfTen));
		this.usedProv.add(provider.toString() + "  " + successfull);

	}

}
