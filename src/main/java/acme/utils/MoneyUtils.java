/*
 * MoneyUtils.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.utils;

import java.util.Collection;

import acme.client.data.datatypes.Money;

public class MoneyUtils {

	public static Money getAvg(final Collection<Money> collection, final String systemCurrency) {

		Money result;

		result = new Money();
		result.setCurrency(systemCurrency);
		result.setAmount(collection.stream().mapToDouble(Money::getAmount).average().getAsDouble());

		return result;
	}

	public static Money getStd(final Collection<Money> collection, final String systemCurrency) {

		Money result;

		result = new Money();
		result.setCurrency(systemCurrency);

		Integer n = collection.size();
		Double media = collection.stream().mapToDouble(Money::getAmount).average().getAsDouble();

		Double varianza = collection.stream().mapToDouble(Money::getAmount).map(x -> Math.pow(x - media, 2) / n).sum();

		result.setAmount(Math.sqrt(varianza));

		return result;
	}

	public static Money getMin(final Collection<Money> collection, final String systemCurrency) {

		Money result;

		result = new Money();
		result.setCurrency(systemCurrency);
		result.setAmount(collection.stream().mapToDouble(Money::getAmount).min().orElse(0.));

		return result;
	}

	public static Money getMax(final Collection<Money> collection, final String systemCurrency) {

		Money result;

		result = new Money();
		result.setCurrency(systemCurrency);
		result.setAmount(collection.stream().mapToDouble(Money::getAmount).max().orElse(0.));

		return result;
	}

}
