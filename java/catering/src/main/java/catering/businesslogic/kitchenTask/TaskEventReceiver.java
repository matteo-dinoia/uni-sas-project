package catering.businesslogic.kitchenTask;


public interface TaskEventReceiver {
    void updateTaskCreated(Task task);
    void updateTaskDeleted(Task task);

    void updateTaskChanged(Task task);
}
