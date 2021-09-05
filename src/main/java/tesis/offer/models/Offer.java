package tesis.offer.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private Integer productID;
    @Column(name = "branch_id")
    private Integer branchID;
    @Column(name = "card_id")
    private Integer cardID;
    @Column(name = "price")
    private Float price;
    @Enumerated(EnumType.STRING)
    @Column(name = "offer_type")
    private OfferTypes offerType;
    @Column(name = "from_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime fromDate;
    @Column(name = "to_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime toDate;
    @Column(name = "available")
    @Builder.Default
    private Boolean available=true;
    @Column(name = "old_price")
    private String oldPrice;
    @Column(name = "offer_description")
    private String offerDescription;

    public static List<Offer> fromMultipleOffers(SaveMultipleOffers offer) {
        return IntStream.range(0, offer.getBranchIDs().size())
                .mapToObj(index -> Offer.from(offer, index))
                .collect(Collectors.toList());
    }

    public static Offer from(SaveMultipleOffers offer, Integer branchIDindex) {
        return Offer.builder()
                .available(offer.getAvailable())
                .branchID(offer.getBranchIDs().get(branchIDindex))
                .cardID(offer.getCardID())
                .fromDate(offer.getFromDate().plusHours(3))
                .toDate(offer.getToDate().plusHours(3))
                .offerDescription(offer.getOfferDescription())
                .oldPrice(offer.getOldPrice())
                .offerType(offer.getOfferType())
                .price(offer.getPrice())
                .productID(offer.getProductID())
                .build();
    }

    public static Offer fromUpdate(SaveMultipleOffers offer, Long offerID, Integer branchID) {
        return Offer.builder()
                .id(offerID)
                .available(offer.getAvailable())
                .cardID(offer.getCardID())
                .fromDate(offer.getFromDate().plusHours(3))
                .toDate(offer.getToDate().plusHours(3))
                .offerDescription(offer.getOfferDescription())
                .oldPrice(offer.getOldPrice())
                .offerType(offer.getOfferType())
                .price(offer.getPrice())
                .productID(offer.getProductID())
                .branchID(branchID)
                .build();
    }
}
