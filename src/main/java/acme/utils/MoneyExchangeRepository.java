/*
 * EmployerApplicationRepository.java
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import acme.client.data.datatypes.Money;
import acme.client.helpers.MomentHelper;
import acme.client.repositories.AbstractRepository;
import acme.entities.configuration.ExchangeRate;

@Repository
public interface MoneyExchangeRepository extends AbstractRepository {

	@Query("select sc.systemCurrency from SystemConfiguration sc")
	String findSystemCurrency();

	@Query("select er from ExchangeRate er where er.source = :source and er.target = :target")
	ExchangeRate findExchangeRate(String source, String target);

	default Money exchangeMoney(final Money money) {
		Money result;
		String sc = this.findSystemCurrency();
		String currency = money.getCurrency();
		if (sc.equals(currency))
			result = money;
		else {
			ExchangeRate er = this.findExchangeRate(currency, sc);
			LocalDate today = LocalDate.now();
			Double rate;
			if (er == null) {
				rate = this.getExchangeRate(currency, sc);
				ExchangeRate ner = new ExchangeRate();
				ner.setSource(currency);
				ner.setTarget(sc);
				ner.setRate(rate);
				ner.setInstantiationMoment(today);
				if (rate.equals(-1.0))
					rate = 1.0;
				else
					this.save(ner);

			} else if (!today.equals(er.getInstantiationMoment())) {
				rate = this.getExchangeRate(currency, sc);
				er.setInstantiationMoment(today);
				er.setRate(rate);
				if (rate.equals(-1.0))
					rate = 1.0;
				else
					this.save(er);

			} else
				rate = er.getRate();

			result = new Money();
			double amount = BigDecimal.valueOf(rate * money.getAmount()).setScale(2, RoundingMode.FLOOR).doubleValue();
			result.setAmount(amount);
			result.setCurrency(sc);
		}
		return result;
	}

	private double getExchangeRate(final String source, final String target) {
		Double result;
		try {
			RestTemplate api = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.set("apikey", "gosbR4UgNOwcVY3AnfgUhYCnrz95oxFf");
			HttpEntity<?> entity = new HttpEntity<>(headers);
			String uri = "https://api.apilayer.com/exchangerates_data/convert?to=" + target + "&from=" + source + "&amount=1";
			result = api.exchange(uri, HttpMethod.GET, entity, Rate.class).getBody().getResult();

			MomentHelper.sleep(1000);
		} catch (final Throwable oops) {
			result = -1.0;
		}
		return result;
	}

}
