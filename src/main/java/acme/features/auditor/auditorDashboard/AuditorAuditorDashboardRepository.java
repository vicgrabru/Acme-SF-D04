/*
 * AuditorAuditorDashboardRepository.java
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

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AuditorAuditorDashboardRepository extends AbstractRepository {

	@Query("select count(ca) from CodeAudit ca where ca.type = acme.entities.codeAudit.AuditType.STATIC and ca.auditor.id = :auditorId and ca.draftMode = false")
	Integer totalNumberOfStaticCodeAuditsByAuditorId(int auditorId);

	@Query("select count(ca) from CodeAudit ca where ca.type = acme.entities.codeAudit.AuditType.DYNAMIC and ca.auditor.id = :auditorId and ca.draftMode = false")
	Integer totalNumberOfDynamicCodeAuditsByAuditorId(int auditorId);

	@Query("select avg(select count(ar) from AuditRecord ar where ar.codeAudit.id = ca.id and ar.draftMode = false) " //
		+ "from CodeAudit ca where ca.auditor.id = :auditorId and ca.draftMode = false")
	Double avgAuditRecordsPerCodeAuditByAuditorId(int auditorId);

	@Query("select min(select count(ar) from AuditRecord ar where ar.codeAudit.id = ca.id and ar.draftMode = false) " //
		+ "from CodeAudit ca where ca.auditor.id = :auditorId and ca.draftMode = false")
	Integer minAuditRecordsPerCodeAuditByAuditorId(int auditorId);

	@Query("select max(select count(ar) from AuditRecord ar where ar.codeAudit.id = ca.id and ar.draftMode = false) " //
		+ "from CodeAudit ca where ca.auditor.id = :auditorId and ca.draftMode = false")
	Integer maxAuditRecordsPerCodeAuditByAuditorId(int auditorId);

	default Double stdAuditRecordsPerCodeAuditByAuditorId(final int auditorId) {
		List<Integer> counts = this.auditRecordsPerCodeAuditByAuditorId(auditorId);

		if (counts == null || counts.size() < 2)
			return null;

		double mean = counts.stream().mapToDouble(Integer::doubleValue).average().orElse(0.0);
		double variance = counts.stream().mapToDouble(Integer::doubleValue).map( //
			count -> Math.pow(count - mean, 2)).average().orElse(0.0);

		return Math.sqrt(variance);
	}

	@Query("select count(*) from AuditRecord ar join CodeAudit ca on ar.codeAudit.id = ca.id " //
		+ "where ca.auditor.id = :auditorId and ca.draftMode = false and ar.draftMode = false group by ca.id")
	List<Integer> auditRecordsPerCodeAuditByAuditorId(int auditorId);

	@Query(value = "SELECT AVG(TIMESTAMPDIFF(second, ar.period_start, ar.period_end)) " //
		+ "FROM Audit_Record ar JOIN Code_Audit ca ON ar.code_audit_id = ca.id " //
		+ "WHERE ca.auditor_id = :auditorId AND ca.draft_mode = false AND ar.draft_mode = false", nativeQuery = true)
	Double avgAuditRecordPeriodLengthByAuditorId(@Param("auditorId") int auditorId);

	@Query(value = "SELECT MIN(TIMESTAMPDIFF(second, ar.period_start, ar.period_end)) " //
		+ "FROM Audit_Record ar JOIN Code_Audit ca ON ar.code_audit_id = ca.id " //
		+ "WHERE ca.auditor_id = :auditorId AND ca.draft_mode = false AND ar.draft_mode = false", nativeQuery = true)
	Double minAuditRecordPeriodLengthByAuditorId(@Param("auditorId") int auditorId);

	@Query(value = "SELECT MAX(TIMESTAMPDIFF(second, ar.period_start, ar.period_end)) " //
		+ "FROM Audit_Record ar JOIN Code_Audit ca ON ar.code_audit_id = ca.id " //
		+ "WHERE ca.auditor_id = :auditorId AND ca.draft_mode = false AND ar.draft_mode = false", nativeQuery = true)
	Double maxAuditRecordPeriodLengthByAuditorId(@Param("auditorId") int auditorId);

	@Query(value = "SELECT CASE " //
		+ "WHEN COUNT(ar.id) < 2 THEN NULL " //
		+ "ELSE STDDEV(TIMESTAMPDIFF(second, ar.period_start, ar.period_end)) " //
		+ "END " //
		+ "FROM Audit_Record ar JOIN Code_Audit ca ON ar.code_audit_id = ca.id " //
		+ "WHERE ca.auditor_id = :auditorId AND ca.draft_mode = false AND ar.draft_mode = false", nativeQuery = true)
	Double stdAuditRecordPeriodLengthByAuditorId(@Param("auditorId") int auditorId);
}
