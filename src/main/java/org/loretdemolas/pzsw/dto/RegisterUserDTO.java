package org.loretdemolas.pzsw.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RegisterUserDTO {
    private String username;

    private  String password;
}
