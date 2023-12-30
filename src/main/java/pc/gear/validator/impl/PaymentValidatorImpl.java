package pc.gear.validator.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pc.gear.config.exception.PcGearException;
import pc.gear.entity.CustomerOrder;
import pc.gear.repository.CustomerOderRepository;
import pc.gear.repository.ProductRepository;
import pc.gear.request.payment.CreatePaymentRequest;
import pc.gear.service.BaseService;
import pc.gear.util.MessageConstants;
import pc.gear.validator.PaymentValidator;

@Service
public class PaymentValidatorImpl extends BaseService implements PaymentValidator {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerOderRepository customerOderRepository;

    @Override
    public CustomerOrder validateCreate(CreatePaymentRequest request) {
        // Check that the customer has already paid with payment code and paymentMethod
        // This is a tutorial so just skip the checking step.

        // Check that order is exist
        CustomerOrder order = customerOderRepository.findById(request.getOrderId()).orElseThrow(() -> new PcGearException(this.getMessage(MessageConstants.DATA_NOT_FOUND, this.getMessage(MessageConstants.ORDER))));
        // Check the amount is the same
        if (order.getTotalAmount().compareTo(request.getPaymentAmount()) != 0) {
            // throw something
            this.throwError(MessageConstants.AMOUNT_NOT_ENOUGH);
        }
        return order;
    }

}
