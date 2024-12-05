package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dto.APIMessageResponse;
import com.example.dto.UserLoginRequest;
import com.example.model.UserLoginModel;
import com.example.repository.UserLoginRepository;

@Service
public class UserLoginService implements UserDetailsService {

	private UserLoginRepository userLoginRepository;
	
	private JWTService jwtService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginService.class);

	
	
	public UserLoginService(UserLoginRepository userLoginRepository,JWTService jwtService) {
		this.userLoginRepository=userLoginRepository;
		this.jwtService = jwtService;
	}
	
	@Override
	public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
		
		UserLoginModel userModel = userLoginRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userEmail));
        
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new User(
                userModel.getUserEmail(),
                userModel.getUserPassword(),
                authorities);
	}
	
	
	
	public ResponseEntity<APIMessageResponse> loginUser(UserLoginRequest userLoginRequest) {
        try {
            if (userLoginRequest.getUserEmail() == null || userLoginRequest.getUserPassword() == null) {
                return ResponseEntity.badRequest()
                        .body(new APIMessageResponse("Invalid credentials Email or password cannot be null"));
            }

            //fetch user details stored in db
            UserLoginModel storedUserDetails = userLoginRepository.findByUserEmail(userLoginRequest.getUserEmail())
                    .orElseThrow(() -> new Exception("User not found with email: " + userLoginRequest.getUserEmail()));
           
            //compare user shared pwd with the one that is stored in db
            if(userLoginRequest.getUserPassword().contentEquals(storedUserDetails.getUserPassword())) {
                String jwtToken = jwtService.generateToken(storedUserDetails.getUserEmail());
                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);

                return ResponseEntity.ok()
                        .headers(responseHeaders)
                        .body(new APIMessageResponse("Login Successful"));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new APIMessageResponse("Invalid credentials Invalid email or password"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new APIMessageResponse("Login Failed " + e.getMessage()));
        }
    }

	public ResponseEntity<APIMessageResponse> signUpUser(UserLoginRequest userLoginRequest) {
	    try {
	        // Validate input
	        if (userLoginRequest.getUserEmail() == null || userLoginRequest.getUserPassword() == null || userLoginRequest.getUserName() == null) {
	            return ResponseEntity.badRequest().body(new APIMessageResponse("User email or User password or username cannot be null"));
	        }

	        // Check if the user already exists
	        Optional<UserLoginModel> storedUser = userLoginRepository.findByUserEmail(userLoginRequest.getUserEmail());
	        
	        if (storedUser.isPresent()) {
	            return ResponseEntity.badRequest().body(new APIMessageResponse("User with this email already exists"));
	        }

	        // Create new user
	        try {
	            UserLoginModel newUser = new UserLoginModel(userLoginRequest.getUserName(), userLoginRequest.getUserEmail(), userLoginRequest.getUserPassword());
	            userLoginRepository.save(newUser);
	            return ResponseEntity.status(HttpStatus.CREATED).body(new APIMessageResponse("User created successfully!"));
	        } catch (Exception e) {
	            return ResponseEntity.badRequest().body(new APIMessageResponse("Unable to create user: " + e.getMessage()));
	        }

	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new APIMessageResponse("Unable to register user: " + e.getMessage()));
	    }
	}




	
	
}
