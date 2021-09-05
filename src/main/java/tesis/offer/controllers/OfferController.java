package tesis.offer.controllers;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import tesis.offer.models.OfertaDTO;
import tesis.offer.models.Offer;
import tesis.offer.models.OfferTypes;
import tesis.offer.models.SaveMultipleOffers;
import tesis.offer.repositories.OfferRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static tesis.offer.utils.ParametersDefaultValue.CLASIFICATIONS;
import static tesis.offer.utils.ParametersDefaultValue.OFFER_TYPES;

@RestController
@Slf4j
@XRayEnabled
@RequiredArgsConstructor
public class OfferController {

    @Value("${products-url}")
    private String productUrl;

    private final OfferRepository repo;

    @PostMapping("/")
    public Offer save(@RequestBody @NotNull Offer offer) {

        if (offer.getId() != null){
            Optional<Offer> offerToUpdate = repo.findById(offer.getId());

        }
        return repo.save(offer);
    }

//    public Offer editOffer(@RequestBody @NotNull Offer offer){
//        return repo.
//    }

    @PostMapping("/multipleSave")
    public void saveMultipleOffers(@RequestBody SaveMultipleOffers offer) {
        repo.saveAll(Offer.fromMultipleOffers(offer));
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        SaveMultipleOffers offer = SaveMultipleOffers.from(repo.findById(id).orElseThrow(() -> new RuntimeException("Offer not found")));
        List<Offer> similarOffers = getOfferFromAllBranches(offer);

        repo.deleteAll(similarOffers);
    }

    @GetMapping("products/{id}")
    public List<Offer> getByProductId(@PathVariable("id") Integer id) {
        return repo.findByProductID(id);
    }

    @GetMapping("branches/{id}")
    public List<Offer> getByBranchId(@PathVariable("id") Integer id) {
        return repo.findByBranchID(id);
    }

    @GetMapping("type/{type}")
    public List<Offer> getByClasification(@PathVariable("type") String type) {
        return repo.findByOfferTypeAndAvailableTrue(OfferTypes.valueOf(type));
    }

    @GetMapping("")
    public List<OfertaDTO> getByFilters(@RequestParam(required = false, defaultValue = CLASIFICATIONS) List<String> clasificaciones,
                                        @RequestParam(required = false, defaultValue = OFFER_TYPES) List<String> tipos,
                                        @RequestParam(required = false, defaultValue = "") String nombre) {
        return OfertaDTO.getInfo(repo.dameProductos(LocalDateTime.now(), LocalDateTime.now(), tipos, clasificaciones, nombre));
    }

    public List getProductsByClasificationsAndName(List<String> clasificaciones, String nombre) {
        RestTemplate rt = new RestTemplate();
        return rt.getForObject(productUrl + "/product/ids?clasificaciones=" + clasificaciones.toString().replace("[", "").replace("]", "") + "&nombre=" + nombre, List.class);
    }

    @GetMapping("startDate/{startDate}")
    public List<Offer> getByStartDate(@PathVariable("startDate") String startDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(startDate, formatter);
        return repo.findByFromDateGreaterThanEqualAndAvailableTrue(dateTime);
    }

    @GetMapping("startDate/{startDate}/endDate/{endDate}")
    public List<Offer> getByStartDateAndEndDate(@PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm");
        LocalDateTime startTime = LocalDateTime.parse(startDate, formatter);
        LocalDateTime endTime = LocalDateTime.parse(endDate, formatter);
        return repo.findByFromDateLessThanEqualAndToDateGreaterThanEqualAndAvailableTrue(startTime, endTime);
    }

    @GetMapping("{date}")
    public List<Offer> getBySpecificDate(@PathVariable("date") String specificDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(specificDate, formatter);
        return repo.findBySpecificDate(dateTime);
    }

    @GetMapping("users/{id}")
    public List<SaveMultipleOffers> getByUser(@PathVariable("id") Long userId) {
        List<SaveMultipleOffers> offers = SaveMultipleOffers.fromOffers(repo.findAvailableOfferForUser(userId));
        List<Long> offersToDelete = new ArrayList<>();

        for (int i = 0; i < offers.size()-1; i++) {
            for (int j = i+1; j < offers.size(); j++) {

                if (!offersToDelete.contains(offers.get(j).getId())){
                    if (offers.get(i).equals(offers.get(j))){
                        offers.get(i).getBranchIDs().addAll(offers.get(j).getBranchIDs());
                        offersToDelete.add(offers.get(j).getId());
                    }

                }
            }
        }

        offers.removeIf(offer -> offersToDelete.contains(offer.getId()));
        offers.forEach(offer -> {
            offer.setNameProduct(repo.getProductName(offer.getProductID()));
            offer.setFromDate(offer.getFromDate().minusHours(3));
            offer.setToDate(offer.getToDate().minusHours(3));
        });

        return offers;
    }

    @GetMapping("cities/{name}")
    public List<Offer> getByCity(@PathVariable("city") String city) {
        return repo.findByCity(city);
    }

    @GetMapping("id/{id}")
    public SaveMultipleOffers getById(@PathVariable Long id){
        SaveMultipleOffers offer = SaveMultipleOffers.from(repo.findById(id).orElseThrow(() -> new RuntimeException("Offer not found")));
        List<Offer> similarOffers = getOfferFromAllBranches(offer);

        offer.setNameProduct(repo.getProductName(offer.getProductID()));
        offer.setProductImage(repo.getProductImage(offer.getProductID()));
        similarOffers.forEach(similarOffer -> {
            if (!offer.getId().equals(similarOffer.getId()))
                offer.getBranchIDs().add(similarOffer.getBranchID());
        });

        return offer;
    }

    public List<Offer> getOfferFromAllBranches(SaveMultipleOffers offer){
        return  repo.findSimilarOffers(offer.getProductID(), offer.getPrice(), offer.getCardID(), offer.getOfferType().toString(),
                offer.getFromDate(), offer.getToDate(), offer.getOldPrice(), offer.getOfferDescription());
    }

    @PutMapping
    public List<Offer> updateOffer(@RequestBody SaveMultipleOffers offers){
        List<Offer> offersToUpdate = getOfferFromAllBranches(getById(offers.getId()));
        List<Offer> offersToRemove = new ArrayList<>();
        List<Integer> branchesFromOffersToUpdate = new ArrayList<>();


        for (Offer offerToUpdate : offersToUpdate) {
            if (!offers.getBranchIDs().contains(offerToUpdate.getBranchID())){
                offersToRemove.add(offerToUpdate);
            }
            branchesFromOffersToUpdate.add(offerToUpdate.getBranchID());
        }

        offersToUpdate.removeAll(offersToRemove);
        offersToUpdate = offersToUpdate.stream()
                .map(offer -> offer = Offer.fromUpdate(offers, offer.getId(), offer.getBranchID()))
                .collect(Collectors.toList());

        List<Integer> branchIDs = new ArrayList<>();
        for (Integer branch:offers.getBranchIDs()) {
            if (!branchesFromOffersToUpdate.contains(branch)){
                branchIDs.add(branch);
            }
        }

        offers.setBranchIDs(branchIDs);
        saveMultipleOffers(offers);
        repo.deleteAll(offersToRemove);
        return repo.saveAll(offersToUpdate);
    }
}
