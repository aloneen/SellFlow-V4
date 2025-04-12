package kz.seisen.sellflowv4.entities;


import jakarta.persistence.*;
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String contentType;

    @Column(columnDefinition = "TEXT")
    private String base64Data;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // Constructors
    public Image() {}

    public Image(String name, String contentType, String base64Data) {
        this.name = name;
        this.contentType = contentType;
        this.base64Data = base64Data;
    }

    // Getters and setters
    // ...


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getBase64Data() {
        return base64Data;
    }

    public void setBase64Data(String base64Data) {
        this.base64Data = base64Data;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}