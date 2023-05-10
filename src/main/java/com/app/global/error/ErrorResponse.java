package com.app.global.error;

import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@Builder
public class ErrorResponse {
    private String errorCode;
    private String errorMessage;

    public static ErrorResponse of(String errorCode, String errorMessage) {
        return ErrorResponse.builder()
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .build();
    }

    public static ErrorResponse of(String errorCode, BindingResult bindingResult){
        return ErrorResponse.builder()
                .errorCode(errorCode)
                .errorMessage(createErrorMessage(bindingResult))
                .build();
    }

    private static String createErrorMessage(BindingResult bindingResult) {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true; // 첫 번째 에러인지, 아닌지에 따라 에러 메세지를 만들어 줄 것이다. (콤마때문에)
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError: fieldErrors){
            if (!isFirst){
                sb.append(", ");
            } else {
                isFirst = false;
            }
            sb.append("[");
            sb.append(fieldError.getField());
            sb.append("]");
            sb.append(fieldError.getDefaultMessage());
        }
        return sb.toString();
    }
}
