package pc.gear.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pc.gear.entity.CustomerOrder;
import pc.gear.entity.Payment;
import pc.gear.repository.CustomerRepository;
import pc.gear.repository.PaymentRepository;
import pc.gear.request.payment.CreatePaymentRequest;
import pc.gear.service.BaseService;
import pc.gear.service.PaymentService;
import pc.gear.util.lang.DateUtil;
import pc.gear.util.lang.JwtUtil;
import pc.gear.validator.PaymentValidator;

@Service
public class PaymentServiceImpl extends BaseService implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentValidator paymentValidator;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    @Transactional
    public void create(CreatePaymentRequest request) {
        CustomerOrder order = paymentValidator.validateCreate(request);
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setCustomer(customerRepository.getReferenceById(JwtUtil.getCurrentUserId()));
        payment.setAmount(request.getPaymentAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPaymentDate(DateUtil.currentDate());
        paymentRepository.save(payment);
    }

}
