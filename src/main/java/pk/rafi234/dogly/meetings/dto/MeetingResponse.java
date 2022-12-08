package pk.rafi234.dogly.meetings.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pk.rafi234.dogly.meetings.Meeting;
import pk.rafi234.dogly.meetings.scrapler.DogPark;
import pk.rafi234.dogly.user.dto.UserResponse;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public final class MeetingResponse {

    private String id;
    private UserResponse user;
    private String title;
    private String description;
    private LocalDateTime date;
    private LocalDateTime addedAt;
    private DogPark dogPark;
    private int going;
    private int interested;

    public MeetingResponse(Meeting meeting) {
        this.id = meeting.getId().toString();
        this.user = new UserResponse(meeting.getUser());
        this.title = meeting.getTitle();
        this.description = meeting.getDescription();
        this.date = meeting.getDate();
        this.going = meeting.getGoing();
        this.interested = meeting.getInterested();
        this.addedAt = meeting.getAddedAt();
        this.dogPark = meeting.getDogPark();
    }
}
