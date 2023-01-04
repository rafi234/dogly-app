package pk.rafi234.dogly.meetings.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pk.rafi234.dogly.meetings.Meeting;
import pk.rafi234.dogly.meetings.scrapler.DogPark;
import pk.rafi234.dogly.user.User;
import pk.rafi234.dogly.user.dto.UserResponse;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

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

    private Set<UserResponse> goingClicked;
    private Set<UserResponse> interestedClicked;

    public MeetingResponse(Meeting meeting) {
        this.id = meeting.getId().toString();
        this.user = new UserResponse(meeting.getUser());
        this.title = meeting.getTitle();
        this.description = meeting.getDescription();
        this.date = meeting.getDate();
        this.addedAt = meeting.getAddedAt();
        this.dogPark = meeting.getDogPark();
        this.goingClicked = getUserResponseSet(meeting.getGoingUsers());
        this.interestedClicked = getUserResponseSet(meeting.getInterestedUsers());
    }

    private Set<UserResponse> getUserResponseSet(Set<User> users) {
        return users.stream()
                .map(UserResponse::new)
                .collect(Collectors.toSet());
    }
}
