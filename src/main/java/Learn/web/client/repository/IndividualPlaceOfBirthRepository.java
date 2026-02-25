package Learn.web.client.repository;


import Learn.web.client.entity.IndividualPlaceOfBirth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndividualPlaceOfBirthRepository extends JpaRepository<IndividualPlaceOfBirth, Long> {
}
