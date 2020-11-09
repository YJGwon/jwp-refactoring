package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.TableChangeRequest;
import kitchenpos.dto.TableCreateRequest;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.dto.TableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/truncate.sql")
class TableGroupServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableGroupService tableGroupService;

    private TableResponse tableA;
    private TableResponse tableB;
    private TableResponse tableC;

    @BeforeEach
    void setUp() {
        TableCreateRequest table = new TableCreateRequest(true, 0);

        tableA = tableService.create(table);
        tableB = tableService.create(table);
        tableC = tableService.create(table);
    }

    @Test
    @DisplayName("create")
    void create() {
        TableGroupCreateRequest request = new TableGroupCreateRequest(
            Arrays.asList(tableA.getId(), tableB.getId(), tableC.getId()));

        TableGroupResponse result = tableGroupService.create(request);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getTables()).hasSize(3);

        // group 에 속한 테이블들은 empty 가 false 로 바뀐다.
        for (TableResponse table : result.getTables()) {
            assertThat(table.isEmpty()).isFalse();
        }
    }

    @Test
    @DisplayName("create - 테이블 한개로 그룹 생성을 시도할 경우 예외처리")
    void create_IfTryWithOneTable_ThrowException() {
        TableGroupCreateRequest request = new TableGroupCreateRequest(
            Collections.singletonList(tableA.getId()));

        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("create - 테이블이 없는 테이블 그룹 생성을 시도할 경우 예외처리")
    void create_IfTryWithoutTable_ThrowException(List<Long> tableIds) {
        TableGroupCreateRequest request = new TableGroupCreateRequest(tableIds);

        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("create - 비어있지 않은 테이블을 테이블 그룹에 포함시키기를 시도할 경우 예외처리")
    void create_IfTryWithNotEmptyTable_ThrowException() {
        // given
        TableChangeRequest changeRequest = new TableChangeRequest(false);
        tableService.changeEmpty(tableB.getId(), changeRequest);

        // when & then
        TableGroupCreateRequest request = new TableGroupCreateRequest(
            Arrays.asList(tableA.getId(), tableB.getId(), tableC.getId()));

        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("create - 다른 그룹에 속해있는 테이블을 새 그룹에 포함시키려 할 경우 예외처리")
    void create_IfTryWithTableBelongingAnotherGroup_ThrowException() {
        // given
        TableGroupCreateRequest anotherGroupRequest = new TableGroupCreateRequest(
            Arrays.asList(tableA.getId(), tableB.getId()));
        tableGroupService.create(anotherGroupRequest);

        // when & then
        TableGroupCreateRequest request = new TableGroupCreateRequest(
            Arrays.asList(tableA.getId(), tableB.getId(), tableC.getId()));

        assertThatThrownBy(() -> tableGroupService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("ungroup")
    void ungroup() {
        // given
        TableGroupCreateRequest request = new TableGroupCreateRequest(
            Arrays.asList(tableA.getId(), tableB.getId(), tableC.getId()));

        TableGroupResponse result = tableGroupService.create(request);

        // when
        tableGroupService.ungroup(result.getId());

        // then
        for (TableResponse response : tableService.list()) {
            assertThat(response.getTableGroupId()).isNull();
        }
    }

    // todo: 이거 예외처리 하도록 바꾸자
    @Test
    @DisplayName("ungroup - 존재하지 않는 테이블 그룹에 대한 ungroup 요청시")
    void ungroup_IfTryWithNotExistTableGroupId_ThrowException() {
        List<TableResponse> beforeTables = tableService.list();

        // when
        tableGroupService.ungroup(100L);

        // then 아무런 변화 없음
        assertThat(tableService.list()).hasSize(beforeTables.size());

        for (TableResponse response : beforeTables) {
            assertThat(response.getTableGroupId()).isNull();
        }
    }

    @Test
    @DisplayName("ungroup - 주문상태가 COMPLETION 이 아닌 테이블이 그룹에 속해있는 경우 예외처리")
    void ungroup_IfAnyTableIsNotInCompletion_ThrowException() {
        // given
        MenuResponse menu = createMenu_후라이드세트();

        TableGroupResponse tableGroup = groupTableABC();

        orderOneMenu(tableA, menu);
        orderOneMenu(tableB, menu);
        orderOneMenu(tableC, menu);

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private TableGroupResponse groupTableABC() {
        TableGroupCreateRequest request = new TableGroupCreateRequest(
            Arrays.asList(tableA.getId(), tableB.getId(), tableC.getId()));
        return tableGroupService.create(request);
    }

    private Order orderOneMenu(TableResponse table, MenuResponse menu) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(2);

        Order order = new Order();
        order.setOrderLineItems(Collections.singletonList(orderLineItem));

        order.setOrderTableId(table.getId());
        return orderService.create(order);
    }

    private MenuResponse createMenu_후라이드세트() {
        // create products
        ProductRequest 후라이드치킨_request = new ProductRequest("후라이드치킨", BigDecimal.valueOf(10_000));
        Product 후라이드치킨 = productService.create(후라이드치킨_request);

        ProductRequest 프랜치프라이_request = new ProductRequest("프랜치프라이", BigDecimal.valueOf(5_000));
        Product 프랜치프라이 = productService.create(프랜치프라이_request);

        // create a menu group
        MenuGroupRequest 세트메뉴_request = new MenuGroupRequest("세트메뉴");
        MenuGroup 세트메뉴 = menuGroupService.create(세트메뉴_request);

        // create menu
        List<MenuProductRequest> menuProducts = createMenuProductsWithAllQuantityAsOne(
            Arrays.asList(후라이드치킨, 프랜치프라이));

        MenuRequest request = new MenuRequest("후라이드 세트", BigDecimal.valueOf(13_000), 세트메뉴.getId(), menuProducts);

        return menuService.create(request);
    }

    private List<MenuProductRequest> createMenuProductsWithAllQuantityAsOne(List<Product> products) {
        List<MenuProductRequest> menuProducts = products.stream()
            .map(product -> new MenuProductRequest(product.getId(), 1))
            .collect(Collectors.toList());

        return Collections.unmodifiableList(menuProducts);
    }
}
