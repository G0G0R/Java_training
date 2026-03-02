package exception;

import com.myapp.exception.TaskNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskNotFoundExceptionTest {

    @Test
    void constructor_withTaskId_shouldFormatMessage() {
        TaskNotFoundException exception = new TaskNotFoundException(42);

        assertEquals("Task with id 42 not found", exception.getMessage());
    }

    @Test
    void constructor_withCustomMessage_shouldKeepMessage() {
        TaskNotFoundException exception = new TaskNotFoundException("Custom error message");

        assertEquals("Custom error message", exception.getMessage());
    }
}
