package con.tbs.payload;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class UpdateWalletRequest {
    @NotBlank
    private String userId;
    @PositiveOrZero
    private double balance;

    public UpdateWalletRequest(String userId, double balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public UpdateWalletRequest() {
    }
}
