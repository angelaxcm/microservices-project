package con.tbs.payload;

import lombok.Data;

@Data
public class MoneyTransferResponse {
    String senderId;
    String receiverId;

    Double amount;

    String message;

    public MoneyTransferResponse(String senderId, String receiverId, Double amount, String message) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.message = message;
    }

    public MoneyTransferResponse() {
    }
}
