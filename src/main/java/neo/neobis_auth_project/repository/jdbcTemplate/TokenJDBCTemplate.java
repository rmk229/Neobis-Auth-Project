package neo.neobis_auth_project.repository.jdbcTemplate;

import lombok.RequiredArgsConstructor;
import neo.neobis_auth_project.models.User;
import neo.neobis_auth_project.enums.Role;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TokenJDBCTemplate {
    private final JdbcTemplate jdbcTemplate;

    public Optional<User> findUserByToken(String token) {
        String sql = "SELECT u.* " +
                "FROM users u " +
                "JOIN refresh_token t ON u.user_id = t.user_id " +
                "WHERE t.token = ?";

        return jdbcTemplate.query(sql, new Object[]{token}, (rs, rowNum) -> {
            // Create and return a User object from the ResultSet
            User user = new User();
            user.setUserId(rs.getLong("user_id"));
            user.setEmail(rs.getString("email")); // Populate email field
            user.setRole(Role.valueOf(rs.getString("role")));
            // Populate other fields of the user object
            return user;
        }).stream().findFirst();
    }
}