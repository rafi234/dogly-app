package pk.rafi234.dogly.meetings;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pk.rafi234.dogly.meetings.dto.MeetingRequest;
import pk.rafi234.dogly.meetings.dto.MeetingResponse;
import pk.rafi234.dogly.security.authenticatedUser.IAuthenticationFacade;
import pk.rafi234.dogly.user.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MeetingsServiceImpl implements MeetingsService {

    private final MeetingsRepository meetingsRepository;
    private final IAuthenticationFacade authenticationFacade;


    @Override
    public MeetingResponse addMeeting(MeetingRequest meetingReq) {
        Meeting meeting = new Meeting();
        LocalDateTime time = LocalDateTime.parse(meetingReq.getDate());
        meeting.setUser(authenticationFacade.getAuthentication());
        meeting.setTitle(meetingReq.getTitle());
        meeting.setId(UUID.randomUUID());
        meeting.setDescription(meetingReq.getDescription());
        meeting.setDate(time);
        meeting.setAddedAt(LocalDateTime.now());
        meeting.setDogPark(meetingReq.getDogPark());
        System.out.println("Hello!!!");
        return new MeetingResponse(meetingsRepository.save(meeting));
    }

    @Override
    public void deleteMeeting(UUID id) {
        meetingsRepository.deleteById(id);
    }

    @Override
    public List<MeetingResponse> getAllMeetings() {
        return meetingsRepository.findAllOrderByAddedAt()
                .stream()
                .map(MeetingResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public MeetingResponse action(UUID id, String action) {
        Meeting meeting = meetingsRepository.findById(id).orElseThrow();
        User user = authenticationFacade.getAuthentication();
        Set<User> clickedInterested = meeting.getInterestedUsers();
        Set<User> clickedGoing = meeting.getGoingUsers();
        if (action.equals("going")) {
            clickedInterested.remove(user);
            clickedGoing.add(user);
        } else if (action.equals("interested")) {
            clickedInterested.add(user);
            clickedGoing.remove(user);
        }
        return new MeetingResponse(meetingsRepository.save(meeting));
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.DAYS)
    public void checkIfMeetingsNeedUpdate() {
        meetingsRepository.findAllOrderByAddedAt().forEach(this::deleteExpiredMeeting);
    }

    private void deleteExpiredMeeting(Meeting meeting) {
        if (isMeetingExpired(meeting)) {
            deleteMeeting(meeting.getId());
        }
    }

    private boolean isMeetingExpired(Meeting meeting) {
        return meeting.getDate().plusHours(5L).isBefore(LocalDateTime.now());
    }
}
