package con.tbs.payload;

import lombok.Data;

import java.util.ArrayList;
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
