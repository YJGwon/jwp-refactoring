package kitchenpos.product.dto;

import java.math.BigDecimal;
import kitchenpos.product.Product;

public class ProductRequest {

    private String name;
    private BigDecimal price;

    public ProductRequest(final String name, final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product toEntity() {
        return Product.ofUnsaved(name, price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    private ProductRequest() {
    }
}
