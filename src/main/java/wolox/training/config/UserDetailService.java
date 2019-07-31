package wolox.training.config;
import java.util.Optional;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.models.User;
import wolox.training.repositories.UserRepository;

@Component
public class UserDetailService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository us;

    public UserDetails loadUserByUsername(final String username){
        User user;
        user = us.findByUsername(username).get();
        final Collection<? extends GrantedAuthority> authorities = Arrays
            .asList(new SimpleGrantedAuthority("ROLE_ USER"));
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(),
            authorities);
    }
}
