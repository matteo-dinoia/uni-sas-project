package catering.businesslogic.kitchenTask;

import catering.businesslogic.event.ServiceInfo;
import catering.businesslogic.recipe.Recipe;
import catering.businesslogic.turns.Turn;
import catering.persistence.BatchUpdateHandler;
import catering.persistence.PersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Task {
    private int id;
    private final Recipe recipe;
    private Date expiration;
    private int durationMin;
    private int quantity;
    private boolean completed;

    private List<Turn> turns = new ArrayList<>();
    private List<ServiceInfo> services = new ArrayList<>();


    private Task(Recipe recipe){
        this.recipe = recipe;
    }
    public Task(Recipe recipe, ServiceInfo service){
        this.recipe = recipe;
        addService(service);
        service.addTask(this);
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    public void addService(ServiceInfo service){
        this.services.add(service);
    }
    public void setQuantity(int quantity) { this.quantity = quantity; }


    @Override public String toString() {
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


    public static ObservableList<Task> getAllTasks() {
        return FXCollections.observableArrayList(loadAllTasks());
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


    public void destroy() {
        for(ServiceInfo serv : this.services)
            serv.removeTask(this);

        // TODO are useful? and task.getTurns();
        turns = null;
        services = null;
    }


    public void saveNewTask() {
        String taskInsert = "INSERT INTO catering.Tasks (recipe_id) VALUES (" + this.recipe.getId() + ");";
        PersistenceManager.executeUpdate(taskInsert);
        this.id = PersistenceManager.getLastId();

        saveConnectionsServices();
        saveConnectionsTurns();
    }

    public void saveTask() {
        //TODO SAVE TASK

        saveConnectionsServices();
        saveConnectionsTurns();
    }

    private void saveConnectionsServices(){
        var services = this.services;
        String taskServiceInsert = "INSERT IGNORE INTO catering.TaskService (task_id, service_id) " +
                "VALUES (" + this.id + ", ?);";

        PersistenceManager.executeBatchUpdate(taskServiceInsert, this.services.size(), new BatchUpdateHandler() {
            @Override public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, services.get(batchCount).getId());
            }

            @Override public void handleGeneratedIds(ResultSet rs, int count) {}
        });
    }

    private void saveConnectionsTurns(){
        /* TODO
        String taskTurnInsert = "DELETE FROM catering.TaskTurn WHERE task_id=" + this.id + ";";
        PersistenceManager.executeUpdate(taskTurnDelete);
         */
    }

    public void deleteTask() {
        String taskDelete = "DELETE FROM catering.Tasks WHERE id=" + this.id + ";";
        PersistenceManager.executeUpdate(taskDelete);

        String taskServiceDelete = "DELETE FROM catering.TaskService WHERE task_id=" + this.id + ";";
        PersistenceManager.executeUpdate(taskServiceDelete);

        String taskTurnDelete = "DELETE FROM catering.TaskTurn WHERE task_id=" + this.id + ";";
        PersistenceManager.executeUpdate(taskTurnDelete);
    }
}
