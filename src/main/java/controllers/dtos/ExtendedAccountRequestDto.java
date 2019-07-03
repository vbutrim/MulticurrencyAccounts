package controllers.dtos;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class ExtendedAccountRequestDto extends AccountRequestDto {

    private String action;

    private Long amount;

    public ExtendedAccountRequestDto(String clientName, String currency, String action, Long amount) {
        super(clientName, currency);
        this.action = action;
        this.amount = amount;
    }


    public ExtendedAccountRequestDto(String clientName, String currency, Long amount) {
        super(clientName, currency);
        this.amount = amount;
        this.action = null;
    }
}
