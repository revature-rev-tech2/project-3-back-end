package com.project3.revtech.controller;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project3.revtech.config.StorageConfig;
import com.project3.revtech.entity.UserEntity;
import com.project3.revtech.exception.ResetPasswordException;
import com.project3.revtech.pojo.ResetPasswordPojo;
import com.project3.revtech.pojo.UserPojo;
import com.project3.revtech.service.UserResetPasswordService;

import net.bytebuddy.utility.RandomString;

@RestController
@CrossOrigin
@RequestMapping("api/reset_password/")
public class ResetPasswordController {
	@Autowired
	UserResetPasswordService resetPasswordService;
	
	@Autowired
	StorageConfig storageConfig;

	// GetMapping For forget_password
	
	/*
	 * 
	 * { "email":"rana@gmail.com" }
	 */
	// 1- http://localhost:7777/api/reset_password/forgot_password
	@PostMapping("forgot_password")
	ResponseEntity<?> processForgotPassword(@RequestBody ResetPasswordPojo resetObject) {
		String token = RandomString.make(30);
		try {
			resetPasswordService.updateResetPasswordToken(token, resetObject.getEmail());
			// When a user click on the link should set a page that will call the link (2)
			// Update the link with collegues path
			String link = storageConfig.getBucketUrl() + "reset-password?token=" + token;
			// Set resetObject.getEmail() instead of "RevEmail123456@gmail.com"
			resetPasswordService.sendEmail(resetObject.getEmail(), link);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<>(e.getMessage(), HttpStatus.METHOD_FAILURE);
		}

		return new ResponseEntity<>( HttpStatus.OK);

	}

	// 2- Redirect that token to this LINK
	// http://localhost:7777/api/reset_password/reset_password
	// check if the token is ok or no
	@GetMapping("reset_password")
	public ResponseEntity<?> showResetPassword(@RequestParam(value = "token") String token) {
		UserPojo user = resetPasswordService.getByResetPasswordToken(token);

		if (user == null) {
			return new ResponseEntity<>(null, HttpStatus.OK);

		}
		// send a user pojo that has id and email of the user that we should update the
		// password
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	// Send a user pojo to this link that contains userId and password
	// http://localhost:7777/api/reset_password/updatePassword
	/*
	 * {user_id: 1, password:'123456789'}
	 * 
	 */
	@PostMapping("updatePassword")
	public ResponseEntity<?> updatePassword(@RequestBody UserPojo userpojo) {

		UserEntity user = resetPasswordService.updatePassword(userpojo.getUser_id(), userpojo.getPassword());

		if (user == null) {
			return new ResponseEntity<>("Failed to reset Password", HttpStatus.METHOD_FAILURE);

		}

		return new ResponseEntity<>(HttpStatus.OK);
	}
}
