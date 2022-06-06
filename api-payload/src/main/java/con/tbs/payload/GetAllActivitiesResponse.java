package con.tbs.payload;

import lombok.Data;

import java.util.List;

@Data
public class GetAllActivitiesResponse {
    private Long currentActivitiesCount;
    private List<LogActivity> activities;

    public GetAllActivitiesResponse(Long currentActivitiesCount, List<LogActivity> activities) {
        this.currentActivitiesCount = currentActivitiesCount;
        this.activities = activities;
    }

    public GetAllActivitiesResponse() {
    }
}
