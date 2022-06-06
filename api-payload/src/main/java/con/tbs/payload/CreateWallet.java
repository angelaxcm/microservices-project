package con.tbs.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateWallet {
    @NotBlank
    private String userId;

    public CreateWallet() {
    }
}
