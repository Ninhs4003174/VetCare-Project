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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Autowired; // Import the @Autowired annotation

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
    return httpSecurity
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(httpForm -> {
                httpForm.loginPage("/login").permitAll();
                httpForm.defaultSuccessUrl("/userhome", true);
            })
            .authorizeHttpRequests(registry -> {
                // Open access for public resources
                registry.requestMatchers("/signup", "/home", "/about", "/resources", "/css/**", "/img/**").permitAll();
                
                // Define access control for different roles
                registry.requestMatchers("/receptionist/**").hasRole("RECEPTIONIST");
                registry.requestMatchers("/vet/**").hasRole("VET");
                registry.requestMatchers("/client/**").hasRole("CLIENT");
                
                // All other requests need authentication
                registry.anyRequest().authenticated();
            })
            .build();
}
}
