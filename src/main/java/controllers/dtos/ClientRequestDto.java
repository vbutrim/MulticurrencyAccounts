package controllers.dtos;

import lombok.Data;

@Data
public class ClientRequestDto {

    private String name;

    private String passportId;

    private String ccyOfInitialAccount;

}
