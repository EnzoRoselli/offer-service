package mymarket.offer.controller;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mymarket.offer.model.Offer;
import mymarket.offer.model.enums.OfferTypes;
import mymarket.offer.service.OfferService;
import mymarket.product.commons.models.Product;
import mymarket.product.commons.models.enums.Clasifications;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
<<<<<<< HEAD
import mymarket.offer.model.Offer;
import mymarket.offer.model.enums.OfferTypes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
=======
>>>>>>> e6d6662d4342e2af1e376baf57f8ee38d794a6f1

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static mymarket.offer.util.ParametersDefaultValue.OFFER_TYPES;
import static mymarket.product.commons.utils.ParametersDefaultValue.CLASIFICATIONS;

@RestController
@RequestMapping("offers")
@Slf4j
@XRayEnabled
@RequiredArgsConstructor
public class OfferController {

    private final OfferService offerService;

    @PostMapping
    public ResponseEntity<Offer> save(@RequestBody Offer offer) {
        return ResponseEntity.created(getLocation(offerService.save(offer))).build();
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable Long id) {
        offerService.deleteById(id);
    }

    @GetMapping
    public ResponseEntity<List<Offer>> getByFilters(@RequestParam(required = false) Long productId,
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
        List<Offer> offers = offerService.getByFilters(productId, branchId, offerTypes, clasifications, productName, startDate, endDate, specificDate, userId, city, available);

        return offers.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(offers);

    }

    @GetMapping("{id}")
    public Offer getById(@PathVariable Long id) {
        return offerService.getById(id);
    }

    private URI getLocation(Offer offer) {

        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(offer.getId())
                .toUri();
    }
}
