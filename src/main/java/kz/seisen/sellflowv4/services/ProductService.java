package kz.seisen.sellflowv4.services;

import kz.seisen.sellflowv4.entities.Image;
import kz.seisen.sellflowv4.entities.Product;
import kz.seisen.sellflowv4.repositories.ImageRepository;
import kz.seisen.sellflowv4.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ImageRepository imageRepository;





    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElse(null);
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    public void updateProduct(Product product) {
        productRepository.save(product);
    }
    public List<Product> searchByTitle(String title) {
        return productRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Product> filterByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }


}
