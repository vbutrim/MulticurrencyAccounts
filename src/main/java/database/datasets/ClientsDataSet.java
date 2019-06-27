package database.datasets;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

import lombok.NonNull;

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
}