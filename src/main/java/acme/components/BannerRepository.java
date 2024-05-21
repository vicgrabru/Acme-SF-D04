/*
 * AdvertisementRepository.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.components;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.helpers.RandomHelper;
import acme.client.repositories.AbstractRepository;
import acme.entities.banner.Banner;

@Repository
public interface BannerRepository extends AbstractRepository {

	@Query("select count(b) from Banner b where b.periodStart <= current_timestamp() and b.periodEnd >= current_timestamp()")
	int countDisplayableBanners();

	@Query("select b from Banner b where b.periodStart <= current_timestamp() and b.periodEnd >= current_timestamp()")
	Banner[] findAllDisplayableBanners();

	default Banner getRandomBanner() {
		Banner result;
		int count, index;
		Banner[] banners;

		count = this.countDisplayableBanners();
		if (count == 0)
			result = null;
		else {
			index = RandomHelper.nextInt(0, count);
			banners = this.findAllDisplayableBanners();
			result = banners.length == 0 ? null : banners[index];
		}

		return result;
	}

}
