package pk.rafi234.dogly.meetings;

import pk.rafi234.dogly.meetings.dto.MeetingRequest;
import pk.rafi234.dogly.meetings.dto.MeetingResponse;

import java.util.List;
import java.util.UUID;

public interface MeetingsService {
    MeetingResponse addMeeting(MeetingRequest meeting);
    void deleteMeeting(UUID id);
    List<MeetingResponse> getMeetings(String page);
    MeetingResponse action(UUID id, String action);

    MeetingResponse updateMeeting(MeetingRequest meetingRequest);
}


