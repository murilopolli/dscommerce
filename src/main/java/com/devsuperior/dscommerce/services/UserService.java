package com.devsuperior.dscommerce.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscommerce.dtos.UserDTO;
import com.devsuperior.dscommerce.entities.Role;
import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.projections.UserDetailsProjection;
import com.devsuperior.dscommerce.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<UserDetailsProjection> projections = repository.searchUserAndRolesByEmail(username);
		if(projections.isEmpty()) {
			throw new UsernameNotFoundException("User not found");
		}
		
		User user = new User();
		user .setEmail(username);
		user.setPassword(projections.get(0).getPassword());
		for (UserDetailsProjection p : projections) {
			user.addRole(new Role(p.getRoleId(), p.getAuthority()));
		}
		return user;
	}

	protected User authenticated() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
			String username = jwtPrincipal.getClaim("username");
	
			return repository.findByEmail(username).get();
		} catch(Exception e) {
			throw new UsernameNotFoundException("User not found");
		}
	}
	
	@Transactional(readOnly = true)
	public UserDTO getMe() {
		User user = authenticated();
		return new UserDTO(user);
	}
}
