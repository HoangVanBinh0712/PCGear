package pc.gear.service;

import pc.gear.request.payment.CreatePaymentRequest;

public interface PaymentService {
    void create(CreatePaymentRequest request);
}
