package kitchenpos.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Arrays;
import java.util.List;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.support.DataSupport;
import kitchenpos.support.RequestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;
    @Autowired
    private DataSupport dataSupport;

    @DisplayName("새로운 상품을 등록할 수 있다.")
    @Test
    void create() {
        // given, when
        final ProductRequest request = RequestBuilder.ofProduct();
        final ProductResponse response = productService.create(request);

        // then
        assertThat(response.getId()).isNotNull();
    }

    @DisplayName("상품을 등록할 때 상품 가격을 입력하지 않으면 예외가 발생한다.")
    @Test
    void create_throwsException_ifNoPrice() {
        final ProductRequest request = RequestBuilder.ofProduct(null);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> productService.create(request));
    }

    @ValueSource(ints = {-1, -500, -1000})
    @ParameterizedTest(name = "상품을 등록할 때 상품 가격이 0보다 작은 {0}이면 예외가 발생한다.")
    void create_throwsException_ifPriceUnder0(final int price) {
        final ProductRequest request = RequestBuilder.ofProduct(price);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> productService.create(request));
    }

    @DisplayName("상품의 전체 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        final Product savedProduct1 = dataSupport.saveProduct("치킨마요", 3500);
        final Product savedProduct2 = dataSupport.saveProduct("참치마요", 4000);
        final List<ProductResponse> expected = ProductResponse.from(Arrays.asList(savedProduct1, savedProduct2));

        // when
        final List<ProductResponse> responses = productService.list();

        // then
        assertThat(responses)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expected);
    }
}
