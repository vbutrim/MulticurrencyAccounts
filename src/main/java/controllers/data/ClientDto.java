package controllers.data;

import lombok.Data;

@Data
public class ClientDto {
    private long id;

    private String name;

    private String passportId;

    public ClientDto(String name, String passportId) {
        this.name = name;
        this.passportId = passportId;
    }
}
