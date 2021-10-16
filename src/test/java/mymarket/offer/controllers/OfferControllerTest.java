package mymarket.offer.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import mymarket.offer.exceptions.OfferNotFoundException;
import mymarket.offer.models.Branch;
import mymarket.offer.models.Offer;
import mymarket.offer.models.enums.OfferTypes;
import mymarket.offer.services.OfferService;
import mymarket.product.commons.models.enums.Clasifications;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
public class OfferControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Mock
    private OfferService offerService;

    @InjectMocks
    private OfferController offerController;

    private Offer offer1, offer2;

    private List<Offer> offerList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(offerController)
                .setControllerAdvice(new ExceptionController())
                .build();

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
    public void save_ExpectedValues_Ok() throws Exception {
        //given
        given(offerService.save(any())).willReturn(offer1);

        //when
        MockHttpServletResponse response = mockMvc.perform(post("/offers/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(offer1))
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        then(offerService).should().save(offer1);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).isEqualTo(asJsonString(offer1));
    }

    @Test
    public void save_MissingValues_DataIntegrityViolationException() throws Exception {
        //given
        given(offerController.save(any())).willThrow(new DataIntegrityViolationException(""));

        //when
        MockHttpServletResponse response = mockMvc.perform(post("/offers/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(offer1))
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        then(offerService).should().save(offer1);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void deleteById_ExpectedValues_Ok() throws Exception {
        //given
        willDoNothing().given(offerService).deleteById(anyLong());

        //when
        MockHttpServletResponse response = mockMvc.perform(delete("/offers/1")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        then(offerService).should().deleteById(1L);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void deleteById_NonexistentId_EmptyResultDataAccessException() throws Exception {
        //given
        willThrow(new EmptyResultDataAccessException(0)).given(offerService).deleteById(anyLong());

        //when
        MockHttpServletResponse response = mockMvc.perform(delete("/offers/150")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        then(offerService).should().deleteById(150L);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void getById_ExpectedValues_Ok() throws Exception {
        //given
        given(offerService.getById(anyLong())).willReturn(offer1);

        //when
        MockHttpServletResponse response = mockMvc.perform(get("/offers/4")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        then(offerService).should().getById(4L);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(asJsonString(offer1));
    }

    @Test
    public void getById_NonexistentId_OfferNotFoundException() throws Exception {
        //given
        BDDMockito.willThrow(new OfferNotFoundException("")).given(offerService).getById(anyLong());

        //when
        MockHttpServletResponse response = mockMvc.perform(get("/offers/150")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        //then
        then(offerService).should().getById(150L);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void getByFilters_ExpectedValues_Ok() throws Exception {
        //given
        given(offerService.getByFilters(anyLong(), anyLong(), anyList(), anyList(), anyString(), any(), any(),
                any(), anyLong(), anyString(), anyBoolean()))
                .willReturn(offerList);

        //when
        MockHttpServletResponse response = mockMvc.perform(get("/offers")
                .param("productId", "1")
                .param("branchId", "1")
                .param("offerTypes", "discount,promotion,quantity")
                .param("clasifications", "Almacen")
                .param("productName", "Harina")
                .param("startDate", "2021-10-01")
                .param("endDate", "2021-11-01")
                .param("userId", "1")
                .param("city", "Mar del Plata")
                .param("available", "true")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn().getResponse();

        //then
        then(offerService).should().getByFilters(1L, 1L, Arrays.asList(OfferTypes.values()), Collections.singletonList(Clasifications.Almacen),
                "Harina", LocalDate.of(2021, 10, 1), LocalDate.of(2021, 11, 1), null,
                1L, "Mar del Plata", true);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(asJsonString(offerList));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
