# 키친포스

## 요구 사항
### Product
1. 상품을 등록할 수 있어야 한다.
  - 상품의 가격은 0원 이상이어야 한다.
2. 가게 전체의 상품을 조회할 수 있어야 한다.
### MenuGroup
1. 메뉴 그룹을 등록할 수 있어야 한다.
2. 메뉴 그룹 전체를 조회할 수 있어야 한다.
### Menu 
1. 주문 가능 단위인 메뉴를 등록할 수 있어야 한다.
  - 메뉴의 가격은 0원 이상이어야 한다.
  - 메뉴는 가게에 등록된 메뉴 그룹에 속해야 한다.
  - 메뉴의 가격은 각 메뉴 상품(MenuProduct) 가격 * 수량보다 낮아야 한다.
2. 메뉴 전체를 조회할 수 있어야 한다.
### Order
1. 주문을 등록할 수 있어야 한다.
  - 주문은 적어도 1개 이상의 주문 내역이 있어야 한다.
  - 가게에 등록된 메뉴만 주문할 수 있다.
  - 비어있는 테이블에는 주문을 등록할 수 없다.
2. 전체 주문을 조회할 수 있어야 한다.
3. 주문 상태를 바꿀 수 있어야 한다.
  - 계산이 완료된 주문은 수정이 불가능하다.
  - 주문 상태가 변경된 내용을 조회할 수 있어야 한다.
### OrderTable
1. 주문이 발생하는 영역을 나타내는 테이블을 등록할 수 있어야 한다.
2. 전체 테이블을 조회할 수 있어야 한다.
3. 테이블을 비워 주문을 받지 않거나 다시 채울 수 있어야 한다.
  - 단체로 지정된 테이블은 상태 전환이 불가능하다.
  - 테이블에 남아있는 주문 상태가 조리또는 식사 중일 경우 상태 전환이 불가능하다.
4. 현재 테이블에 있는 손님 수를 수정할 수 있어야 한다.
  - 손님 수는 0이상이어야 한다.
  - 비어있는 테이블은 손님 수를 수정할 수 없다.
### TableGroup
1. 테이블을 묶어 단체 지정을 할 수 있어야 한다.
  - 적어도 단체 지정할 테이블은 2개 이상이어야 한다.
  - 현재 가게에 존재하는 테이블을 지정해야 한다.
  - 테이블은 현재 단체 지정이 되어있지 않아야 하며, 비어있는 테이블이어서는 안된다.
2.  단체 지정을 해제할 수 있어야 한다.
  - 현재 조리 중이거나 식사 중인 테이블일 경우 단체 지정을 해제할 수 없다.

## 용어 사전

| 한글명 | 영문명 | 설명 |
| --- | --- | --- |
| 상품 | product | 메뉴를 관리하는 기준이 되는 데이터 |
| 메뉴 그룹 | menu group | 메뉴 묶음, 분류 |
| 메뉴 | menu | 메뉴 그룹에 속하는 실제 주문 가능 단위 |
| 메뉴 상품 | menu product | 메뉴에 속하는 수량이 있는 상품 |
| 금액 | amount | 가격 * 수량 |
| 주문 테이블 | order table | 매장에서 주문이 발생하는 영역 |
| 빈 테이블 | empty table | 주문을 등록할 수 없는 주문 테이블 |
| 주문 | order | 매장에서 발생하는 주문 |
| 주문 상태 | order status | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정 | table group | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목 | order line item | 주문에 속하는 수량이 있는 메뉴 |
| 매장 식사 | eat in | 포장하지 않고 매장에서 식사하는 것 |
