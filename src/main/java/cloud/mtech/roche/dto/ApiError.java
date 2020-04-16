package cloud.mtech.roche.dto;

import lombok.Data;

import java.util.List;

@Data
public class ApiError {

    private final int status;
    private final String errorMessage;
    private List<String> errors;
}
