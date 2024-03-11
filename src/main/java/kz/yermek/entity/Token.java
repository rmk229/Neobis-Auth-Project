package kz.yermek.entity;

import jakarta.persistence.*;
import lombok.*;
import kz.yermek.enums.TokenType;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "refresh_token")
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private String refreshToken;
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;
    public boolean revoked;
    public boolean expired;
    private LocalDateTime confirmedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;
}