package catering.businesslogic.kitchenTask;

import catering.businesslogic.UseCaseLogicException;
import catering.businesslogic.event.ServiceInfo;
import catering.businesslogic.recipe.Recipe;
import catering.businesslogic.turns.Turn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import catering.businesslogic.user.User;

public class TaskManager {
    private TaskEventReceiver eventReceiver;
    private ServiceInfo service;

    public void setEventReceiver(TaskEventReceiver eventReceiver){
        this.eventReceiver = eventReceiver;
    }


    public void openService(ServiceInfo service) {
        this.service = service;
        //TODO controllo user magari
    }

    public ObservableList<Turn> getTurns(){
        Date now = new Date();
        Stream<Turn> turns = Turn.getAllTurns().stream().filter(turn -> turn.getDate().after(now));

        ObservableList<Turn> turnsObs = FXCollections.observableArrayList();
        turns.forEach(turnsObs::add);
        return FXCollections.unmodifiableObservableList(turnsObs);
    }

    public ObservableList<Task> getTasks() {
        return FXCollections.unmodifiableObservableList(Task.getAllTasks());
    }

    public Task createNewTask(Recipe recipe) throws UseCaseLogicException{
        if(service == null)
            throw new UseCaseLogicException("No service declared");

        Task newTask = new Task(recipe, service);
        eventReceiver.updateTaskCreated(newTask);

        return newTask;
    }

    public void editTask(Task task, User cook, Date expiration, Integer durationMin, List<Turn> turns, Integer quantity){
        if(expiration != null) task.setExpiration(expiration);
        if(durationMin != null) task.setDuration(durationMin);
        if(quantity != null) task.setQuantity(quantity);

        if(cook != null && cook.isCook())
            task.setAssignedCook(cook);

        if(turns != null)
            task.setTurns(turns);

        //TODO Split this function (togli da qua)
        eventReceiver.updateTaskChanged(task);
    }

    public void useTaskAlsoForOtherService(Task task, ServiceInfo serviceToAddTaskTo, Integer newQuantity) {
        if(newQuantity != null)
            task.setQuantity(newQuantity);

        task.addService(serviceToAddTaskTo);
        serviceToAddTaskTo.addTask(task);

        eventReceiver.updateTaskChanged(task);
    }

    public void deleteTask(Task task){
        task.destroy();
        eventReceiver.updateTaskDeleted(task);
    }

    public void markTaskAsAlreadyCompleted(Task task, boolean completed){
        task.setCompleted(completed);
        eventReceiver.updateTaskChanged(task);
    }



}
