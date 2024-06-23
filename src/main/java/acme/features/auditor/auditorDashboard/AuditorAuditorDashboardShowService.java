/*
 * AuditorAuditorDashboardShowService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.auditor.auditorDashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.forms.AuditorDashboard;
import acme.roles.Auditor;

@Service
public class AuditorAuditorDashboardShowService extends AbstractService<Auditor, AuditorDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorAuditorDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		int auditorId;
		AuditorDashboard dashboard;

		Integer totalNumberOfStaticCodeAudits;
		Integer totalNumberOfDynamicCodeAudits;

		Double avgAuditRecordsPerCodeAudit;
		Integer minAuditRecordsPerCodeAudit;
		Integer maxAuditRecordsPerCodeAudit;
		Double stdAuditRecordsPerCodeAudit;

		Double avgAuditRecordPeriodLength;
		Double minAuditRecordPeriodLength;
		Double maxAuditRecordPeriodLength;
		Double stdAuditRecordPeriodLength;

		auditorId = super.getRequest().getPrincipal().getActiveRoleId();

		totalNumberOfStaticCodeAudits = this.repository.totalNumberOfStaticCodeAuditsByAuditorId(auditorId);
		totalNumberOfDynamicCodeAudits = this.repository.totalNumberOfDynamicCodeAuditsByAuditorId(auditorId);

		avgAuditRecordsPerCodeAudit = this.repository.avgAuditRecordsPerCodeAuditByAuditorId(auditorId);
		minAuditRecordsPerCodeAudit = this.repository.minAuditRecordsPerCodeAuditByAuditorId(auditorId);
		maxAuditRecordsPerCodeAudit = this.repository.maxAuditRecordsPerCodeAuditByAuditorId(auditorId);
		stdAuditRecordsPerCodeAudit = this.repository.stdAuditRecordsPerCodeAuditByAuditorId(auditorId);

		avgAuditRecordPeriodLength = this.repository.avgAuditRecordPeriodLengthByAuditorId(auditorId);
		minAuditRecordPeriodLength = this.repository.minAuditRecordPeriodLengthByAuditorId(auditorId);
		maxAuditRecordPeriodLength = this.repository.maxAuditRecordPeriodLengthByAuditorId(auditorId);
		stdAuditRecordPeriodLength = this.repository.stdAuditRecordPeriodLengthByAuditorId(auditorId);

		dashboard = new AuditorDashboard();

		dashboard.setTotalNumberOfStaticCodeAudits(totalNumberOfStaticCodeAudits);
		dashboard.setTotalNumberOfDynamicCodeAudits(totalNumberOfDynamicCodeAudits);

		dashboard.setAvgAuditRecordsPerCodeAudit(avgAuditRecordsPerCodeAudit);
		dashboard.setMinAuditRecordsPerCodeAudit(minAuditRecordsPerCodeAudit);
		dashboard.setMaxAuditRecordsPerCodeAudit(maxAuditRecordsPerCodeAudit);
		dashboard.setStdAuditRecordsPerCodeAudit(stdAuditRecordsPerCodeAudit);

		dashboard.setAvgAuditRecordPeriodLength(avgAuditRecordPeriodLength);
		dashboard.setMinAuditRecordPeriodLength(minAuditRecordPeriodLength);
		dashboard.setMaxAuditRecordPeriodLength(maxAuditRecordPeriodLength);
		dashboard.setStdAuditRecordPeriodLength(stdAuditRecordPeriodLength);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final AuditorDashboard object) {
		Dataset dataset;

		dataset = super.unbind(object, //
			"totalNumberOfStaticCodeAudits", //
			"totalNumberOfDynamicCodeAudits", //
			"avgAuditRecordsPerCodeAudit", //
			"minAuditRecordsPerCodeAudit", //
			"maxAuditRecordsPerCodeAudit", //
			"stdAuditRecordsPerCodeAudit", //
			"avgAuditRecordPeriodLength", //
			"minAuditRecordPeriodLength", //
			"maxAuditRecordPeriodLength", //
			"stdAuditRecordPeriodLength");

		super.getResponse().addData(dataset);
	}

}
