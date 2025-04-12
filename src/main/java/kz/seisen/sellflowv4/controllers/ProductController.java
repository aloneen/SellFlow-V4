package kz.seisen.sellflowv4.controllers;

import kz.seisen.sellflowv4.entities.City;
import kz.seisen.sellflowv4.entities.Image;
import kz.seisen.sellflowv4.services.*;
import kz.seisen.sellflowv4.entities.Category;
import kz.seisen.sellflowv4.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final CityService cityService;


    private static final long MAX_IMAGE_SIZE = 2 * 1024 * 1024;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService, CityService cityService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.cityService = cityService;
    }


    @GetMapping
    public String index(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long cityId,
            Model model) {

        List<Product> products;

        if (search != null && !search.isEmpty()) {
            products = productService.searchByTitle(search);
        } else if (categoryId != null && cityId != null) {
            // Both category and city are selected.
            products = productService.filterByCategoryAndCity(categoryId, cityId);
        } else if (categoryId != null) {
            // Only category is selected.
            products = productService.filterByCategory(categoryId);
        } else if (cityId != null) {
            // Only city is selected.
            products = productService.filterByCity(cityId);
        } else {
            // No filter at all.
            products = productService.getAllProducts();
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("cities", cityService.getAllCities());
        model.addAttribute("selectedCategory", categoryId);
        model.addAttribute("selectedCity", cityId);
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
        model.addAttribute("cities", cityService.getAllCities());
        return "add";
    }


    @PostMapping("/add")
    public String addProduct(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam int price,
            @RequestParam Long city,
            @RequestParam String author,
            @RequestParam Long category,
            @RequestParam("images") MultipartFile[] imageFiles) throws IOException {

        Product product = new Product();
        product.setTitle(title);
        product.setDescription(description);
        product.setPrice(price);
        product.setAuthor(author);

        Category productCategory = categoryService.getCategoryById(category);
        product.setCategory(productCategory);


        City productCity = cityService.getCityById(city);
        product.setCity(productCity);

        for (MultipartFile file : imageFiles) {
            if (file.getSize() > MAX_IMAGE_SIZE) {
                return "redirect:/";
            }
            if (!file.isEmpty() && !file.getContentType().startsWith("image/")) {
                continue;
            }
            Image image = new Image();
            image.setName(file.getOriginalFilename());
            image.setContentType(file.getContentType());
            image.setBase64Data(Base64.getEncoder().encodeToString(file.getBytes()));
            product.addImage(image);
        }

        productService.saveProduct(product);
        return "redirect:/";

    }







}
