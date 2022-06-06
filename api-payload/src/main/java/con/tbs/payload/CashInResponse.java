package con.tbs.payload;

import lombok.Data;

@Data
public class CashInResponse {


    private String userId;
    private Double cashInAmount;
    private Double balance;

    public CashInResponse(String mobileNumber, Double cashInAmount, Double balance) {
        this.userId = mobileNumber;
        this.cashInAmount = cashInAmount;
        this.balance = balance;
    }

    public CashInResponse() {
    }
}