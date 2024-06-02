package catering.persistence;

import catering.businesslogic.kitchenTask.Task;
import catering.businesslogic.kitchenTask.TaskEventReceiver;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskPersistence implements TaskEventReceiver {

    @Override public void updateTaskCreated(Task task) { task.saveNewTask(); }

    @Override public void updateTaskDeleted(Task task) { task.deleteTask(); }

    @Override public void updateTaskChanged(Task task) { task.saveTask(); }
}
