package kz.yermek.dto;

import kz.yermek.enums.Role;
import lombok.Builder;

@Builder
public record SignInResponse(
    Long id,
    String accessToken,
    String refreshToken,
    String email,
    Role role
){
}