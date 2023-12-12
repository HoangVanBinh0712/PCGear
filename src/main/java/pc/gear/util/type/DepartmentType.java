package pc.gear.util.type;

import lombok.Getter;

@Getter
public enum DepartmentType {
    CUSTOMER("customer", "CUSTOMER"),
    PRODUCT_MANAGER("product_manager", "Product Manager"),
    ORDER_MANAGER("order_manager", "Order Manager");

    String key;

    String value;

    DepartmentType(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
