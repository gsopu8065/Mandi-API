package com.wku.mandi.dao;


import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.wku.mandi.SpringBoot;
import com.wku.mandi.dao.impl.UserDaoImpl;
import com.wku.mandi.db.Address;
import com.wku.mandi.db.Inventory;
import com.wku.mandi.db.User;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBoot.class)
@WebAppConfiguration
public class TestUserDaoImpl {

	@Autowired
	private UserDaoImpl userDaoImpl;

	private User fakeUser = new User();
	private Address homeAddress = new Address();
	
	private static final String JOHN_DOE = "JohnDoe";
	
	@Before
	public void setUp() {
		fakeUser.setFirstName("John");
		fakeUser.setLastName("Doe");
		fakeUser.setSex("M");
		fakeUser.setUserId(JOHN_DOE);
		
		injectHomeAddress();
		injectInventory();
		
		userDaoImpl.saveUser(fakeUser);
	}
	
	@After
	public void tearDown() {
		//userDaoImpl.deleteUser(JOHN_DOE);
	}
	
	@Test
	public void testSaveUserandRetrieve() {
		User actualuser = userDaoImpl.findUserById(JOHN_DOE);
		
		compareUserData(actualuser);
		
		Address actualAddress = actualuser.getAddresses().get(0);
		
		compareAddressData(actualAddress);
	}

	@Test
	public void testFindUsersNameLike() {
		List<User> userList = userDaoImpl.findUsersWithNameLike("john");
		
		Assert.assertNotNull(userList);
		Assert.assertEquals(1, userList.size());
		
		User actualUser = userList.get(0);
		
		Assert.assertEquals(fakeUser.getFirstName(), actualUser.getFirstName());
		Assert.assertEquals(fakeUser.getLastName(), actualUser.getLastName());
		Assert.assertEquals(fakeUser.getSex(), actualUser.getSex());
		Assert.assertEquals(fakeUser.getUserId(), actualUser.getUserId());
	}
	
	@Test
	public void testGetSearchNear() {
		double loc[] = {-86.8556036, 35.9462032};
		
		List<User> searchResults = userDaoImpl.getSearchResults(loc, 10);
		Assert.assertNotNull(searchResults);
		Assert.assertEquals(1, searchResults.size());
		
		User actualUser = searchResults.get(0);
        compareUserData(actualUser);
		
		Address actualAddress = actualUser.getAddresses().get(0);
		
		compareAddressData(actualAddress);
	}
	
	private void injectHomeAddress() {
		double loc[] = {-86.8556036, 35.9462032};
		
		homeAddress.setAddressLine1("123 Some Street");
		homeAddress.setAddressLine2("Apartment 1");
		homeAddress.setCity("Nashville");
		homeAddress.setState("TN");
		homeAddress.setZipCode("37027");
		homeAddress.setLocation(loc);
		homeAddress.setType("Home");
		
		fakeUser.setAddresses(Arrays.asList(homeAddress));
	}
	
	private void injectInventory() {
		Inventory inventory = new Inventory();
		
		inventory.setInventoryId(new ObjectId().toString());		
		inventory.setBought(false);
		inventory.setName("Tomato");
		inventory.setDescription("Big Red Tomatoes");
		inventory.setExpiryDate(new Date());
		inventory.setQuantity(10);
		inventory.setUnit("Singles");
		
		fakeUser.setInventory(Arrays.asList(inventory));
	}
	
	private void compareAddressData(Address actualAddress) {
		Assert.assertEquals(homeAddress.getAddressLine1(), actualAddress.getAddressLine1());
		Assert.assertEquals(homeAddress.getAddressLine2(), actualAddress.getAddressLine2());
		Assert.assertEquals(homeAddress.getCity(), actualAddress.getCity());
		Assert.assertEquals(homeAddress.getState(), actualAddress.getState());
		Assert.assertEquals(homeAddress.getZipCode(), actualAddress.getZipCode());
		Assert.assertEquals(homeAddress.getType(), actualAddress.getType());
	}

	private void compareUserData(User actualuser) {
		Assert.assertNotNull(actualuser);
		Assert.assertEquals(fakeUser.getFirstName(), actualuser.getFirstName());
		Assert.assertEquals(fakeUser.getLastName(), actualuser.getLastName());
		Assert.assertEquals(fakeUser.getSex(), actualuser.getSex());
		Assert.assertEquals(fakeUser.getUserId(), actualuser.getUserId());
		Assert.assertNotNull(actualuser.getAddresses());
		Assert.assertEquals(1, actualuser.getAddresses().size());
	}
}

