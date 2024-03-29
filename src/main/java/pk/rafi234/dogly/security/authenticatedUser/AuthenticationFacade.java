package pk.rafi234.dogly.security.authenticatedUser;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pk.rafi234.dogly.user.User;
import pk.rafi234.dogly.security.UserDetailsImpl;

@Component
public class AuthenticationFacade implements IAuthenticationFacade {

    @Override
    public User getAuthentication() {
        try {
            return ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        } catch (ClassCastException e) {
            throw new RuntimeException("no user is logged in");
        }
    }
}
