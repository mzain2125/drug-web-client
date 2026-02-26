package Learn.web.client.repository;

import Learn.web.client.entity.IndividualDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndividualDocumentRepository extends JpaRepository<IndividualDocument, Long> {
}