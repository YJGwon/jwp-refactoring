# 키친포스

## 요구 사항

### 상품
- 상품을 등록할 수 있다.
  - 상품 가격은 반드시 입력해야 한다.
  - 상품 가격은 0보다 작을 수 없다.
- 상품 전체 목록을 조회할 수 있다.

### 메뉴 그룹
- 메뉴 그룹을 등록할 수 있다.
- 메뉴 그룹 전체 목록을 조회할 수 있다.

### 메뉴
- 메뉴를 등록할 수 있다.
  - 메뉴 가격은 반드시 입력해야 한다.
  - 메뉴 가격은 0보다 작을 수 없다.
  - 메뉴 가격은 메뉴 상품 가격의 총 합보다 클 수 없다.
- 메뉴 전체 목록을 조회할 수 있다. 

### 테이블
- 테이블을 등록할 수 있다.
- 테이블 전체 목록을 조회할 수 있다.
- 테이블이 비었는지 상태를 변경할 수 있다.
  - 단체 지정된 테이블의 상태는 변경할 수 없다.
  - 조리중이거나 식사중인 테이블의 상태는 변경할 수 없다.
- 테이블의 고객 수를 변경할 수 있다.
  - 고객 수는 0보다 작을 수 없다.
  - 빈 테이블의 고객 수는 변경할 수 없다.

### 단체 지정
- 여러 테이블을 단체로 지정할 수 있다.
  - 단체에 속한 테이블 수는 2보다 적을 수 없다.
  - 비어있지 않은 테이블은 단체로 지정할 수 없다.
  - 이미 단체로 지정된 테이블은 단체로 지정할 수 없다.
- 단체 지정을 해제할 수 있다.
  - 조리중이거나 식사중인 테이블이 있는 경우 단체를 해제할 수 없다.

### 주문
- 테이블 별로 메뉴를 주문할 수 있다.
  - 빈 테이블에서는 주문할 수 없다.
  - 메뉴가 주문된 후에는 주문 상태가 조리로 변경된다.
- 주문 전체 목록을 조회할 수 있다.
- 주문 상태를 변경할 수 있다.
  - 이미 완료된 주문의 상태는 변경할 수 없다.


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
