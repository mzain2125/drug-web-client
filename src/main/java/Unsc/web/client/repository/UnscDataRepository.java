package Unsc.web.client.repository;

import Unsc.web.client.entity.UnscData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnscDataRepository extends JpaRepository<UnscData, Long> {
}
