package kz.seisen.sellflowv4.repositories;

import kz.seisen.sellflowv4.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByTitleContainingIgnoreCase(String title);

    List<Product> findByCategoryId(Long categoryId);
}
