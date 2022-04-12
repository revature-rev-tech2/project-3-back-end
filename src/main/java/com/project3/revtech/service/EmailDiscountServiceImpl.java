package com.project3.revtech.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project3.revtech.dao.WishItemRepository;
import com.project3.revtech.dao.WishListRepository;
import com.project3.revtech.entity.WishListEntity;
import com.project3.revtech.entity.WishListItemEntity;
import com.project3.revtech.exception.ApplicationException;
import com.project3.revtech.joinedpojo.SentEmailsPojo;
import com.project3.revtech.pojo.BundlePojo;
import com.project3.revtech.pojo.DiscountPojo;

@Service
@Transactional
public class EmailDiscountServiceImpl {
	
	@Autowired        //will re qutowire later
	EmailService emailService;
	

	@Autowired
	WishItemRepository wishItemRepository;
	
	@Autowired
	WishListRepository wishlistRepository;
	
	@Autowired
	List<SentEmailsPojo> sentEmails;
	
	@Autowired
	SentEmailsPojo sent;
	
	// todo add custom exceptions
	public List<SentEmailsPojo> sendByDiscount(DiscountPojo discount) {
		
		
		List<WishListItemEntity> wishedDiscounts;
		try {
			wishedDiscounts = wishItemRepository.findAllByProductId(discount.getProductId());	
			sentEmails.clear();
			for(WishListItemEntity wishItemEntity : wishedDiscounts)
			{
				// stops sentEmails from having a dooped entry

				// Creates email message
				String messageText = wishItemEntity.getProductEntity().getProductName() + "Just went on Sale \n"
				+ "GET " + discount.getDiscountPercentage().multiply(new BigDecimal(100)) + "% OFF!!!!!";			
				// not sure if necessary, Create return to test
				try {
					// wish-Item -> wish-list-details -> users join call
					emailService.sendMessage(wishItemEntity.getWishListEntity().getUserEntity().getEmail(),"Discount",messageText);
					sent.setProductId(wishItemEntity.getProductEntity().getProductId());
					sent.setDiscountId(discount.getDiscountId());
					sent.setUserId(wishItemEntity.getWishListEntity().getUserEntity().getUserId());
					sent.setEmail(wishItemEntity.getWishListEntity().getUserEntity().getEmail());
					sent.setDiscount(discount.getDiscountPercentage());
					sent.setProductName(wishItemEntity.getProductEntity().getProductName() );
					sentEmails.add(sent);
					
					
				} catch(Exception e) {
					sent.setProductId(1);
					sent.setDiscountId(1);
					sent.setUserId(1);
					sent.setEmail("email");
					sent.setDiscount(new BigDecimal(.5));
					sent.setProductName("Message failed");
					sentEmails.add(sent);
			//		e.printStackTrace();			
				}
			}

		} catch (ApplicationException e1) {
			// TODO Auto-generated catch block
		//	e1.printStackTrace();
		}
		
		
		return sentEmails;
	}
	
	
	
	// Sends discounts for bundles, dubplicate emails if both items are wishlisted
	public List<SentEmailsPojo> sendByBundle(BundlePojo bundle) {

		List<WishListItemEntity> wishedDiscounts;
		try {
			// Copy Paste code do to hard coded limit for bundles
			// stops sentEmails from having a dooped entry
			sentEmails.clear();
			HashSet<Integer> sentUsers = new HashSet<Integer>();
			wishedDiscounts = wishItemRepository.findAllByProductId(bundle.getProductOnePojo().getProductId());		
			for(WishListItemEntity wishItemEntity : wishedDiscounts)
			{
				
				String messageText = bundle.getProductOnePojo().getProductName() + "Just went on Sale in the " + bundle.getBundleName() + " with the " + bundle.getProductTwoPojo().getProductName() + " \n"
				+ "GET " + bundle.getBundlePercentage().multiply(new BigDecimal(100)) + "% OFF THE BUNDLE!!!!!!!!!";			
				// not sure if necessary
				try {
					// wish-Item -> wish-list-details -> users join call
					emailService.sendMessage(wishItemEntity.getWishListEntity().getUserEntity().getEmail(),"Discount",messageText);
					//stops duplicate emails
					sentUsers.add(wishItemEntity.getWishListEntity().getUserEntity().getUserId());
					sent.setProductId(wishItemEntity.getProductEntity().getProductId());
					sent.setDiscountId(bundle.getBundleId());
					sent.setUserId(wishItemEntity.getWishListEntity().getUserEntity().getUserId());
					sent.setEmail(wishItemEntity.getWishListEntity().getUserEntity().getEmail());
					sent.setDiscount(bundle.getBundlePercentage());
					sent.setProductName(wishItemEntity.getProductEntity().getProductName());
					sentEmails.add(sent);
					
					
				} catch(Exception e) {
					sent.setProductId(1);
					sent.setDiscountId(1);
					sent.setUserId(1);
					sent.setEmail("email");
					sent.setDiscount(new BigDecimal(.5));
					sent.setProductName("Message failed");
					sentEmails.add(sent);
			//		e.printStackTrace();			
				}
			}
			
			// Copy Paste code do to hard coded limit for bundles
			wishedDiscounts = wishItemRepository.findAllByProductId(bundle.getProductTwoPojo().getProductId());		
			for(WishListItemEntity wishItemEntity : wishedDiscounts)
			{
				if(!sentUsers.contains(wishItemEntity.getWishListEntity().getUserEntity().getUserId()))
				{
					String messageText = bundle.getProductTwoPojo().getProductName() + "Just went on Sale in the " + bundle.getBundleName() + " with the " + bundle.getProductOnePojo().getProductName() + " \n"
					+ "GET " + bundle.getBundlePercentage().multiply(new BigDecimal(100)) + "% OFF!!!!!";			
					// not sure if necessary
					try {
						// wish-Item -> wish-list-details -> users join call
						emailService.sendMessage(wishItemEntity.getWishListEntity().getUserEntity().getEmail(),"Discount",messageText);
						sent.setProductId(wishItemEntity.getProductEntity().getProductId());
						sent.setDiscountId(bundle.getBundleId());
						sent.setUserId(wishItemEntity.getWishListEntity().getUserEntity().getUserId());
						sent.setEmail(wishItemEntity.getWishListEntity().getUserEntity().getEmail());
						sent.setDiscount(bundle.getBundlePercentage());
						sent.setProductName(wishItemEntity.getProductEntity().getProductName());
						sentEmails.add(sent);
						
						
					} catch(Exception e) {
						sent.setProductId(1);
						sent.setDiscountId(1);
						sent.setUserId(1);
						sent.setEmail("email");
						sent.setDiscount(new BigDecimal(.5));
						sent.setProductName("Message failed");
						sentEmails.add(sent);
				//		e.printStackTrace();			
					}
				}
			}
			
		} catch (ApplicationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		return sentEmails;
		
		
		
	}

}
