package com.project3.revtech.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.project3.revtech.dao.WishItemRepository;
import com.project3.revtech.dao.WishListRepository;
import com.project3.revtech.entity.ProductEntity;
import com.project3.revtech.entity.UserEntity;
import com.project3.revtech.entity.WishListEntity;
import com.project3.revtech.entity.WishListItemEntity;
import com.project3.revtech.exception.ApplicationException;
import com.project3.revtech.joinedpojo.SentEmailsPojo;
import com.project3.revtech.pojo.BundlePojo;
import com.project3.revtech.pojo.DiscountPojo;
import com.project3.revtech.pojo.ProductPojo;


@ContextConfiguration(classes = {EmailDiscountServiceImpl.class,
		EmailService.class,
		JavaMailSenderImpl.class,
		SimpleMailMessage.class,
		SentEmailsPojo.class,
		ProductPojo.class})
@ExtendWith(SpringExtension.class )
@ComponentScan
public class EmailDiscountTest {
	
	
	@Autowired
	EmailDiscountServiceImpl emailDiscountTest;
	@MockBean
	EmailService eService;
	@MockBean 
	WishItemRepository wishItemRepository;
	@MockBean
	WishListRepository wishListRepository;
	//@MockBean
	//SentEmailsPojo sent;
	@MockBean
	ProductPojo item1;


//	@RegisterExtension
//	static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP);
	
	
	@Test
	void testEmailSendByDiscount() throws ApplicationException 
	{
		DiscountPojo discount = new DiscountPojo(1,1,"high",new BigDecimal(.5));
		// need to create mocked return value for test to work
		ArrayList<WishListItemEntity> wishlist = new ArrayList<WishListItemEntity>();
		WishListItemEntity item = new WishListItemEntity();
		item.setProductEntity(new ProductEntity());
		item.setWishItemId(1);
		wishlist.add(item);
		
		
		when(wishItemRepository.findAllByProductId(1)).thenReturn(wishlist);
		//current has cached emails crashing and catching, add donothing here for working emails
		//doNothing().when(eService).sendMessage("demoreceiveracct1@gmail.com", "Discount", messageText);

   /*     List<SentEmailsPojo> sentEmails = new ArrayList<SentEmailsPojo>();
        SentEmailsPojo sent = new SentEmailsPojo(1,1,1,"demoreceiveracct1@gmail.com",new BigDecimal(.5),"Hello");
        sentEmails.add(sent);*/

        List<SentEmailsPojo> actualResult =  emailDiscountTest.sendByDiscount(discount);

        assertEquals(1, actualResult.size());

	} 
	


	@Test
	void testEmailSendByBundles() throws ApplicationException 
	{
	//	item1.setProductId(1);
		BundlePojo bundle = new BundlePojo(1,"name",new BigDecimal(.5)
				,new ProductPojo(1," ","name",new BigDecimal(22),"catagory","discription",2,"imageUrl",false)
				,new ProductPojo(2," ","name",new BigDecimal(22),"catagory","discription",2,"imageUrl",false));
//				new ProductPojo(2," ","name",new BigDecimal(22),"catagory","discription",2,"imageUrl",false));


		ArrayList<WishListItemEntity> wishlist = new ArrayList<WishListItemEntity>();
		WishListItemEntity item = new WishListItemEntity();
		item.setWishItemId(1);
		wishlist.add(item);
		
		
		when(wishItemRepository.findAllByProductId(1)).thenReturn(wishlist);
		when(wishItemRepository.findAllByProductId(2)).thenReturn(wishlist);
		
		//current has cached emails crashing and catching, add donothing here for working emails
		//doNothing().when(eService).sendMessage("demoreceiveracct1@gmail.com", "Discount", messageText);
		
		// necessary if you wnat to comapare actual values, only size for now.
        List<SentEmailsPojo> sentEmails = new ArrayList<SentEmailsPojo>();
        SentEmailsPojo sent = new SentEmailsPojo(1,1,1,"demoreceiveracct1@gmail.com",new BigDecimal(.5),"Hello");
        sentEmails.add(sent);
        sentEmails.add(sent);

        List<SentEmailsPojo> actualResult =  emailDiscountTest.sendByBundle(bundle);

        assertEquals(sentEmails.size(), actualResult.size());

	}
	
	
	
}
