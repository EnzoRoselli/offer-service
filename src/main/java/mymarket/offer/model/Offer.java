package mymarket.offer.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import mymarket.offer.model.enums.OfferTypes;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "offers")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "product_id")
    private Long productId;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "branches_x_offers",
            joinColumns = @JoinColumn(name = "offer_id"),
            inverseJoinColumns = @JoinColumn(name = "branch_id"))
    private List<Branch> branches;
    @Column(name = "price", nullable = false)
    private Float price;
    @Column(name = "offer_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private OfferTypes offerType;
    @Column(name = "from_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime fromDate;
    @Column(name = "to_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime toDate;
    @Column(name = "available", nullable = false)
    @Builder.Default
    private Boolean available = true;
    @Column(name = "old_price", nullable = false)
    private Float oldPrice;
    @Column(name = "offer_description")
    private String offerDescription;
    @JsonManagedReference
    @OneToMany(mappedBy = "offer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Partner> partners;
}
