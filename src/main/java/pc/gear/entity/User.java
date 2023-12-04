package pc.gear.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pc.gear.util.type.Gender;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    private Long id;

    @Column(name = "name", columnDefinition = "nvarchar(255) not null")
    private String name;

    @Column(name = "age", columnDefinition = "int not null")
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", columnDefinition = "nvarchar(10) not null")
    private Gender gender;
}
