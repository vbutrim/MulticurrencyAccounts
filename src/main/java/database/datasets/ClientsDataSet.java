package database.datasets;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class ClientsDataSet implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", unique = true)
    @NonNull
    private String name;

    @Column(name = "passportId")
    @NonNull
    private String passportId;

    @OneToMany(
            mappedBy = "client",
            fetch = FetchType.EAGER
    )
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 30)
    private List<AccountsDataSet> accounts = new ArrayList<>();
}