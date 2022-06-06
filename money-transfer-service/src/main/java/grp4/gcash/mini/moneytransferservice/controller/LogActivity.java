package grp4.gcash.mini.moneytransferservice.controller;

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

    public LogActivity(String action, String data, String identity) {
        this.action = action;
        this.information = data;
        this.identity = identity;
    }

    public LogActivity() {
    }
}
