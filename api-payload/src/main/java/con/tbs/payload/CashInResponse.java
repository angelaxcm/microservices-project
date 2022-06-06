package con.tbs.payload;

import lombok.Data;

@Data
public class CashInResponse {


    private String mobileNumber;
    private Double cashInAmount;
    private Double balance;

    public CashInResponse(String mobileNumber, Double cashInAmount, Double balance) {
        this.mobileNumber = mobileNumber;
        this.cashInAmount = cashInAmount;
        this.balance = balance;
    }

    public CashInResponse() {
    }
}