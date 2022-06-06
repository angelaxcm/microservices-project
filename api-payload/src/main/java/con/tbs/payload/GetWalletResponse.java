package con.tbs.payload;

import lombok.Data;

@Data
public class GetWalletResponse {
    String userId;
    Double balance;

    public GetWalletResponse(String userId, Double balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public GetWalletResponse() {
    }
}

