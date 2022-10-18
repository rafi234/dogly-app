package pk.rafi234.dogly.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import pk.rafi234.dogly.user.UserRepository;

import java.util.UUID;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final UserRepository userRepository;

    @Autowired
    public SecurityConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .and()
                .csrf().disable()
                .cors().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .mvcMatchers("").permitAll()
                .and()
                .logout().permitAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public UserDetailsService detailsService() {
        return username -> new UserDetailsImpl(userRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with name " + username + " not exist!")));

    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider()  {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(detailsService());
        authProvider.setPasswordEncoder(getEncoder());
        return  authProvider;
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder(13);
    }

    @Bean
    public UUID getUuid() {
        return UUID.randomUUID();
    }
}
