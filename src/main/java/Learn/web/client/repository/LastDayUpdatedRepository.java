package Learn.web.client.repository;

import Learn.web.client.entity.LastDayUpdated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LastDayUpdatedRepository extends JpaRepository<LastDayUpdated, Long> {
}
