package com.lcwd.user.service.controllers;

import com.lcwd.user.service.entities.Rating;
import com.lcwd.user.service.entities.User;
import com.lcwd.user.service.services.UserService;
import com.netflix.discovery.converters.Auto;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

//import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
//import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
//import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	RestTemplate restTemplate;

	private Logger logger = LoggerFactory.getLogger(UserController.class);

	// create
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		User user1 = userService.saveUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(user1);
	}

	// all user get
	@GetMapping
	public ResponseEntity<List<User>> getAllUser() {
		List<User> allUser = userService.getAllUser();
		return ResponseEntity.ok(allUser);
	}

//	@GetMapping("/{userId}")
//	public User getUserByIs(@PathVariable String userId) {
//		User user = userService.getUser(userId);
//
//		return user;
//	}

	// calling through rest temple without circuit breaker
	@GetMapping("/get/{userId}")
	public ResponseEntity<User> getSingleUser(@PathVariable String userId) {

		User user = userService.getSingleUser(userId);
		return ResponseEntity.ok(user);
	}
  int retryCount = 1;
	
    //single user get
    @GetMapping("/{userId}")
//    @CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
//    @Retry(name = "ratingHotelService", fallbackMethod = "ratingHotelFallback")
    @RateLimiter(name = "userRateLimiter", fallbackMethod = "ratingHotelFallback")
    public ResponseEntity<User> getUserById(@PathVariable String userId) {
        logger.info("Get Single User Handler: UserController");
        
        logger.info("Retry count: {}", retryCount);
        retryCount ++;
        User user = userService.getUser(userId);
        return ResponseEntity.ok(user);
    }

    //creating fall back  method for circuitbreaker


    public ResponseEntity<User> ratingHotelFallback(String userId, Exception ex) {
    	
        logger.info("Fallback is executed because service is down : ", ex.getMessage());

        ex.printStackTrace();

//        User user = User.builder().email("dummy@gmail.com").name("Dummy").about("This user is created dummy because some service is down").userId("141234").build();
       
        // without using builder
        User user = new User();
        user.setEmail("dummy@gmail.com");
        user.setName("Dummy");
        user.setAbout("This user is created dummy because some service is down");
        user.setUserId("141234");
        
        return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
    }


//    //all user get
//    @GetMapping
//    public String helloo() {
//       
//        return "Hello I am Don";
//    }
}
