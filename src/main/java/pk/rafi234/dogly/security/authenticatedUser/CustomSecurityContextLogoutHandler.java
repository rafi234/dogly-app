package pk.rafi234.dogly.security.authenticatedUser;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import pk.rafi234.dogly.security.UserDetailsImpl;
import pk.rafi234.dogly.user.CustomUserDetailsService;
import pk.rafi234.dogly.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class CustomSecurityContextLogoutHandler extends SecurityContextLogoutHandler {

    private final CustomUserDetailsService userDetailsService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
//        User user = ((UserDetailsImpl) auth.getPrincipal()).getUser();
//        userDetailsService.setStateOfUser(user, false);
        super.logout(request, response, auth);
    }

    @Override
    public void setClearAuthentication(boolean clearAuthentication) {
        User user = ((UserDetailsImpl)(SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getUser();
        userDetailsService.setStateOfUser(user, false);
        super.setClearAuthentication(clearAuthentication);
    }
}
