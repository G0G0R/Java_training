import service.TaskService;

public class Application {

    private final TaskService taskService;

    public Application(TaskService taskService) {
        this.taskService = taskService;
    }

    public void run() {
        taskService.createTask("Ceci est ma tache 1");
        taskService.createTask("Ceci est ma tache 2");
        taskService.createTask("Ceci est ma tache 3");
        taskService.getAllTasks().forEach(System.out::println);
        System.out.println(taskService.getTaskById(2));
        System.out.println(taskService.getTaskById(0));
    }
}
