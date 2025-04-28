package kz.seisen.sellflowv4.repositories;

import kz.seisen.sellflowv4.entities.SupportMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportMessageRepository extends JpaRepository<SupportMessage, Long> {
}
