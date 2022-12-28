package pk.rafi234.dogly.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import pk.rafi234.dogly.security.UserDetailsImpl;
import pk.rafi234.dogly.security.filter.CustomAuthorizationFilter;
import pk.rafi234.dogly.user.CustomUserDetailsService;
import pk.rafi234.dogly.user.UserRepository;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {
    private final UserRepository userRepository;
    private final CustomUserDetailsService userDetailsService;
    private final CustomAuthorizationFilter authorizationFilter;



    @Autowired
    public WebSecurityConfiguration(
            UserRepository userRepository,
            @Lazy CustomUserDetailsService userDetailsService,
            @Lazy CustomAuthorizationFilter authorizationFilter) throws Exception {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.authorizationFilter = authorizationFilter;
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable();
//        http.authorizeRequests().antMatchers("/api/authenticate").permitAll()
//                .antMatchers(HttpHeaders.ALLOW).permitAll()
//                .anyRequest().authenticated();
        http.authorizeRequests().anyRequest().permitAll();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.addFilterBefore(
                authorizationFilter,
                UsernamePasswordAuthenticationFilter.class
        );
//        http
//                .httpBasic()
//                .and()
//                .cors().disable()
//                .csrf().disable()
//                .headers().frameOptions().disable()
//                .and()
//                .authorizeRequests()
//                .anyRequest().permitAll()
//                .and()
//                .formLogin()
//                .successHandler(authenticationSuccessHandler())
//                .defaultSuccessUrl("/api/user", true)
//                .and()
//                .logout()
//                .deleteCookies("JSESSIONID")
//                .addLogoutHandler(securityContextLogoutHandler())
//                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
//                .permitAll()
//                .and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public UserDetailsService detailsService() {
        return username -> new UserDetailsImpl(userRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with name " + username + " not exist!")));

    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(detailsService());
        authProvider.setPasswordEncoder(getEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(detailsService())
                .passwordEncoder(getEncoder())
                .and().build();
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder(13);
    }
}
