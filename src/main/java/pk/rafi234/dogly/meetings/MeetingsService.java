package pk.rafi234.dogly.meetings;

import pk.rafi234.dogly.meetings.dto.MeetingRequest;
import pk.rafi234.dogly.meetings.dto.MeetingResponse;
import pk.rafi234.dogly.user.dto.UserResponse;

import java.util.List;
import java.util.UUID;

public interface MeetingsService {
    MeetingResponse addMeeting(MeetingRequest meeting);
    void deleteMeeting(UUID id);
    List<MeetingResponse> getAllMeetings();
    MeetingResponse action(UUID id, String action);
}


