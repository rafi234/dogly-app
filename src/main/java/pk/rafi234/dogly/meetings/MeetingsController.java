package pk.rafi234.dogly.meetings;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pk.rafi234.dogly.meetings.dto.MeetingRequest;
import pk.rafi234.dogly.meetings.dto.MeetingResponse;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meetings")
public class MeetingsController {

    private final MeetingsService meetingsService;

    @PostMapping()
    public ResponseEntity<MeetingResponse> getMeetings(MeetingRequest meetingRequest) { //TODO should return ResponseEntity
        return ResponseEntity.ok(meetingsService.addMeeting(meetingRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMeeting(@PathVariable String id) {
        meetingsService.deleteMeeting(UUID.fromString(id));
        return ResponseEntity.ok(null);
    }
}
