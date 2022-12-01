package pk.rafi234.dogly.security.authenticatedUser;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import pk.rafi234.dogly.security.UserDetailsImpl;
import pk.rafi234.dogly.user.CustomUserDetailsService;
import pk.rafi234.dogly.user.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class CustomOnAuthenticationHandler implements AuthenticationSuccessHandler {

    private final CustomUserDetailsService userDetailsService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest req,
            HttpServletResponse res,
            Authentication auth)
            throws IOException, ServletException {
        User user = ((UserDetailsImpl) auth.getPrincipal()).getUser();
        userDetailsService.setStateOfUser(user, true);
    }
}
