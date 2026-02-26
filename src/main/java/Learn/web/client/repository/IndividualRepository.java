package Learn.web.client.repository;

import Learn.web.client.entity.Individual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndividualRepository extends JpaRepository<Individual, Long> {

    // Custom query example
    boolean existsByReferenceNumber(String referenceNumber);

}
