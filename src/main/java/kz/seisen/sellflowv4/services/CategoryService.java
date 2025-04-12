package kz.seisen.sellflowv4.services;

import kz.seisen.sellflowv4.repositories.CategoryRepository;
import kz.seisen.sellflowv4.entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }
    public void updateCategory(Category category) {
        categoryRepository.save(category);
    }
}
