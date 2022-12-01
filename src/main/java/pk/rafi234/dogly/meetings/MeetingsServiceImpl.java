package pk.rafi234.dogly.meetings;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pk.rafi234.dogly.meetings.dto.MeetingRequest;
import pk.rafi234.dogly.meetings.dto.MeetingResponse;
import pk.rafi234.dogly.security.authenticatedUser.IAuthenticationFacade;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingsServiceImpl implements MeetingsService {

    private final MeetingsRepository meetingsRepository;
    private final IAuthenticationFacade authenticationFacade;


    @Override
    public MeetingResponse addMeeting(MeetingRequest meetingReq) {
        Meeting meeting = new Meeting();
        meeting.setUser(authenticationFacade.getAuthentication());
        meeting.setId(UUID.randomUUID());
        meeting.setDescription(meetingReq.getDescription());
        meeting.setDate(meetingReq.getDate());
        meeting.setAddedAt(LocalDateTime.now());
        meeting.setImageUrl(meetingReq.getImageUrl());
        return new MeetingResponse(meetingsRepository.save(meeting));
    }

    @Override
    public void deleteMeeting(UUID id) {
        meetingsRepository.deleteById(id);
    }

    @Override
    public List<MeetingResponse> getAllMeetings() {
        return meetingsRepository.findAll()
                .stream()
                .map(MeetingResponse::new)
                .collect(Collectors.toList());
    }
}
