package com.project3.revtech.service;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project3.revtech.dao.ResetPasswordEntityRepository;
import com.project3.revtech.dao.UserRepository;
import com.project3.revtech.entity.ResetPasswordEntity;
import com.project3.revtech.entity.UserEntity;
import com.project3.revtech.exception.ResetPasswordException;
import com.project3.revtech.pojo.UserPojo;

@Service
@Transactional
public class UserResetPasswordServiceImpl implements UserResetPasswordService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	ResetPasswordEntityRepository resetRepository;
	// The password encoder that is used in the UserServiceImpl
	@Autowired
	PasswordEncoder encoder;

	
	
	
	@Autowired
	JavaMailSender mailSender;
	@Override
	public void updateResetPasswordToken(String token, String email) throws ResetPasswordException {
		// TODO Auto-generated method stub

		UserEntity user = userRepository.findByEmail(email);
		// the user exists
		if (user != null) {
			ResetPasswordEntity resetPasswordEntity = new ResetPasswordEntity();
			//if we have a reset token for that email
			if (resetRepository.findByUserEmail(user.getEmail()) != null) {
				resetPasswordEntity = resetRepository.findByUserEmail(user.getEmail());
				//set the new token instead of the old one
				resetPasswordEntity.setResetPasswordToken(token);
				resetRepository.save(resetPasswordEntity);
			} else {
				resetPasswordEntity.setUser(user);
				resetPasswordEntity.setResetPasswordToken(token);
				resetRepository.save(resetPasswordEntity);
			}
			
		} else
			throw new ResetPasswordException("User not found");

	}

	@Override
	public UserEntity updatePassword(Integer userId, String newPassword) {

		String encodedPassword = encoder.encode(newPassword);
		UserEntity user = userRepository.findById(userId).get();
		user.setPassword(encodedPassword);
		
		ResetPasswordEntity resetPasswordEntity = resetRepository.findByUserEmail(user.getEmail());
		resetPasswordEntity.setResetPasswordToken(null);
		resetRepository.save(resetPasswordEntity);
		return userRepository.save(user);
		
	}

	@Override
	public UserPojo getByResetPasswordToken(String token) {
		// TODO Auto-generated method stub
		ResetPasswordEntity resetPasswordEntity = resetRepository.findByResetPasswordToken(token);
		if (resetPasswordEntity != null ) {
			UserEntity userEntity = resetPasswordEntity.getUser();
			UserPojo userPojo = new UserPojo();
			userPojo.setUser_id(userEntity.getUserId());
			userPojo.setEmail(userEntity.getEmail());
			userPojo.setUsername(userEntity.getUsername());
			return userPojo;
		}
		return null;
	}

	@Override
	public void sendEmail(String recipientEmail, String link) throws UnsupportedEncodingException, MessagingException {
		// TODO Auto-generated method stub
		
		
		  MimeMessage message = mailSender.createMimeMessage();              
		    MimeMessageHelper helper = new MimeMessageHelper(message);
		     
		    helper.setFrom("revtech@support.com", "RevTech Support");
		    helper.setTo(recipientEmail);
		     
		    String subject = "Here's the link to reset your password";
		     
		    String content = "<p>Hello,</p>"
		            + "<p>You have requested to reset your password.</p>"
		            + "<p>Click the link below to change your password:</p>"
		            + "<p><a href=\"" + link + "\">Change my password</a></p>"
		            + "<br>"
		            + "<p>Ignore this email if you do remember your password, "
		            + "or you have not made the request.</p>";
		     
		    helper.setSubject(subject);
		     
		    helper.setText(content, true);
		     
		    mailSender.send(message);

	}

}
