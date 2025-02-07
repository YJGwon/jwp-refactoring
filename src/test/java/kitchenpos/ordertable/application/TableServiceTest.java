package kitchenpos.ordertable.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.OrderStatus;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.TableGroup;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.support.DataSupport;
import kitchenpos.support.RequestBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableServiceTest {

    @Autowired
    private TableService tableService;
    @Autowired
    private DataSupport dataSupport;

    @DisplayName("새로운 테이블을 등록할 수 있다.")
    @Test
    void create() {
        // given, when
        final OrderTableRequest request = RequestBuilder.ofEmptyTable();
        final OrderTableResponse savedTable = tableService.create(request);

        // then
        assertThat(savedTable.getId()).isNotNull();
    }

    @DisplayName("테이블의 전체 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        final OrderTable savedTable1 = dataSupport.saveOrderTable(0, true);
        final OrderTable savedTable2 = dataSupport.saveOrderTable(2, false);
        final List<OrderTableResponse> expected = OrderTableResponse.from(Arrays.asList(savedTable1, savedTable2));

        // when
        final List<OrderTableResponse> responses = tableService.list();

        // then
        assertThat(responses)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expected);
    }

    @DisplayName("테이블이 비었는지 상태를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        // given
        final OrderTable savedOrderTable = dataSupport.saveOrderTable(0, true);
        final Long orderTableId = savedOrderTable.getId();

        // when
        final OrderTableRequest request = RequestBuilder.ofFullTable();
        tableService.changeEmpty(orderTableId, request);

        // then
        final OrderTable changedTable = dataSupport.findOrderTable(orderTableId);
        assertThat(changedTable.isEmpty()).isFalse();
    }

    @DisplayName("존재하지 않는 테이블의 상태를 변경하면 예외가 발생한다.")
    @Test
    void changeEmpty_throwsException_whenTableNotFound() {
        final OrderTableRequest request = RequestBuilder.ofFullTable();
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableService.changeEmpty(0L, request));
    }

    @DisplayName("단체 지정된 테이블의 상태를 변경하면 예외가 발생한다.")
    @Test
    void changeEmpty_throwsException_whenHasGroup() {
        // given
        final TableGroup tableGroup = dataSupport.saveTableGroup();
        final List<OrderTable> groupedTables = dataSupport.saveTwoGroupedTables(tableGroup);
        final OrderTable groupedTable = groupedTables.get(0);
        final Long orderTableId = groupedTable.getId();

        // when, then
        final OrderTableRequest request = RequestBuilder.ofFullTable();
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableService.changeEmpty(orderTableId, request));
    }

    @DisplayName("조리중인 테이블의 상태를 변경하면 예외가 발생한다.")
    @Test
    void changeEmpty_throwsException_whenCooking() {
        // given
        final OrderTable savedOrderTable = dataSupport.saveOrderTable(0, true);
        final Long orderTableId = savedOrderTable.getId();
        dataSupport.saveOrderWithoutItem(orderTableId, OrderStatus.COOKING);

        // when, then
        final OrderTableRequest request = RequestBuilder.ofFullTable();
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableService.changeEmpty(orderTableId, request));
    }

    @DisplayName("식사중인 테이블의 상태를 변경하면 예외가 발생한다.")
    @Test
    void changeEmpty_throwsException_whenMeal() {
        // given
        final OrderTable savedOrderTable = dataSupport.saveOrderTable(0, true);
        final Long orderTableId = savedOrderTable.getId();
        dataSupport.saveOrderWithoutItem(orderTableId, OrderStatus.MEAL);

        // when, then
        final OrderTableRequest request = RequestBuilder.ofFullTable();
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableService.changeEmpty(orderTableId, request));
    }

    @DisplayName("테이블의 고객 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        final OrderTable savedOrderTable = dataSupport.saveOrderTable(0, false);
        final Long orderTableId = savedOrderTable.getId();
        final int numberOfGuests = 2;

        // when
        final OrderTableRequest request = RequestBuilder.ofTableWithGuests(numberOfGuests);
        tableService.changeNumberOfGuests(orderTableId, request);

        // then
        final OrderTable changedTable = dataSupport.findOrderTable(orderTableId);
        assertThat(changedTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @DisplayName("존재하지 않는 테이블의 고객 수를 변경하면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_throwsException_ifTableNotFound() {
        final OrderTableRequest request = RequestBuilder.ofTableWithGuests(2);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableService.changeNumberOfGuests(0L, request));
    }

    @DisplayName("테이블의 고객 수를 0보다 작게 변경하면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_throwsException_ifGuestsUnder0() {
        // given
        final OrderTable savedOrderTable = dataSupport.saveOrderTable(0, false);
        final Long orderTableId = savedOrderTable.getId();

        // when, then
        final OrderTableRequest request = RequestBuilder.ofTableWithGuests(-1);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, request));
    }

    @DisplayName("빈 테이블의 고객 수를 변경하면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_throwsException_ifEmpty() {
        // given
        final OrderTable savedOrderTable = dataSupport.saveOrderTable(0, true);
        final Long orderTableId = savedOrderTable.getId();

        // when, then
        final OrderTableRequest request = RequestBuilder.ofTableWithGuests(2);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, request));
    }
}
