package com.devsuperior.dscommerce.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

	
}
