package es.us.isa.odin.server.socialsecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import es.us.isa.odin.server.security.UserAccount;
import es.us.isa.odin.server.security.UserAccountRepository;
 
public class RepositoryUserDetailsService implements UserDetailsService {
 
    private UserAccountRepository repository;
 
    @Autowired
    public RepositoryUserDetailsService(UserAccountRepository repository) {
        this.repository = repository;
    }
 
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	UserAccount user = repository.findByEmail(username);
 
        if (user == null) {
            throw new UsernameNotFoundException("No user found with username: " + username);
        }
 
        ExampleUserDetails principal = ExampleUserDetails.getBuilder()
                .firstName(user.getFirstName())
                .id(user.getId())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .role(user.getRole())
                .socialSignInProvider(user.getSignInProvider())
                .username(user.getEmail())
                .build();
 
        return principal;
    }
}