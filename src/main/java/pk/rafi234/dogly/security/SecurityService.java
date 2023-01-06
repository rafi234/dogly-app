package pk.rafi234.dogly.security;

import pk.rafi234.dogly.user.dto.UserResponse;

public interface SecurityService {
    UserResponse grantPermission(String role, String id, String action);
}
