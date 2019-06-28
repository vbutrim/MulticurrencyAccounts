package database.datasets;

import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
    @Expose
    private List<AccountsDataSet> accounts = new ArrayList<>();
}