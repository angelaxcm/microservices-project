package con.tbs.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
public class CashInRequest {

    @NotBlank
    @Size(min=10, max=11)
    @Positive
    private String mobileNumber;

    @NotNull
    private Double cashInAmount;

    public CashInRequest() {
    }
}

