package catering.persistence;

import catering.businesslogic.kitchenTask.Task;
import catering.businesslogic.kitchenTask.TaskEventReceiver;

public class TaskPersistence implements TaskEventReceiver {

    @Override public void updateTaskCreated(Task task) { task.saveNewTask(); }

    @Override public void updateTaskDeleted(Task task) { task.deleteTask(); }

    @Override public void updateTaskChanged(Task task) { task.saveTask(); }
}
