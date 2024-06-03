package catering.businesslogic.kitchenTask;

import catering.businesslogic.event.ServiceInfo;
import catering.businesslogic.recipe.Recipe;
import catering.businesslogic.turns.Turn;
import catering.businesslogic.user.User;
import catering.persistence.handler.BatchUpdateHandler;
import catering.persistence.PersistenceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Task {
    private int id;
    private final Recipe recipe;
    private Date expiration;
    private Integer durationMin;
    private Integer quantity;
    private boolean completed;
    private User assignedCook;

    private List<Turn> turns = new ArrayList<>();
    private final List<ServiceInfo> services = new ArrayList<>();


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
    public void setExpiration(Date expiration) { this.expiration = expiration; }
    public void setDuration(Integer minutes) { this.durationMin = minutes; }
    public void setAssignedCook(User cook) { this.assignedCook = cook; }
    public void setTurns(List<Turn> turns) { this.turns = turns; }

    public List<Turn> getTurns() {
        return new ArrayList<>(turns);
    }


    public String toString(){ return this.id + " of recipe " + this.recipe; }

    public String formatted(){
        return "Task{" +
                "id=" + id +
                ", recipe=" + recipe +
                ", cook=" + (assignedCook != null ? assignedCook.getUserName() : null) +
                ", expiration=" + expiration +
                ", durationMin=" + durationMin +
                ", quantity=" + quantity +
                ", completed=" + completed +
                ", turns=" + turns +
                ", services=" + services +
                '}';
    }


    public void destroy() {
        for(ServiceInfo serv : this.services)
            serv.removeTask(this);
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

            task.assignedCook = User.loadUserById(rs.getInt("cook_id"));

            res.add(task);
            task.loadTaskServices();
            task.loadTurnServices();
        });
        return res;
    }

    private void loadTaskServices(){
        final var thisTask = this;
        String query = "SELECT * FROM catering.TaskService WHERE task_id = " + this.id;

        ObservableList<Task> res = FXCollections.observableArrayList();
        PersistenceManager.executeQuery(query, (rs) -> thisTask.services.add(ServiceInfo.loadServiceByID(rs.getInt("service_id"))));
    }

    private void loadTurnServices(){
        final var thisTask = this;
        String query = "SELECT * FROM catering.TaskTurn WHERE task_id = " + this.id;

        ObservableList<Task> res = FXCollections.observableArrayList();
        PersistenceManager.executeQuery(query, (rs) -> thisTask.turns.add(Turn.getTurnByID(rs.getInt("turn_id"))));
    }

    public void saveNewTask() {
        String taskInsert = "INSERT INTO catering.Tasks (recipe_id) VALUES (" + this.recipe.getId() + ");";
        PersistenceManager.executeUpdate(taskInsert);
        this.id = PersistenceManager.getLastId();

        saveConnectionsServices();
        saveConnectionsTurns();
    }

    public void saveTask() {
        final var thisTask = this;
        String taskUpdate = "UPDATE catering.Tasks SET " +
                " expiration = ?, quantity = ?, duration_min = ?, completed = ?, cook_id = ? " +
                " WHERE id = ?;";

        PersistenceManager.executeUpdate(taskUpdate, (ps) -> {
            ps.setDate(1, PersistenceManager.getSqlDate(thisTask.expiration));
            if(thisTask.quantity != null)
                ps.setInt(2, thisTask.quantity);
            else ps.setNull(2, Types.INTEGER);
            if(thisTask.durationMin != null)
                ps.setInt(3, thisTask.durationMin);
            else ps.setNull(3, Types.INTEGER);
            ps.setBoolean(4, completed);
            if(thisTask.assignedCook != null)
                ps.setInt(5, thisTask.assignedCook.getId());
            else ps.setNull(5, Types.INTEGER);
            ps.setInt(6, id);
        });

        saveConnectionsServices();
        saveConnectionsTurns();
    }

    private void saveConnectionsServices(){
        final var services = this.services;
        String taskServiceInsert = "INSERT IGNORE INTO catering.TaskService (task_id, service_id) " +
                "VALUES (" + this.id + ", ?);";

        PersistenceManager.executeBatchUpdate(taskServiceInsert, services.size(), new BatchUpdateHandler() {
            @Override public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, services.get(batchCount).getId());
            }

            @Override public void handleGeneratedIds(ResultSet rs, int count) {}
        });
    }

    private void saveConnectionsTurns(){
        final var turns = this.turns;
        String taskServiceInsert = "INSERT IGNORE INTO catering.TaskTurn (task_id, turn_id) " +
                "VALUES (" + this.id + ", ?);";

        PersistenceManager.executeBatchUpdate(taskServiceInsert, turns.size(), new BatchUpdateHandler() {
            @Override public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, turns.get(batchCount).getId());
            }

            @Override public void handleGeneratedIds(ResultSet rs, int count) {}
        });
    }

    public void deleteTask() {
        String taskDelete = "DELETE FROM catering.Tasks WHERE id=" + this.id + ";";
        PersistenceManager.executeUpdate(taskDelete);

        String taskServiceDelete = "DELETE FROM catering.TaskService WHERE task_id=" + this.id + ";";
        PersistenceManager.executeUpdate(taskServiceDelete);

        String taskTurnDelete = "DELETE FROM catering.TaskTurn WHERE task_id=" + this.id + ";";
        PersistenceManager.executeUpdate(taskTurnDelete);
    }

    public void saveTurnDeletion(List<Turn> oldTurns) {
        String turnsDelete = "DELETE FROM catering.TaskTurn WHERE task_id =" + this.id + " AND turn_id =?;";
        PersistenceManager.executeBatchUpdate(turnsDelete, oldTurns.size(), new BatchUpdateHandler() {
            @Override public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, oldTurns.get(batchCount).getId());
            }

            @Override public void handleGeneratedIds(ResultSet rs, int count){}
        });
    }




}
