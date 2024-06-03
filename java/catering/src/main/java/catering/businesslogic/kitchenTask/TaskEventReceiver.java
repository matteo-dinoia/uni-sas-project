package catering.businesslogic.kitchenTask;


import catering.businesslogic.turns.Turn;

import java.util.List;

public interface TaskEventReceiver {
    void updateTaskCreated(Task task);
    void updateTaskDeleted(Task task);
    void updateTaskChanged(Task task);
    void updateTaskTurnsRemoved(Task task, List<Turn> oldTurns);
}
