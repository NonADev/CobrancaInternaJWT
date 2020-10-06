package br.com.sevencomm.cobranca.application.configs.security;

import br.com.sevencomm.cobranca.data.repositories.UserRepository;
import br.com.sevencomm.cobranca.domain.models.Usuario;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service(value = "userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = userRepository.findByLogin(username);

        if (usuario == null) {
            throw new UsernameNotFoundException("User not found");
        }
        else{
            return usuario;
        }
    }
}
