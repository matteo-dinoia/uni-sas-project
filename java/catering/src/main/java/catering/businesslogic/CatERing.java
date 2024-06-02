package catering.businesslogic;

import catering.businesslogic.event.EventManager;
import catering.businesslogic.kitchenTask.TaskManager;
import catering.businesslogic.menu.MenuManager;
import catering.businesslogic.recipe.RecipeManager;
import catering.businesslogic.user.UserManager;
import catering.persistence.MenuPersistence;
import catering.persistence.TaskPersistence;

public class CatERing {
    private static CatERing singleInstance;

    public static CatERing getInstance() {
        if (singleInstance == null) {
            singleInstance = new CatERing();
        }
        return singleInstance;
    }

    private final MenuManager menuMgr;
    private final RecipeManager recipeMgr;
    private final UserManager userMgr;
    private final TaskManager taskMgr;
    private final EventManager eventMgr;

    private CatERing() {
        menuMgr = new MenuManager();
        recipeMgr = new RecipeManager();
        userMgr = new UserManager();
        taskMgr = new TaskManager();
        eventMgr = new EventManager();

        menuMgr.setEventReceiver(new MenuPersistence());
        taskMgr.setEventReceiver(new TaskPersistence());
    }


    public MenuManager getMenuManager() {
        return menuMgr;
    }

    public RecipeManager getRecipeManager() {
        return recipeMgr;
    }

    public UserManager getUserManager() {
        return userMgr;
    }

    public EventManager getEventManager() { return eventMgr; }

    public TaskManager getTaskManager() { return taskMgr; }
}
