package kz.seisen.sellflowv4.controllers;


import kz.seisen.sellflowv4.entities.Category;
import kz.seisen.sellflowv4.entities.Product;
import kz.seisen.sellflowv4.services.CategoryService;
import kz.seisen.sellflowv4.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }


    @GetMapping
    public String index(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId,
            Model model) {

        List<Product> products;
        if (search != null && !search.isEmpty()) {
            products = productService.searchByTitle(search);
        } else if (categoryId != null) {
            products = productService.filterByCategory(categoryId);
        } else {
            products = productService.getAllProducts();
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("selectedCategory", categoryId);
        model.addAttribute("searchQuery", search);
        return "index";
    }


    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("category", product.getCategory().getName());
        return "detail";
    }



    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "add";
    }


    @PostMapping("/add")
    public String addProduct(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam int price,
            @RequestParam String city,
            @RequestParam String author,
            @RequestParam Long category) {

        Product product = new Product();
        product.setTitle(title);
        product.setDescription(description);
        product.setPrice(price);
        product.setCity(city);
        product.setAuthor(author);


        Category productCategory = categoryService.getCategoryById(category);
        product.setCategory(productCategory);

        productService.saveProduct(product);
        return "redirect:/";
    }




}
