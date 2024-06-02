package catering.businesslogic.kitchenTask;

import catering.businesslogic.event.ServiceInfo;
import catering.businesslogic.recipe.Recipe;
import catering.businesslogic.turns.Turn;
import catering.persistence.PersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class Task {
    private int id;
    private final Recipe recipe;
    private Date expiration;
    private int durationMin;
    private int quantity;
    private boolean completed;

    private List<Turn> turns = new ArrayList<>();
    private List<ServiceInfo> services = new ArrayList<>();

    public Task(Recipe recipe, ServiceInfo service){
        this.recipe = recipe;
        addService(service);
    }

    private Task(Recipe recipe){
        this.recipe = recipe;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void addService(ServiceInfo service){
        this.services.add(service);
    }

    public void setId(int id) {
        this.id = id;
    }

    private static ObservableList<Task> loadAllTasks() {
        String query = "SELECT * FROM catering.Tasks";

        ObservableList<Task> res = FXCollections.observableArrayList();
        PersistenceManager.executeQuery(query, (rs) -> {
            int id = rs.getInt("id");
            int recipeId = rs.getInt("recipe_id");
            Recipe recipe = Recipe.loadRecipeById(recipeId);

            Task task = new Task(recipe);

            task.id = id;
            task.expiration = rs.getDate("expiration");
            task.quantity = rs.getInt("quantity");
            task.durationMin = rs.getInt("duration_min");
            task.completed = rs.getBoolean("completed");

            res.add(task);
        });
        return res;
    }

    public static ObservableList<Task> getAllTasks() {
        return FXCollections.observableArrayList(loadAllTasks());
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", recipe=" + recipe +
                ", expiration=" + expiration +
                ", durationMin=" + durationMin +
                ", quantity=" + quantity +
                ", completed=" + completed +
                ", turns=" + turns +
                ", services=" + services +
                '}';
    }
}
