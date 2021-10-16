package mymarket.offer.service;

import lombok.RequiredArgsConstructor;
import mymarket.offer.exception.OfferNotFoundException;
import mymarket.offer.model.Offer;
import mymarket.offer.model.enums.OfferTypes;
import mymarket.offer.repository.OfferRepository;
import mymarket.product.commons.models.Product;
import mymarket.product.commons.models.enums.Clasifications;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OfferService {

    private final OfferRepository offerRepository;

    @Value("${products-url}")
    private String productUrl;

    private RestTemplate restTemplate = new RestTemplate();

    public Offer save(Offer offer){
        return offerRepository.save(offer);
    }

    public void deleteById(Long id){ offerRepository.deleteById(id); }

    public Offer getById(Long id){
        return offerRepository.findById(id).orElseThrow(() -> new OfferNotFoundException("Offer with id" + id + " not found."));
    }

    public List<Offer> getByFilters(Long productId, Long branchId, List<OfferTypes> offerTypes, List<Clasifications> clasifications, String productName,
                                    LocalDate startDate, LocalDate endDate, LocalDate specificDate, Long userId, String city, Boolean available){
        List<Long> productIds = getProductIdsByClasificationsAndProductName(clasifications, productName);

        return offerRepository.findByFilters(productId, branchId, OfferTypes.toStringList(offerTypes), startDate,
                endDate, specificDate, userId, city,available).stream()
                .filter(offer -> productIds.contains(offer.getProductId()))
                .collect(Collectors.toList());
    }

    public List<Long> getProductIdsByClasificationsAndProductName(List<Clasifications> clasifications, String productName){
        String clasificationsToString = clasifications.stream().map(Enum::toString).collect(Collectors.joining(","));

        return Arrays.stream(Objects.requireNonNull(restTemplate.getForEntity(
                productUrl + "/products?clasifications=" + clasificationsToString + "&name=" + productName, Product[].class)
                .getBody()))
                .map(Product::getId)
                .collect(Collectors.toList());
    }
}
