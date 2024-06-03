package catering.persistence;

import catering.businesslogic.kitchenTask.Task;
import catering.businesslogic.kitchenTask.TaskEventReceiver;
import catering.businesslogic.turns.Turn;

import java.util.List;

public class TaskPersistence implements TaskEventReceiver {

    @Override public void updateTaskCreated(Task task) { task.saveNewTask(); }

    @Override public void updateTaskDeleted(Task task) { task.deleteTask(); }

    @Override public void updateTaskChanged(Task task) { task.saveTask(); }

    @Override public void updateTaskTurnsRemoved(Task task, List<Turn> oldTurns) {
        task.saveTurnDeletion(oldTurns);
    }
}
