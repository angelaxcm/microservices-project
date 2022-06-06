package con.tbs.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class MoneyTransferRequest {
    @NotBlank
    String senderId;
    @NotBlank
    String receiverId;
    @NotNull
    @Positive
    Double amount;

    String message;

    public MoneyTransferRequest() {
    }
}
