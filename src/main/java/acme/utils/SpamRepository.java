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

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.configuration.ExchangeRate;

@Repository
public interface SpamRepository extends AbstractRepository {

	@Query("select sc.spamTerms from SystemConfiguration sc")
	String findSpamTerms();

	@Query("select sc.spamThreshold from SystemConfiguration sc")
	double findSpamThreshold();

	@Query("select er from ExchangeRate er where er.source = :source and er.target = :target")
	ExchangeRate findExchangeRate(String source, String target);

	default boolean isOverSpamThreshold(final String textToCheck) {
		boolean result = false;
		return result;
	}

	//	private double getExchangeRate(final String source, final String target) {
	//		Double result;
	//		try {
	//			RestTemplate api = new RestTemplate();
	//			HttpHeaders headers = new HttpHeaders();
	//			headers.set("apikey", "gmLPUQdRBtFyAg2zzuw4lQxj4PmEE4kO");
	//			HttpEntity<?> entity = new HttpEntity<>(headers);
	//			String uri = "https://api.apilayer.com/exchangerates_data/convert?to=" + target + "&from=" + source + "&amount=1";
	//			result = api.exchange(uri, HttpMethod.GET, entity, Rate.class).getBody().getResult();
	//
	//			MomentHelper.sleep(1000);
	//		} catch (final Throwable oops) {
	//			result = -1.0;
	//		}
	//		return result;
	//	}

}
