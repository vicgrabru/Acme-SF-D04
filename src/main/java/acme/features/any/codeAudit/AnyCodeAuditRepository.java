
package acme.features.any.codeAudit;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.codeAudit.CodeAudit;
import acme.entities.codeAudit.Mark;

@Repository
public interface AnyCodeAuditRepository extends AbstractRepository {

	@Query("select ca from CodeAudit ca where ca.draftMode = false")
	Collection<CodeAudit> findPublishedCodeAudits();

	@Query("select ar.mark, count(ar.mark) as markCount " //
		+ "from AuditRecord ar " //
		+ "where ar.codeAudit.id = :codeAuditId and ar.draftMode = false " //
		+ "group by ar.mark " //
		+ "order by markCount desc")
	Collection<Mark> findOrderedMarkAmountsByCodeAuditId(int codeAuditId);

	@Query("select ca from CodeAudit ca where ca.id = :id")
	CodeAudit findOneCodeAuditById(int id);

}
