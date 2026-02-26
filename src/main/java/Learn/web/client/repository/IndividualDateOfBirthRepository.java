package Learn.web.client.repository;

import Learn.web.client.entity.IndividualDateOfBirth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndividualDateOfBirthRepository extends JpaRepository<IndividualDateOfBirth, Long> {
}
