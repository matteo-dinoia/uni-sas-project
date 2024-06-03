package catering;

import catering.businesslogic.CatERing;
import catering.businesslogic.UseCaseLogicException;
import catering.businesslogic.event.ServiceInfo;
import catering.businesslogic.kitchenTask.Task;
import catering.businesslogic.kitchenTask.TaskManager;
import catering.businesslogic.recipe.Recipe;
import catering.businesslogic.turns.Turn;
import catering.businesslogic.user.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestTask1 {
    public static final CatERing INSTANCE = CatERing.getInstance();
    public static final TaskManager TASK_MGR = INSTANCE.getTaskManager();

    public static void main(String[] args) {
        try {
            INSTANCE.getUserManager().fakeLogin("Lidia");
            System.out.println(INSTANCE.getUserManager().getCurrentUser());

            ServiceInfo service = ServiceInfo.loadServiceByID(1);
            TASK_MGR.openService(service);
            printTasks("doing nothing");

            Recipe randomRecipe = INSTANCE.getRecipeManager().getRecipes().get(0);
            Task newTask = TASK_MGR.createNewTask(randomRecipe);
            printTasks("adding a task");

            TASK_MGR.markTaskAsAlreadyCompleted(newTask, true);
            printTasks("marking as completed");

            List<Turn> turns = new ArrayList<>();
            turns.add(Turn.getTurnByID(1));
            turns.add(Turn.getTurnByID(2));
            TASK_MGR.editTask(newTask, User.loadUserById(4), new Date(), 120, turns,50);
            printTasks("changing cook, date, duration, quanitity");

            ServiceInfo service2 = ServiceInfo.loadServiceByID(2);
            TASK_MGR.useTaskAlsoForOtherService(newTask, service2, 100);
            printTasks("adding another service and changing quantity");

            TASK_MGR.editTask(newTask, null, null, 130, null, null);
            printTasks("changing duration");

        } catch (UseCaseLogicException ex) {
            System.out.println("Errore di logica nello use case: " + ex.getMessage());
        }
    }

    public static void printTasks(String operationMsg){
        System.out.println("\nDATA FROM SERVER AFTER '" + operationMsg + "':");
        TASK_MGR.getTasks().forEach(task -> System.out.println(task.formatted()));
    }
}
