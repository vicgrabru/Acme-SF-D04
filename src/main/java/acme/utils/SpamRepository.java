/*
 * MoneyExchangeRepository.java
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

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import spamDetector.SpamDetector;

@Repository
public interface SpamRepository extends AbstractRepository {

	@Query("select sc.spamTerms from SystemConfiguration sc")
	String findSpamTerms();

	@Query("select sc.spamThreshold from SystemConfiguration sc")
	double findSpamThreshold();

	default boolean checkTextValue(final String textToCheck) {
		boolean result = false;
		List<String> spamTerms;
		double spamThreshold;

		spamTerms = new ArrayList<>(List.of(this.findSpamTerms().split(", ")));

		spamThreshold = this.findSpamThreshold();

		result = SpamDetector.checkTextValue(textToCheck, spamTerms, spamThreshold);

		return result;
	}

}
