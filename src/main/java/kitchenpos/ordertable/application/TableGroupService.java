package kitchenpos.ordertable.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dto.request.IdRequest;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.OrderTables;
import kitchenpos.ordertable.TableGroup;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.ordertable.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public TableGroupService(final TableGroupRepository tableGroupRepository,
                             final OrderTableRepository orderTableRepository,
                             final OrderTableValidator orderTableValidator) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final List<IdRequest> orderTableIdRequests = request.getOrderTables();
        final List<OrderTable> savedOrderTables = orderTableIdRequests.stream()
                .map(this::getOrderTableFrom)
                .collect(Collectors.toList());
        final OrderTables orderTables = new OrderTables(savedOrderTables);

        final TableGroup tableGroup = TableGroup.ofUnsaved();
        tableGroupRepository.save(tableGroup);
        orderTables.joinGroup(tableGroup.getId());

        return TableGroupResponse.from(tableGroup, orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findByTableGroupId(tableGroupId);
        final OrderTables groupedTables = new OrderTables(orderTables);

        final List<Long> orderTableIds = groupedTables.getIds();
        orderTableValidator.checkOrderCompleted(orderTableIds);

        groupedTables.ungroup();
    }

    private OrderTable getOrderTableFrom(final IdRequest request) {
        return orderTableRepository.findById(request.getId())
                .orElseThrow(IllegalArgumentException::new);
    }
}
