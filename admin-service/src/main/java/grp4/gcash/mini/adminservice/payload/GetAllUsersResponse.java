package grp4.gcash.mini.adminservice.payload;

import lombok.Data;

import java.util.List;

@Data
public class GetAllUsersResponse {
    private Long currentUsers;
    private List<UserDetails> users;

    public GetAllUsersResponse(Long currentUsers, List<UserDetails> users) {
        this.currentUsers = currentUsers;
        this.users = users;
    }

    public GetAllUsersResponse() {
    }
}
