package pk.rafi234.dogly.meetings.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MeetingRequest {

    private String imageUrl;
    private String description;
    private LocalDateTime date;
}
