package tesis.offer.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder
public class SaveMultipleOffers {

    private Long id;
    private Integer productID;
    private List<Integer> branchIDs = new ArrayList<>();
    private Integer cardID;
    private Float price;
    private OfferTypes offerType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime fromDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime toDate;
    private Boolean available;
    private String oldPrice;
    private String offerDescription;
    private String nameProduct;
    private String productImage;

    public static List<SaveMultipleOffers> fromOffers(List<Offer> offers) {
        return offers.stream()
                .map(SaveMultipleOffers::from)
                .collect(Collectors.toList());
    }

    public static SaveMultipleOffers from(Offer offer) {
        return SaveMultipleOffers.builder()
                .id(offer.getId())
                .productID(offer.getProductID())
                .branchIDs(new ArrayList<>(Collections.singletonList(offer.getBranchID())))
                .cardID(offer.getCardID())
                .price(offer.getPrice())
                .offerType(offer.getOfferType())
                .fromDate(offer.getFromDate())
                .toDate(offer.getToDate())
                .available(offer.getAvailable())
                .oldPrice(offer.getOldPrice())
                .offerDescription(offer.getOfferDescription())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaveMultipleOffers that = (SaveMultipleOffers) o;
        return Objects.equals(productID, that.productID) && Objects.equals(cardID, that.cardID) &&
                Objects.equals(price, that.price) && offerType == that.offerType && Objects.equals(fromDate, that.fromDate) &&
                Objects.equals(toDate, that.toDate) && Objects.equals(available, that.available) && Objects.equals(oldPrice, that.oldPrice) &&
                Objects.equals(offerDescription, that.offerDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productID, cardID, price, offerType, fromDate, toDate, available, oldPrice, offerDescription);
    }
}
