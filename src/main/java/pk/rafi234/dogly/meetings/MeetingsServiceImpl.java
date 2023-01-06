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
        boolean containsGoing = clickedGoing.stream()
                .anyMatch(u -> u.getId().equals(user.getId()));
        boolean containsInterested = clickedInterested.stream()
                .anyMatch(u -> u.getId().equals(user.getId()));

        System.out.println(containsInterested + " " + containsGoing + " " + action);

        User userToDelete = getUserToDelete(clickedGoing, clickedInterested, user);
        clickedGoing.remove(userToDelete);
        clickedInterested.remove(userToDelete);

        clickedGoing.forEach(System.out::println);
        clickedInterested.forEach(System.out::println);

        if (action.equals("going") && (!containsGoing || containsInterested)) {
            clickedGoing.add(user);
        } else if (action.equals("interested") && (!containsInterested || containsGoing)) {
            clickedInterested.add(user);
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

    private User getUserToDelete(Set<User> clickedGoing, Set<User> clickedInterested, User user) {
        List<User> usersToDeleteFromGoing = clickedGoing.stream().filter(u -> u.equals(user)).toList();
        List<User> usersToDeleteFromInterested = clickedInterested.stream().filter(u -> u.equals(user)).toList();
        if (usersToDeleteFromGoing.size() > 0)
            return usersToDeleteFromGoing.get(0);
        if (usersToDeleteFromInterested.size() > 0) {
            return usersToDeleteFromInterested.get(0);
        }
        return null;
    }
}
