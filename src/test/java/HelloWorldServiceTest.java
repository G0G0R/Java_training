import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelloWorldServiceTest {

    @Test
    void shouldReturnHelloWorldMessage() {
        HelloWorldService service = new HelloWorldService();
        assertEquals("Hello World!", service.getMessage());
    }
}
