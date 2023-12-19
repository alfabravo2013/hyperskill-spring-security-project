package taskmanagement.common;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ProblemDetail handleApiException(ApiException e) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatusCode.valueOf(e.getStatusCode()),
                e.getMessage()
        );
    }
}
