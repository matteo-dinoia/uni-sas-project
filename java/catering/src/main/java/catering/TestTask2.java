package catering;

import catering.businesslogic.CatERing;
import catering.businesslogic.UseCaseLogicException;
import catering.businesslogic.event.ServiceInfo;
import catering.businesslogic.kitchenTask.Task;
import catering.businesslogic.kitchenTask.TaskManager;
import catering.businesslogic.recipe.Recipe;

import java.util.Date;

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
