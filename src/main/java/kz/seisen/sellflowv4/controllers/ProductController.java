package kz.seisen.sellflowv4.controllers;

import kz.seisen.sellflowv4.entities.City;
import kz.seisen.sellflowv4.entities.Image;
import kz.seisen.sellflowv4.services.*;
import kz.seisen.sellflowv4.entities.Category;
import kz.seisen.sellflowv4.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
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
            @RequestParam Long category,
            @RequestParam("images") MultipartFile[] imageFiles) throws IOException {

        Product product = new Product();
        product.setTitle(title);
        product.setDescription(description);
        product.setPrice(price);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        product.setAuthor(auth.getName());

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









    @GetMapping("/edit/{id}")
    public String showEditProduct(@PathVariable Long id, Model model, Principal principal) {
        Product product = productService.getProductById(id);

        if (product == null || !product.getAuthor().equals(principal.getName())) {
            return "redirect:/?error=unauthorized";
        }
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("cities", cityService.getAllCities());
        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id,
                                @RequestParam String title,
                                @RequestParam String description,
                                @RequestParam int price,
                                @RequestParam Long city,
                                @RequestParam Long category,
                                @RequestParam("images") MultipartFile[] imageFiles,
                                Principal principal) throws IOException {

        Product product = productService.getProductById(id);
        if (product == null || !product.getAuthor().equals(principal.getName())) {
            return "redirect:/?error=unauthorized";
        }

        product.setTitle(title);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(categoryService.getCategoryById(category));
        product.setCity(cityService.getCityById(city));

        // Check if new images were uploaded
        boolean newImagesUploaded = Arrays.stream(imageFiles)
                .anyMatch(file -> !file.isEmpty());

        if (newImagesUploaded) {
            product.getImages().clear();

            for (MultipartFile file : imageFiles) {
                if (file.getSize() > ProductController.MAX_IMAGE_SIZE) {
                    continue;
                }
                if (!file.isEmpty() && file.getContentType().startsWith("image/")) {
                    Image image = new Image();
                    image.setName(file.getOriginalFilename());
                    image.setContentType(file.getContentType());
                    image.setBase64Data(Base64.getEncoder().encodeToString(file.getBytes()));
                    product.addImage(image);
                }
            }
        }

        productService.saveProduct(product);
        return "redirect:/";
    }


    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, Principal principal) {
        Product product = productService.getProductById(id);
        if (product != null && product.getAuthor().equals(principal.getName())) {
            productService.deleteProduct(id);
        }
        return "redirect:/";
    }






}
