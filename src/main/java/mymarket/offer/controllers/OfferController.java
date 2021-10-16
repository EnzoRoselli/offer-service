package mymarket.offer.controllers;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mymarket.offer.services.OfferService;
import mymarket.product.commons.models.enums.Clasifications;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import mymarket.offer.models.Offer;
import mymarket.offer.models.enums.OfferTypes;
import mymarket.offer.repositories.OfferRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static mymarket.offer.utils.ParametersDefaultValue.OFFER_TYPES;
import static mymarket.product.commons.utils.ParametersDefaultValue.CLASIFICATIONS;

@RestController
@RequestMapping("offers")
@Slf4j
@XRayEnabled
@RequiredArgsConstructor
public class OfferController {

    private final OfferRepository repo;

    private final OfferService offerService;

    @PostMapping
    public Offer save(@RequestBody Offer offer) {
        return offerService.save(offer);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable Long id) {
        offerService.deleteById(id);
    }

    @GetMapping
    public List<Offer> getByFilters(@RequestParam(required = false) Long productId,
                                    @RequestParam(required = false) Long branchId,
                                    @RequestParam(required = false, defaultValue = OFFER_TYPES) List<OfferTypes> offerTypes,
                                    @RequestParam(required = false, defaultValue = CLASIFICATIONS) List<Clasifications> clasifications,
                                    @RequestParam(required = false, defaultValue = "") String productName,
                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate specificDate,
                                    @RequestParam(required = false) Long userId,
                                    @RequestParam(required = false) String city,
                                    @RequestParam(required = false, defaultValue = "true") Boolean available){
        return offerService.getByFilters(productId, branchId, offerTypes, clasifications, productName, startDate, endDate, specificDate, userId, city, available);
    }

    @GetMapping("{id}")
    public Offer getById(@PathVariable Long id){
        return offerService.getById(id);
    }
}
