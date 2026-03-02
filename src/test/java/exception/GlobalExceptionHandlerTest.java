package exception;

import com.myapp.exception.ApiError;
import com.myapp.exception.GlobalExceptionHandler;
import com.myapp.exception.TaskNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleTaskNotFound_shouldReturn404AndPayload() {
        ResponseEntity<ApiError> response = handler.handleTaskNotFound(new TaskNotFoundException(7));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("NOT_FOUND", response.getBody().getError());
        assertEquals("Task with id 7 not found", response.getBody().getMessage());
    }

    @Test
    void handleBadRequest_shouldReturn400AndPayload() {
        ResponseEntity<ApiError> response = handler.handleBadRequest(new IllegalArgumentException("bad request"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("BAD_REQUEST", response.getBody().getError());
        assertEquals("bad request", response.getBody().getMessage());
    }

    @Test
    void handleGenericException_shouldReturn500AndGenericMessage() {
        ResponseEntity<ApiError> response = handler.handleGenericException(new RuntimeException("boom"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getStatus());
        assertEquals("INTERNAL_SERVER_ERROR", response.getBody().getError());
        assertEquals("An unexpected error occurred", response.getBody().getMessage());
    }
}
