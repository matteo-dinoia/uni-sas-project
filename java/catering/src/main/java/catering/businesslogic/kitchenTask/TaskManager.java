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
    public void createNewTask(Recipe recipe) throws UseCaseLogicException{
        if(service == null)
            throw new UseCaseLogicException("No service declared");

        Task newTask = new Task(recipe, service);
        eventReceiver.createTask(newTask);
    }

    public ObservableList<Task> getTasks() {
        return FXCollections.unmodifiableObservableList(Task.getAllTasks());
    }

    public void modificaCompito(/*compito: Compito, cuoco?: Cuoco, scadenza: data, stimaTempo: durata, turni: insieme di Turni, quantità: numero*/){

    }

    public void cumulaCompiti(/*compito: Compito, servizio: Servizio, nuovaQuantità?: numero*/) {

    }

    public void eliminaCompito(Task task){

    }

    public void markTaskAsAlreadyCompleted(Task task, boolean completed){
        task.setCompleted(completed);

        //TODO persistency
    }
}
