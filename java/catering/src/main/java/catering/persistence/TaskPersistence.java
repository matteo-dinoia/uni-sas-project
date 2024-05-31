package catering.persistence;

import catering.businesslogic.kitchenTask.Task;
import catering.businesslogic.kitchenTask.TaskEventReceiver;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskPersistence implements TaskEventReceiver {

    @Override public void createTask(Task task) {

        String taskInsert = "INSERT INTO catering.Tasks (recipe_id) VALUES (?);";

        PersistenceManager.executeBatchUpdate(taskInsert, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                // TODO replace 0
                ps.setInt(1, 0);
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                // should be only one
                if (count == 0) {
                    task.setId(rs.getInt(1));
                }
            }
        });
    }
}
