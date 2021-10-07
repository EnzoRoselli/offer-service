package mymarket.offer.models.enums;

import java.util.List;
import java.util.stream.Collectors;

public enum OfferTypes {
    discount,promotion,quantity;

    public static List<String> toStringList(List<OfferTypes> offerTypes) {
        return offerTypes.stream().map(Enum::toString).collect(Collectors.toList());
    }
}
