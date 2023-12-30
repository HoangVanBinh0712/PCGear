package pc.gear.validator;

import pc.gear.entity.CustomerOrder;
import pc.gear.request.payment.CreatePaymentRequest;

public interface PaymentValidator {
    CustomerOrder validateCreate(CreatePaymentRequest request);
}
