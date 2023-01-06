package pk.rafi234.dogly.meetings;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pk.rafi234.dogly.meetings.dto.MeetingRequest;
import pk.rafi234.dogly.meetings.dto.MeetingResponse;
import pk.rafi234.dogly.security.annotation.IsUser;
import pk.rafi234.dogly.security.annotation.IsUserLogged;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meetings")
public class MeetingsController {

    private final MeetingsService meetingsService;

    @PostMapping()
    @IsUser
    public ResponseEntity<MeetingResponse> createMeetings(@RequestBody MeetingRequest meetingRequest) {
        return ResponseEntity.ok(meetingsService.addMeeting(meetingRequest));
    }

    @DeleteMapping("/{id}")
    @IsUserLogged
    public ResponseEntity<?> deleteMeeting(@PathVariable String id) {
        meetingsService.deleteMeeting(UUID.fromString(id));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/action")
    @IsUser
    public ResponseEntity<?> actionMeeting(
            @PathVariable String id,
            @RequestParam String action) {
        meetingsService.action(UUID.fromString(id), action);
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity<List<MeetingResponse>> getAllMeetings() {
        return ResponseEntity.ok(meetingsService.getAllMeetings());
    }
}
