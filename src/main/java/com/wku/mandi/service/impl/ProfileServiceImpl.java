package com.wku.mandi.service.impl;

import com.wku.mandi.dao.UserDao;
import com.wku.mandi.db.Address;
import com.wku.mandi.db.User;
import com.wku.mandi.rest.GeographicalAPI;
import com.wku.mandi.rest.ZipcodeRestAPI;
import com.wku.mandi.rest.response.GeospatialAPIResponse;
import com.wku.mandi.rest.response.ZipCodeResponse;
import com.wku.mandi.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by srujangopu on 7/5/15.
 */

@Service("ProfileService")
public class ProfileServiceImpl implements ProfileService{

    private final UserDao userDao;
    private final ZipcodeRestAPI zipcodeRestAPI;
    private final GeographicalAPI geographicalAPI;

    @Autowired
    public ProfileServiceImpl(UserDao userDao, ZipcodeRestAPI zipcodeRestAPI, GeographicalAPI geographicalAPI) {

        this.userDao = userDao;
        this.zipcodeRestAPI = zipcodeRestAPI;
        this.geographicalAPI = geographicalAPI;

    }

    @Override
    public User findUserById(String id) {
        return userDao.findUserById(id);
    }

    @Override
    public List<User> findUsersWithNameLike(String nameLike) {
        return userDao.findUsersWithNameLike(nameLike);
    }

    @Override
    public void saveUser(User user) {

        for(Address address : user.getAddresses()){
            String street1 = address.getAddressLine1();
            String street2 = address.getAddressLine2();
            String city = address.getCity();
            String state = address.getState();
            String zipCode = address.getZipCode();

            String completeAddress = String.join(" ",street1,street2,city,state,zipCode);
            GeospatialAPIResponse geospatialAPIResponse = geographicalAPI.getGeospatialAPIResponse(completeAddress);

            if(geospatialAPIResponse != null){
                address.setLatitude(geospatialAPIResponse.getResults().get(0).getGeometry().getLocation().getLat());
                address.setLongitude(geospatialAPIResponse.getResults().get(0).getGeometry().getLocation().getLng());
            }
        }
        userDao.saveUser(user);
    }

    @Override
    public User deleteUser(String id) {
        return userDao.deleteUser(id);
    }

    @Override
    public ZipCodeResponse getAddressDetails(String zipCode) {
        return zipcodeRestAPI.getAddressDetails(zipCode);
    }


}
