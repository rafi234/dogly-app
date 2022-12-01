package pk.rafi234.dogly.meetings.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Lazy;
import pk.rafi234.dogly.meetings.Meeting;
import pk.rafi234.dogly.user.User;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
public class MeetingResponse {

    private User user;
    private String description;
    private LocalDateTime date;
    private LocalDateTime addedAt;

    public MeetingResponse(Meeting meeting) {
        this.user = meeting.getUser();
        this.description = meeting.getDescription();
        this.date = meeting.getDate();
        this.addedAt = meeting.getAddedAt();
    }
}
