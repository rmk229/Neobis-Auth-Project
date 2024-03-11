package kz.yermek.repository;

import kz.yermek.dto.UserResponse;
import kz.yermek.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> getUserByEmail(String email);
    boolean existsUserByEmail(String email);
    @Query("select new neo.neobis_auth_project.dto.UserResponse(u.email)from User u")
    List<UserResponse> getAllUser();
}