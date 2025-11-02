package models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean succeeded;
    private T result;
    private Object error;
}
