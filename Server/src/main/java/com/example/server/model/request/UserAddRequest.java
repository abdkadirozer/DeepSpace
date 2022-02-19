package com.example.server.model.request;

import com.example.server.model.User;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

import static com.example.server.model.User.UserBuilder.anUser;

@AllArgsConstructor
public class UserAddRequest {
    @NotBlank
    @Length(max = 20)
    private String username;

    @NotBlank
    @Length(min = 6, max = 20)
    private String password;

    public User toUser() {
        return anUser()
                .setUsername(this.username)
                .setPassword(this.password)
                .build();
    }
}
