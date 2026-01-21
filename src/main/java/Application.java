public class Application {

    private final HelloWorldService service;

    public Application(HelloWorldService service) {
        this.service = service;
    }

    public void run() {
        System.out.println(service.getMessage());
    }
}
