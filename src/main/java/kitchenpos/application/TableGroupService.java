package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.IdRequest;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final OrderDao orderDao;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final OrderDao orderDao) {
        this.orderTableRepository = orderTableRepository;
        this.orderDao = orderDao;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest request) {
        final List<IdRequest> orderTableIdRequests = request.getOrderTables();
        final List<OrderTable> savedOrderTables = orderTableIdRequests.stream()
                .map(this::getOrderTableFrom)
                .collect(Collectors.toList());

        final TableGroup tableGroup = TableGroup.ofNew();
        final TableGroup savedTableGroup = orderTableRepository.saveTableGroup(tableGroup);

        final List<OrderTable> groupedTables = savedTableGroup.groupTables(savedOrderTables);
        orderTableRepository.saveAll(groupedTables);

        return savedTableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findGroupedTables(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
            orderTableRepository.save(orderTable);
        }
    }

    private OrderTable getOrderTableFrom(final IdRequest request) {
        return orderTableRepository.findById(request.getId())
                .orElseThrow(IllegalArgumentException::new);
    }
}
