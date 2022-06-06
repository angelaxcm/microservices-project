package con.tbs.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LogActivity {
    @NotBlank
    private String action;

    @NotBlank
    private String information;

    @NotBlank
    private String identity;

    public LogActivity(String action, String information, String identity) {
        this.action = action;
        this.information = information;
        this.identity = identity;
    }

    public LogActivity() {
    }
}
