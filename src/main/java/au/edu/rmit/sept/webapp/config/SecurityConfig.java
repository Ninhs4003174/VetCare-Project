package au.edu.rmit.sept.webapp.config;

import au.edu.rmit.sept.webapp.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.Collection;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    @Lazy
    private UserService appUserService;

    @Bean
    public UserDetailsService appDetailsService() {
        return appUserService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(appUserService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> {
                    // Open access for public resources and login page
                    registry.requestMatchers("/vetcaresystemhome", "/signup-client", "/signup-admin", "/home", "/about",
                            "/resources", "/css/**", "/img/**").permitAll();

                    // Role-based access control for creating and deleting pet records
                    registry.requestMatchers("/pets/create", "/pets/delete/**").hasRole("ADMIN");

                    // All other requests need authentication
                    registry.anyRequest().authenticated();
                })
                .formLogin(httpForm -> {
                    httpForm.loginPage("/login-client").permitAll();
                    httpForm.successHandler(customSuccessHandler());
                })
                .exceptionHandling(exceptionHandling -> {
                    exceptionHandling.accessDeniedPage("/403");
                });

        return httpSecurity.build();
    }

    // Custom authentication success handler
    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return (request, response, authentication) -> {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            String redirectUrl = "/vetcaresystemhome"; // Default if no role matched
            for (GrantedAuthority authority : authorities) {
                switch (authority.getAuthority()) {
                    case "CLIENT":
                        redirectUrl = "/userhome";
                        break;
                    case "RECEPTIONIST":
                        redirectUrl = "/clinichome";
                        break;
                    case "VET":
                        redirectUrl = "/vethome";
                        break;
                    case "ADMIN":
                        redirectUrl = "/adminhome";
                        break;
                }
            }

            response.sendRedirect(redirectUrl);
        };
    }
}
