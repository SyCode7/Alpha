package de.uni_potsdam.hpi.cloudstore20.clientfrontend.buttonFunction.configuration;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;

import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.CloudstoreConfigException;
import de.uni_potsdam.hpi.cloudstore20.meta.dataTransmitting.config.PROVIDER_ENUM;

public class HasseDiagramm {

	private Set<PROVIDER_ENUM> possibleProviders;
	private long fileSize;

	private Set<CloudraidNode> allNodes = new HashSet<CloudraidNode>();

	public HasseDiagramm(Set<PROVIDER_ENUM> providers, long fileSize) {

		this.possibleProviders = providers;
		this.fileSize = fileSize;
		this.initialize();

	}

	private void initialize() {

		// TODO: hier müsste halt ein logischer baum initialisiert werden

		int n = this.possibleProviders.size();

		for (int k = 1; k <= n; k++) {

			for (int m = 0; m + k <= n; m++) {

				// Alle Provider Kobinationen mit k+m Providern erzeugen
				ICombinatoricsVector<PROVIDER_ENUM> initialVector = Factory.createVector(this.possibleProviders);
				Generator<PROVIDER_ENUM> gen = Factory.createSimpleCombinationGenerator(initialVector, k + m);
				List<ICombinatoricsVector<PROVIDER_ENUM>> combiations = gen.generateAllObjects();
				for (ICombinatoricsVector<PROVIDER_ENUM> combination : combiations) {

					Set<PROVIDER_ENUM> combinationSet = this.translateToSet(combination.getVector());

					// Alle Downloadkombinationen erzeugen für dieses Subset
					ICombinatoricsVector<PROVIDER_ENUM> initialVector_ = Factory.createVector(combination);
					Generator<PROVIDER_ENUM> gen_ = Factory.createSimpleCombinationGenerator(initialVector_, k);
					List<ICombinatoricsVector<PROVIDER_ENUM>> combiations_ = gen_.generateAllObjects();

					for (ICombinatoricsVector<PROVIDER_ENUM> combination_ : combiations_) {
						this.allNodes.add(new CloudraidNode(k, m, combinationSet, this.translateToSet(combination_.getVector()),
								this.fileSize));
					}
				}

			}

		}

	}

	public Set<CloudraidNode> filterNodes(double maxCosts, int numberOfNines) throws CloudstoreConfigException {

		Set<CloudraidNode> result = new HashSet<CloudraidNode>();

		BigInteger minAvailability = this.getBigInt4NumberOfNines(numberOfNines);
		for (CloudraidNode crn : this.allNodes) {

			try {

				// a.compareTo(b); // returns (-1 if a < b), (0 if a == b), (1 if a > b)
				if (minAvailability.compareTo(crn.getAvailability()) == 1) {
					continue;
				}
				if (crn.getCostsInComparisonToBestSingleUpload() > maxCosts) {
					continue;
				}

				result.add(crn);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;

	}

	public Set<CloudraidNode> filterNodesCosts(double maxCosts) throws CloudstoreConfigException {

		return this.filterNodes(maxCosts, 0);

	}

	public Set<CloudraidNode> filterNodesAvailability(int numberOfNines) throws CloudstoreConfigException {

		return this.filterNodes(Double.MAX_VALUE, numberOfNines);

	}

	public Set<CloudraidNode> getUnfiltered() {

		return this.allNodes;
	}

	private Set<PROVIDER_ENUM> translateToSet(List<PROVIDER_ENUM> l) {

		Set<PROVIDER_ENUM> result = new HashSet<PROVIDER_ENUM>();

		for (PROVIDER_ENUM p : l) {

			result.add(p);

		}

		return result;

	}

	private BigInteger getBigInt4NumberOfNines(int numberOfNines) throws CloudstoreConfigException {

		if (numberOfNines >= AvailiablityPropNode.powOfTen) {
			throw new CloudstoreConfigException("Zu viele Neuner für Rechensystem verlangt. Grenze liegt bei: "
					+ (AvailiablityPropNode.powOfTen - 1));
		}

		String bigInt = "";
		for (int i = 0; i < numberOfNines; i++) {
			bigInt += "9";
		}
		for (int i = numberOfNines; i < AvailiablityPropNode.powOfTen - 1; i++) {
			bigInt += "0";
		}

		return new BigInteger(bigInt);
	}

}