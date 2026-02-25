package Learn.web.client.repository;

import Learn.web.client.entity.IndividualAlias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndividualAliasRepository extends JpaRepository<IndividualAlias, Long> {
}
