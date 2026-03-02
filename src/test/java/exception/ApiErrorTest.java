package exception;

import com.myapp.exception.ApiError;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ApiErrorTest {

    @Test
    void constructor_shouldPopulateAllFields() {
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);

        ApiError apiError = new ApiError(404, "NOT_FOUND", "Task with id 1 not found");

        assertEquals(404, apiError.getStatus());
        assertEquals("NOT_FOUND", apiError.getError());
        assertEquals("Task with id 1 not found", apiError.getMessage());
        assertNotNull(apiError.getTimestamp());
        assertTrue(apiError.getTimestamp().isAfter(before));
    }
}
