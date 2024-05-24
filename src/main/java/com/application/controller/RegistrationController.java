package com.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.application.model.Professor;
import com.application.model.User;
import com.application.services.ProfessorService;
import com.application.services.UserService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class RegistrationController 
{
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProfessorService professorService;

	private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

	public static boolean isValidEmail(String email) {
		Pattern pattern = Pattern.compile(EMAIL_REGEX);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	
	@PostMapping("/registeruser")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> registerUser(@RequestBody User user) throws Exception {
		String currEmail = user.getEmail();
		String newID = getNewID();
		user.setUserid(newID);


		if (isValidEmail(user.getEmail()) == false) {
			return new ResponseEntity<>("enter valid userId", HttpStatus.BAD_REQUEST);
		}
			if (currEmail != null || !"".equals(currEmail)) {
				User userObj = userService.fetchUserByEmail(currEmail);
				if (userObj != null) {
					throw new Exception("User with " + currEmail + " already exists !!!");
				}
			}
			User userObj = null;
			userObj = userService.saveUser(user);
		return new ResponseEntity<>(userObj, HttpStatus.CREATED);
		}

	
	@PostMapping("/registerprofessor")
	@CrossOrigin(origins = "http://localhost:4200")
	public Professor registerDoctor(@RequestBody Professor professor) throws Exception
	{
		String currEmail = professor.getEmail();
		String newID = getNewID();
		professor.setProfessorid(newID);
		
		if(currEmail != null || !"".equals(currEmail))
		{
			Professor professorObj = professorService.fetchProfessorByEmail(currEmail);
			if(professorObj != null)
			{
				throw new Exception("Professor with "+currEmail+" already exists !!!");
			}
		}
		Professor professorObj = null;
		professorObj = professorService.saveProfessor(professor);
		return professorObj;
	}
	
	public String getNewID()
	{
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"+"0123456789"+"abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++) 
        {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
	}
}