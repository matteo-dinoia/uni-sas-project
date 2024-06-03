package catering;

import catering.businesslogic.CatERing;
import catering.businesslogic.UseCaseLogicException;
import catering.businesslogic.event.ServiceInfo;
import catering.businesslogic.kitchenTask.Task;
import catering.businesslogic.kitchenTask.TaskManager;
import catering.businesslogic.recipe.Recipe;
import catering.businesslogic.turns.Turn;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestTask5 {
    public static final CatERing INSTANCE = CatERing.getInstance();
    public static final TaskManager TASK_MGR = INSTANCE.getTaskManager();

    public static void main(String[] args) {
        Task newTask;
        try {
            INSTANCE.getUserManager().fakeLogin("Lidia");
            System.out.println(INSTANCE.getUserManager().getCurrentUser());

            ServiceInfo service = ServiceInfo.loadServiceByID(1);
            TASK_MGR.openService(service);
            printTasks("doing nothing");

            Recipe randomRecipe = INSTANCE.getRecipeManager().getRecipes().get(0);
            newTask = TASK_MGR.createNewTask(randomRecipe);
            printTasks("adding a task");

            TASK_MGR.deleteTask(newTask);
            printTasks("deleting the task");

            TASK_MGR.deleteTask(newTask);
            printTasks("deleting the same task again");
        } catch (UseCaseLogicException ex) {
            System.out.println("Errore di logica nello use case: " + ex.getMessage());
        }
    }

    public static void printTasks(String operationMsg){
        System.out.println("\nDATA FROM SERVER AFTER '" + operationMsg + "':");
        TASK_MGR.getTasks().forEach(task -> System.out.println(task.formatted()));
    }
}
