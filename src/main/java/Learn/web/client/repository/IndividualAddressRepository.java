package Learn.web.client.repository;

import Learn.web.client.entity.IndividualAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndividualAddressRepository extends JpaRepository<IndividualAddress, Long> {
}
