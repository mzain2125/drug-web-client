package Learn.web.client.repository;

import Learn.web.client.entity.Individual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndividualRepository extends JpaRepository<Individual, Long> {
    // you can add custom queries if needed later
}
