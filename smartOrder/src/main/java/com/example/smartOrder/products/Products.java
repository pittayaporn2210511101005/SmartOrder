package com.example.smartOrder.products;

import com.example.smartOrder.category.Category;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", nullable = false, length = 100)
    private String productName;

    @Column(name = "buy_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal buyPrice;

    @Column(name = "sell_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal sellPrice;

    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt;

    @Column(name = "update_at", nullable = false)
    private LocalDateTime updateAt;

    @Column(name = "min_stock_qty", nullable = false)
    private int minStockQty;

    @Column(name = "warehouse_stock", nullable = false)
    private int warehouseStock;

    @Column(name = "store_stock", nullable = false)
    private int storeStock;

    @Lob
    @Column(name = "image_url", columnDefinition = "LONGTEXT")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public Products() {}

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createAt = now;
        updateAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updateAt = LocalDateTime.now();
    }

    // ===== Getter Setter =====


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public int getMinStockQty() {
        return minStockQty;
    }

    public void setMinStockQty(int minStockQty) {
        this.minStockQty = minStockQty;
    }

    public int getWarehouseStock() {
        return warehouseStock;
    }

    public void setWarehouseStock(int warehouseStock) {
        this.warehouseStock = warehouseStock;
    }

    public int getStoreStock() {
        return storeStock;
    }

    public void setStoreStock(int storeStock) {
        this.storeStock = storeStock;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Transient
    public int getTotalStock() {
        return warehouseStock + storeStock;
    }

}