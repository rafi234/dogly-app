package pk.rafi234.dogly.meetings.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pk.rafi234.dogly.meetings.scrapler.DogPark;

@Data
@NoArgsConstructor
public final class MeetingRequest {

    private String title;
    private String description;
    private String date;
    private DogPark dogPark;
}
