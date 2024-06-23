/*
 * AnyAuditRecordRepository.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.any.auditRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.codeAudit.AuditRecord;
import acme.entities.codeAudit.CodeAudit;

@Repository
public interface AnyAuditRecordRepository extends AbstractRepository {

	@Query("select ca from CodeAudit ca where ca.id = :id")
	CodeAudit findOneCodeAuditById(int id);

	@Query("select ar from AuditRecord ar where ar.codeAudit.id = :codeAuditId " //
		+ "and ar.draftMode = false")
	Collection<AuditRecord> findAllPublishedAuditRecordsByCodeAuditId(int codeAuditId);

	@Query("select ar from AuditRecord ar where ar.id = :id")
	AuditRecord findOneAuditRecordById(int id);
}
