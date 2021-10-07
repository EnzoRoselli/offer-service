package mymarket.offer.models;

import lombok.*;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "branches")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "address", nullable = false)
    private String address;
    @Column(name = "city", nullable = false)
    private String city;
    @Column(name = "latitude")
    private String latitude;
    @Column(name = "longitude")
    private String longitude;
}
