package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final ProductDao productDao;

    public MenuService(final MenuRepository menuRepository,
                       final ProductDao productDao) {
        this.menuRepository = menuRepository;
        this.productDao = productDao;
    }

    @Transactional
    public Menu create(final MenuRequest request) {
        final Menu menu = request.toEntity();
        if (!menuRepository.isGroupExist(menu)) {
            throw new IllegalArgumentException();
        }

        final List<Product> products = getProducts(request);
        final Price totalPriceOfProducts = Product.calculateTotalPrice(products);
        if (menu.isExpensiveThan(totalPriceOfProducts)) {
            throw new IllegalArgumentException();
        }

        return menuRepository.save(menu);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }

    private List<Product> getProducts(final MenuRequest request) {
        final List<MenuProductRequest> menuProductRequests = request.getMenuProducts();
        return menuProductRequests.stream()
                .map(this::getProductFrom)
                .collect(Collectors.toList());
    }

    private Product getProductFrom(final MenuProductRequest menuProductRequest) {
        return productDao.findById(menuProductRequest.getProductId())
                .orElseThrow(IllegalArgumentException::new);
    }
}
