package org.springframework.samples.dwarf.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.dwarf.statistic.Achievement;

public interface UserRepository extends CrudRepository<User, String> {

	// @Modifying
	// @Query("DELETE FROM Owner o WHERE o.user.username = :username")
	// void deleteOwnerOfUser(String username);
	//
	// @Modifying
	// @Query("DELETE FROM Pet p WHERE p.owner.id = :id")
	// public void deletePetsOfOwner(@Param("id") int id);

	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);

	Optional<User> findById(Integer id);

	@Query("SELECT u FROM User u WHERE u.authority.authority = :auth")
	Iterable<User> findAllByAuthority(String auth);

	Pageable pageable = PageRequest.of(0, 5, Sort.by(Order.asc("username")));

	Page<User> findAll(Pageable pageable);

	@Query("SELECT u FROM User u WHERE u.isLoggedIn = :b")
	List<User> findByIsLoggedIn(boolean b);

	@Query("SELECT u.achievements FROM User u WHERE u.username = ?1")
	List<Achievement> findAchievemenByUserName(String username);

}
