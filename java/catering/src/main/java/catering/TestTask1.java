package catering;

import catering.businesslogic.CatERing;
import catering.businesslogic.UseCaseLogicException;
import catering.businesslogic.event.ServiceInfo;
import catering.businesslogic.kitchenTask.Task;
import catering.businesslogic.kitchenTask.TaskManager;
import catering.businesslogic.recipe.Recipe;

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
            printTasks();

            Recipe randomRecipe = INSTANCE.getRecipeManager().getRecipes().get(0);
            Task newTask = TASK_MGR.createNewTask(randomRecipe);
            printTasks();

            TASK_MGR.deleteTask(newTask);
            printTasks();

        } catch (UseCaseLogicException ex) {
            System.out.println("Errore di logica nello use case: " + ex.getMessage());
        }
    }

    public static void printTasks(){
        System.out.println();
        TASK_MGR.getTasks().forEach(System.out::println);
    }
}
