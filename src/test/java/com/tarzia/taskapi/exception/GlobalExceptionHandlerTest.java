package com.tarzia.taskapi.exception;

import com.tarzia.taskapi.dto.TaskRequestDTO;
import jakarta.validation.Valid;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldHandleResourceNotFoundException() {
        ResponseEntity<ErrorResponse> response = handler.handleResourceNotFoundException(
                new ResourceNotFoundException("Task not found")
        );

        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Task not found", response.getBody().getMessage());
        assertEquals(404, response.getBody().getStatus());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void shouldHandleValidationException() throws Exception {
        TaskRequestDTO dto = new TaskRequestDTO();
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(dto, "taskRequestDTO");
        bindingResult.addError(new FieldError("taskRequestDTO", "title", "must not be blank"));

        Method method = DummyValidationTarget.class.getDeclaredMethod("validate", TaskRequestDTO.class);
        MethodParameter parameter = new MethodParameter(method, 0);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(parameter, bindingResult);

        ResponseEntity<ErrorResponse> response = handler.handleValidationException(exception);

        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("title: must not be blank", response.getBody().getMessage());
        assertEquals(400, response.getBody().getStatus());
        assertNotNull(response.getBody().getTimestamp());
    }

    private static class DummyValidationTarget {
        @SuppressWarnings("unused")
        void validate(@Valid TaskRequestDTO dto) {
            // No implementation needed, method is only used to build MethodParameter.
        }
    }
}

