package mymarket.offer.services;

import mymarket.offer.exceptions.OfferNotFoundException;
import mymarket.offer.models.Branch;
import mymarket.offer.models.Offer;
import mymarket.offer.models.enums.OfferTypes;
import mymarket.offer.repositories.OfferRepository;
import mymarket.product.commons.models.Product;
import mymarket.product.commons.models.enums.Clasifications;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;


import java.util.*;

import static com.googlecode.catchexception.apis.BDDCatchException.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class OfferServiceTest {

    @Mock
    private OfferRepository offerRepository;

    @InjectMocks
    private OfferService offerService;

    private Offer offer1, offer2;

    private List<Offer> offerList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Branch branch = Branch.builder().id(1L).name("Branch 1").userId(1L).address("Street 1234").city("Mar del Plata").build();

        offer1 = Offer.builder().id(1L).productId(1L)
                .branches(Collections.singletonList(branch))
                .price(1.99f)
                .offerType(OfferTypes.discount)
                .oldPrice(2.50f)
                .build();

        offer2 = Offer.builder().id(2L).productId(1L)
                .branches(Collections.singletonList(branch))
                .price(7f)
                .offerType(OfferTypes.discount)
                .oldPrice(7.50f)
                .build();

        offerList.add(offer1);
        offerList.add(offer2);
    }

    @Test
    public void save_ExpectedValues_Ok(){
        //given
        given(offerRepository.save(any())).willReturn(offer1);

        //when
        Offer offer = offerService.save(offer1);

        //then
        then(offerRepository).should().save(offer1);
        assertThat(offer).isNotNull();
        assertThat(offer).isEqualTo(offer1);
    }

    @Test
    public void deleteById_ExpectedValues_Ok() {
        //given
        willDoNothing().given(offerRepository).deleteById(anyLong());

        //when
        offerService.deleteById(1L);
        offerService.deleteById(2L);
        offerService.deleteById(3L);

        //then
        then(offerRepository).should(times(3)).deleteById(anyLong());
    }

    @Test
    public void getById_ExpectedValues_Ok() {
        //given
        Optional<Offer> offerOptional = Optional.of(offer1);
        given(offerRepository.findById(anyLong())).willReturn(offerOptional);

        //when
        Offer offerFromRepository = offerService.getById(1L);

        //then
        then(offerRepository).should().findById(1L);
        assertThat(offerFromRepository).isNotNull();
        assertThat(offerFromRepository).isEqualTo(offerOptional.get());
    }

    @Test
    public void getById_NonexistentId_OfferNotFoundException() {
        //given
        BDDMockito.willThrow(new OfferNotFoundException("Offer with id 3 not found.")).given(offerRepository).findById(anyLong());

        //when
        when(() -> offerService.getById(3L));

        //then
        BDDAssertions.then(caughtException())
                .isInstanceOf(OfferNotFoundException.class)
                .hasMessage("Offer with id 3 not found.")
                .hasNoCause();
    }

    @Test
    public void getByFilters_ExpectedValues_Ok() {
        OfferService spyOfferService = Mockito.spy(offerService);
        doReturn(Arrays.asList(1L, 2L, 3L)).when(spyOfferService).getProductIdsByClasificationsAndProductName(anyList(), anyString());

        //given
        given(offerRepository.findByFilters(anyLong(), anyLong(), anyList(), any(), any(), any(), anyLong(), anyString(), anyBoolean()))
                .willReturn(offerList);

        //when
        List<Offer> offers = spyOfferService.getByFilters(1L, 1L, Arrays.asList(OfferTypes.values()), Arrays.asList(Clasifications.values()),
                "harina", LocalDate.of(2021, 1, 1), LocalDate.of(2021, 12, 24), null,
                1L, "Mar del Plata", true);

        //then
        then(offerRepository).should().findByFilters(1L, 1L, OfferTypes.toStringList(Arrays.asList(OfferTypes.values())),
                LocalDate.of(2021, 1, 1), LocalDate.of(2021, 12, 24), null,
                1L, "Mar del Plata", true);
        assertThat(offers).isNotNull();
        assertThat(offers).hasSize(2);
        assertThat(offers).isEqualTo(offerList);
    }
}