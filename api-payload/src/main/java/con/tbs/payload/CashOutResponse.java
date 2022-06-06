package con.tbs.payload;

import lombok.Data;

@Data
public class CashOutResponse {

    private String mobileNumber;
    private Double cashOutAmount;
    private Double balance;

    public CashOutResponse(String mobileNumber, Double cashOutAmount, Double balance) {
        this.mobileNumber = mobileNumber;
        this.balance = balance;
        this.cashOutAmount = cashOutAmount;
    }

    public CashOutResponse() {
    }
}
