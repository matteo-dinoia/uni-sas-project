package catering;

import catering.businesslogic.CatERing;
import catering.businesslogic.UseCaseLogicException;
import catering.businesslogic.event.ServiceInfo;
import catering.businesslogic.kitchenTask.Task;
import catering.businesslogic.kitchenTask.TaskManager;
import catering.businesslogic.recipe.Recipe;

import java.util.Date;

public class TestTask1 {
    public static final CatERing INSTANCE = CatERing.getInstance();
    public static final TaskManager TASK_MGR = INSTANCE.getTaskManager();

    public static void main(String[] args) {
        try {
            /* System.out.println("TEST DATABASE CONNECTION");
            PersistenceManager.testSQLConnection();*/
            INSTANCE.getUserManager().fakeLogin("Lidia");
            System.out.println(INSTANCE.getUserManager().getCurrentUser());

            ServiceInfo service = INSTANCE.getEventManager().getEventInfo().get(0).getServices().get(0);
            TASK_MGR.openService(service);
            printTasks("doing nothing");

            Recipe randomRecipe = INSTANCE.getRecipeManager().getRecipes().get(0);
            Task newTask = TASK_MGR.createNewTask(randomRecipe);
            printTasks("adding a task");

            TASK_MGR.markTaskAsAlreadyCompleted(newTask, true);
            printTasks("marking as completed");

            TASK_MGR.editTask(newTask, new Date(), 120, 50);
            printTasks("changing date, duration, quanitity");

            TASK_MGR.editTask(newTask, null, 130, null);
            printTasks("changing duration");

        } catch (UseCaseLogicException ex) {
            System.out.println("Errore di logica nello use case: " + ex.getMessage());
        }
    }

    public static void printTasks(String operationMsg){
        System.out.println("\nDATA FROM SERVER AFTER '" + operationMsg + "':");
        TASK_MGR.getTasks().forEach(System.out::println);
    }
}
