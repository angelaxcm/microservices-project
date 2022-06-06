package con.tbs.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class GetWalletRequest {
    @NotBlank
    String userId;

    public GetWalletRequest() {
    }
}

