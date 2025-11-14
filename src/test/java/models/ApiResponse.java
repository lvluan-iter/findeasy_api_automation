package models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean succeeded;
    private T result;
    private List<String> errors;
}
