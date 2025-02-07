package kitchenpos.product.application;

import java.util.List;
import kitchenpos.product.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest request) {
        final Product product = request.toEntity();
        productRepository.save(product);
        return ProductResponse.from(product);
    }

    public List<ProductResponse> list() {
        final List<Product> products = productRepository.findAll();
        return ProductResponse.from(products);
    }
}
