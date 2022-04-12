package com.project3.revtech.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Optional;

import javax.mail.MessagingException;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.project3.revtech.dao.ResetPasswordEntityRepository;
import com.project3.revtech.dao.UserRepository;
import com.project3.revtech.entity.ResetPasswordEntity;
import com.project3.revtech.entity.UserEntity;
import com.project3.revtech.exception.ResetPasswordException;
import com.project3.revtech.pojo.UserPojo;


@ContextConfiguration(classes = { UserResetPasswordServiceImpl.class })
@ExtendWith(SpringExtension.class)

public class ResetPasswordServicesImplTest {
@MockBean
ResetPasswordEntityRepository resetPasswordEntityRepository;
@MockBean
UserRepository userRepository;
@Autowired
UserResetPasswordServiceImpl userResetPasswordServiceImpl;
@MockBean
JavaMailSender mailSender;

@MockBean
PasswordEncoder encoder;

@Test
void updateResetPasswordToken() throws ResetPasswordException {
UserEntity userEntity = new UserEntity();

userEntity.setAddress("42 Main St");
userEntity.setContact("Contact");
userEntity.setEmail("jane.doe@example.org");
userEntity.setFirstName("Jane");
userEntity.setLastName("Doe");
userEntity.setPassword("iloveyou");
userEntity.setRoles(new HashSet<>());
userEntity.setUserId(1);
userEntity.setUsername("janedoe");
ResetPasswordEntity resetPasswordEntity = new ResetPasswordEntity();
when(this.userRepository.findByEmail("jane.doe@example.org")).thenReturn(userEntity);
userResetPasswordServiceImpl.updateResetPasswordToken(null, "jane.doe@example.org");
verify(this.resetPasswordEntityRepository).findByUserEmail("jane.doe@example.org");
}

@Test
void updatePasswordTest() {
UserEntity userEntity = new UserEntity();

userEntity.setAddress("42 Main St");
userEntity.setContact("Contact");
userEntity.setEmail("jane.doe@example.org");
userEntity.setFirstName("Jane");
userEntity.setLastName("Doe");
userEntity.setPassword("iloveyou");
userEntity.setRoles(new HashSet<>());
userEntity.setUserId(1);
userEntity.setUsername("janedoe");
ResetPasswordEntity resetPasswordEntity = new ResetPasswordEntity();
resetPasswordEntity.setResetId(1);
resetPasswordEntity.setResetPasswordToken("hi");
resetPasswordEntity.setUser(userEntity);

when(this.userRepository.findById(1)).thenReturn(Optional.of(userEntity));
when(this.resetPasswordEntityRepository.findByUserEmail(any())).thenReturn(resetPasswordEntity);
userResetPasswordServiceImpl.updatePassword(1, "jane.doe@example.org");
verify(this.userRepository).save(userEntity);
}

}
