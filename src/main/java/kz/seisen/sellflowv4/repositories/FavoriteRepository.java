package kz.seisen.sellflowv4.repositories;

import kz.seisen.sellflowv4.entities.Favorite;
import kz.seisen.sellflowv4.entities.User;
import kz.seisen.sellflowv4.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUser(User user);
    Optional<Favorite> findByUserAndProduct(User user, Product product);
    void deleteByUserAndProduct(User user, Product product);

    @Modifying
    @Transactional
    void deleteByProduct(Product product);
}
