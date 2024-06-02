package catering.persistence.handler;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PreparedStatementHandler {
    void setParameters(PreparedStatement ps) throws SQLException;
}
