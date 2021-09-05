package tesis.offer.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Builder
public class OfertaDTO {
    private Integer idOferta;
    private Integer branchID;
    private Integer cardID;
    private Float price;
    private String offerType;
    private String offerDescription;
    private Timestamp fromDate;
    private Timestamp toDate;
    private Boolean avaliable;
    private String oldPrice;

    private Integer idProduct;
    private String nameProduct;
    private String imageProduct;
    private String clasificationProduct;
    private String descriptionProduct;

    public static List<OfertaDTO> getInfo(List<Object[]> dtos) {
        return dtos.stream().map(OfertaDTO::getDTO).collect(Collectors.toList());
    }

    public static OfertaDTO getDTO(Object[] dto) {
        return OfertaDTO.builder()
                .idOferta((Integer) dto[0])
                .branchID((Integer) dto[1])
                .cardID((Integer) dto[2])
                .price((Float) dto[3])
                .offerType((String) dto[4])
                .fromDate((Timestamp) dto[5])
                .toDate((Timestamp) dto[6])
                .avaliable((Boolean) dto[7])
                .oldPrice((String) dto[8])
                .idProduct((Integer) dto[9])
                .nameProduct((String) dto[10])
                .imageProduct((String) dto[11])
                .clasificationProduct((String) dto[12])
                .descriptionProduct((String) dto[13])
                .offerDescription((String)dto[14])
                .build();
    }

}
