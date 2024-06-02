package catering.businesslogic.kitchenTask;

import catering.businesslogic.UseCaseLogicException;
import catering.businesslogic.event.ServiceInfo;
import catering.businesslogic.recipe.Recipe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

    public void getTabelloneTurni(){
        //TODO DO NOTHING
    }
    public Task createNewTask(Recipe recipe) throws UseCaseLogicException{
        if(service == null)
            throw new UseCaseLogicException("No service declared");

        Task newTask = new Task(recipe, service);
        eventReceiver.updateTaskCreated(newTask);

        return newTask;
    }

    public void editTask(Task task /*cuoco?: Cuoco, scadenza: data, stimaTempo: durata, turni: insieme di Turni, quantità: numero*/){
        //TODO change stuff
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


    public ObservableList<Task> getTasks() {
        return FXCollections.unmodifiableObservableList(Task.getAllTasks());
    }
}
