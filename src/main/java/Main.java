import service.TaskService;

public class Main {
    public static void main(String[] args) {
        new Application(new TaskService()).run();
    }
}
