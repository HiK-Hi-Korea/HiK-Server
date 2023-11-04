package HiK.HiKServer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Table(name = "user")
@Entity
public class User {

    @Id
    private Long userId;
    @Column
    private String username;
    @Column
    private int age;
    @Column
    private String gender;
    @Column
    private String nation;
    @Column
    private String language;
}
