package mymarket.offer.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import mymarket.offer.model.enums.Partners;

import javax.persistence.*;

@Entity
@Table(name = "partners_x_offers")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Partner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "partners", nullable = false)
    @Enumerated(EnumType.STRING)
    private Partners partners;
    @Column(name = "link", nullable = false)
    private String link;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Offer offer;
}
