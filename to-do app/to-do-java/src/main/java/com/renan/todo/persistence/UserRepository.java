package com.renan.todo.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.renan.todo.model.User;

import jakarta.transaction.Transactional;

/**
 * Repository for user operations
 */
public interface UserRepository extends JpaRepository<User, Integer> {

	/**
	 * find the user on the database by email
	 *
	 * @param email - user email
	 * @return
	 */
	public Optional<User> findByEmail(String email);

	/**
	 * Set a new password code for the user
	 *
	 * @param email           - user email
	 * @param newPasswordCode - the new password code
	 */
	@Transactional
	@Modifying
	@Query(value = "UPDATE User u set u.newPasswordCode = :newPasswordCode WHERE u.email = :email")
	public void setNewPasswordCode(@Param("email") String email, @Param("newPasswordCode") String newPasswordCode);

	/**
	 * Change the user password based on the code
	 *
	 * @param email           - user email
	 * @param password        - new password
	 * @param newPasswordCode - the code to validate
	 */
	@Transactional
	@Modifying
	@Query(value = "UPDATE User u set u.password = :password, u.newPasswordCode = null WHERE u.email = :email AND u.newPasswordCode = :newPasswordCode")
	public void resetUserPasswordByCode(@Param("email") String email, @Param("password") String password,
	        @Param("newPasswordCode") String newPasswordCode);

	/**
	 * Activate the user on the database
	 *
	 * @param email          - user email
	 * @param activationCode - the activation code
	 * @param status         - new status
	 */
	@Transactional
	@Modifying
	@Query(value = "UPDATE User u set u.userStatus = :status, u.activationCode = null WHERE u.email = :email AND u.activationCode = :activationCode")
	public void activateUser(@Param("email") String email, @Param("activationCode") String activationCode,
	        @Param("status") Integer status);

	/**
	 * Update user info on database
	 *
	 * @param id       - user id
	 * @param name     - name of user
	 * @param lastName - last name
	 */
	@Transactional
	@Modifying
	@Query(value = "UPDATE User u set u.firstName = :name, u.lastName = :lastName WHERE u.id = :id")
	public void updateUserInfo(@Param("id") Integer id, @Param("name") String name, @Param("lastName") String lastName);

	/**
	 * Update user password on database
	 *
	 * @param id       - user id
	 * @param password - user password
	 */
	@Transactional
	@Modifying
	@Query(value = "UPDATE User u set u.password = :password WHERE u.id = :id")
	public void updateUserPassword(@Param("id") Integer id, @Param("password") String password);
}
