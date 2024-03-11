package kz.yermek.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class TokenResponse{
    private String accessToken;
    private String refreshToken;
}