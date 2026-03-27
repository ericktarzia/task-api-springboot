package com.tarzia.taskapi.dto.common;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> {
    private boolean success;
    private String message;
    private T data;
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    public static <T> BaseResponse<T> ok(T data) {

        return BaseResponse.<T>builder()
                .success(true)
                .data(data)
                .timestamp(LocalDateTime.now())
                .message("Operação realizada com sucesso.")
                .build();
    }


    public static <T> BaseResponse<T> error(String message) {
        return BaseResponse.<T>builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
