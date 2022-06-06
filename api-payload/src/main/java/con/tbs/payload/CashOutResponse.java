package con.tbs.payload;

import lombok.Data;

@Data
public class CashOutResponse {

    private String userId;
    private Double cashOutAmount;
    private Double balance;

    public CashOutResponse(String userId, Double cashOutAmount, Double balance) {
        this.userId = userId;
        this.balance = balance;
        this.cashOutAmount = cashOutAmount;
    }

    public CashOutResponse() {
    }
}
