package com.wku.mandi.dao;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.wku.mandi.db.User;
import org.springframework.data.repository.Repository;

import java.io.InputStream;
import java.util.List;

public interface UserDao extends Repository<User, String> {

	public User findUserById(String id);

	public List<User> findUsersWithNameLike(String nameLike);

	public void saveUser(User user);

	public void updateUser(User user);

	public User deleteUser(String id);

	public void uploadProfileImage(String id, InputStream profileImage);

	public List<User> getSearchResults(double[] loc,int distance);

}