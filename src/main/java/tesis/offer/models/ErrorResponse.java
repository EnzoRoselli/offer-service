package tesis.offer.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private String statusCode;
    private String errorMessage;
    private LocalDateTime dateTime;
}
