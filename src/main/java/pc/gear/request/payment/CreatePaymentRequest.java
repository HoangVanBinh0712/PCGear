package pc.gear.request.payment;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import pc.gear.annotaion.interfaces.EnumValue;
import pc.gear.util.type.PaymentMethod;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePaymentRequest {
    // The code response from the 3rd party
    @NotBlank
    @Size(max = 100)
    String paymentCode;

    // Type of
    @NotNull
    @EnumValue(enumClass = PaymentMethod.class)
    String paymentMethod;

    // This can be achieved from the data response from 3rd party
    @NotNull
    @Digits(integer = 30, fraction = 2)
    BigDecimal paymentAmount;

    @NotNull
    Long orderId;
}
