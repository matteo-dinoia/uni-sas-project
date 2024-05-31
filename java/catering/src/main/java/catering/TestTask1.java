package catering;

import catering.businesslogic.CatERing;
import catering.businesslogic.UseCaseLogicException;
import catering.businesslogic.event.ServiceInfo;
import catering.businesslogic.menu.Menu;
import catering.businesslogic.menu.MenuItem;
import catering.businesslogic.menu.Section;
import catering.businesslogic.recipe.Recipe;
import javafx.collections.ObservableList;

public class TestTask1 {
    public static void main(String[] args) {
        try {
            /* System.out.println("TEST DATABASE CONNECTION");
            PersistenceManager.testSQLConnection();*/
            CatERing.getInstance().getUserManager().fakeLogin("Lidia");
            System.out.println(CatERing.getInstance().getUserManager().getCurrentUser());

            final var TASK_MGR = CatERing.getInstance().getTaskManager();

            ServiceInfo service = CatERing.getInstance().getEventManager().getEventInfo().get(0).getServices().get(0);

            TASK_MGR.openService(service);
            Recipe randomRecipe = CatERing.getInstance().getRecipeManager().getRecipes().get(0);
            TASK_MGR.createNewTask(randomRecipe);

            System.out.println(TASK_MGR.getTasks());

            //TODO delete
            //System.out.println(TASK_MGR.getTasks());

        } catch (UseCaseLogicException ex) {
            System.out.println("Errore di logica nello use case: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
