package database.datasets;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "accounts")
@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class AccountsDataSet implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "isActive")
    @NonNull
    private boolean isActive;

    @Column(name = "cash")
    @NonNull
    private double cash;

    // TODO: one to one with currency table
    @Column(name = "currency")
    @NonNull
    private String currency;

    // TODO: one to many with clients table
}