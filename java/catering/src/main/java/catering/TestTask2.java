package catering;

import catering.businesslogic.CatERing;
import catering.businesslogic.kitchenTask.TaskManager;

public class TestTask2 {
    public static final CatERing INSTANCE = CatERing.getInstance();
    public static final TaskManager TASK_MGR = INSTANCE.getTaskManager();

    public static void main(String[] args) {
        INSTANCE.getUserManager().fakeLogin("Lidia");
        System.out.println(INSTANCE.getUserManager().getCurrentUser());

        TASK_MGR.getTasks().forEach(TASK_MGR::deleteTask);
        System.out.println("\nAll task deleted\n");
    }
}
