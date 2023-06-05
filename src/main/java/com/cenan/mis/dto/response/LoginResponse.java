package com.cenan.mis.dto.response;

import com.cenan.mis.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String userName;
    private Role role;
    private String token;
}
