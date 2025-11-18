package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String fullname;
    private String phoneNumber;
    private String gender;
    private String birthdate;
    private String currentPassword;
    private String newPassword;
}
