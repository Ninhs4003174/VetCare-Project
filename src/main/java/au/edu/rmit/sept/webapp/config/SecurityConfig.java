package au.edu.rmit.sept.webapp.config;

import au.edu.rmit.sept.webapp.service.UserService; // Import your custom UserService
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
import org.springframework.beans.factory.annotation.Autowired; // Import the @Autowired annotation
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.Collection;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    @Lazy
    private UserService appUserService; // Ensure the appUserService is autowired correctly

    @Bean
    public UserDetailsService appDetailsService() {
        return appUserService; // Return your custom UserService
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(appUserService);
        provider.setPasswordEncoder(passwordEncoder()); // Ensure password encoder is set
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for simplicity; consider re-enabling in
                                                       // production
                .authorizeHttpRequests(registry -> {
                    // Open access for public resources and role selection page
                    registry.requestMatchers("/vetcaresystemhome", "/vetcaresystemhome/selectrole", "/signup-client",
                            "/signup-admin", "/home", "/about", "/resources", "/css/**", "/img/**").permitAll();

                    // Define access control for different roles after login
                    registry.requestMatchers("/receptionisthome/").hasRole("RECEPTIONIST");
                    registry.requestMatchers("/vethome/").hasRole("VET");
                    registry.requestMatchers("/userhome/").hasRole("CLIENT");

                    // All other requests need authentication
                    registry.anyRequest().authenticated(); // Ensure all other requests require authentication
                })
                .formLogin(httpForm -> {
                    // Define custom login pages for different roles
                    httpForm.loginPage("/login-client").permitAll(); // Allow all users to access the login page
                    httpForm.successHandler(customSuccessHandler()); // Use the custom success handler
                })
                .exceptionHandling(exceptionHandling -> {
                    exceptionHandling.accessDeniedPage("/403"); // Redirect to access denied page for unauthorized
                                                                // access
                });

        return httpSecurity.build(); // Finalize the SecurityFilterChain
    }

    // Custom authentication success handler
    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return (request, response, authentication) -> {
            // Get the user's roles
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            System.out.println("Granted Authorities: " + authorities);

            // Determine the redirect URL based on roles
            String redirectUrl = "/vetcaresystemhome"; // Default if no role matched
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals("CLIENT")) {
                    redirectUrl = "/userhome"; // Redirect to user home for CLIENT role
                    break;
                } else if (authority.getAuthority().equals("RECEPTIONIST")) {
                    redirectUrl = "/receptionisthome"; // Redirect to receptionist home for RECEPTIONIST role
                    break;
                } else if (authority.getAuthority().equals("VET")) {
                    redirectUrl = "/vethome"; // Redirect to vet home for VET role
                    break;
                }
            }

            // Redirect to the determined URL
            response.sendRedirect(redirectUrl);
        };
    }
}
