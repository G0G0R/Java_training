package dto;

import com.myapp.dto.UpdateTaskRequest;
import com.myapp.model.Priority;
import com.myapp.model.Status;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UpdateTaskRequestTest {

    @Test
    void getters_shouldReturnFieldValues() throws Exception {
        UpdateTaskRequest request = new UpdateTaskRequest();

        setField(request, "description", "description patch");
        setField(request, "status", Status.IN_PROGRESS);
        setField(request, "priority", Priority.HIGH);
        setField(request, "dueDate", LocalDate.of(2026, 1, 15));

        assertEquals("description patch", request.getDescription());
        assertEquals(Status.IN_PROGRESS, request.getStatus());
        assertEquals(Priority.HIGH, request.getPriority());
        assertEquals(LocalDate.of(2026, 1, 15), request.getDueDate());
    }

    private void setField(Object target, String name, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }
}
