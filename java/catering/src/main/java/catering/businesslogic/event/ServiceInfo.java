package catering.businesslogic.event;

import catering.businesslogic.kitchenTask.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import catering.persistence.PersistenceManager;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class ServiceInfo implements EventItemInfo {
    private int id;
    private String name;
    private Date date;
    private Time timeStart;
    private Time timeEnd;
    private int participants;
    private List<Task> tasks = new ArrayList<>();

    public ServiceInfo(String name) {
        this.name = name;
    }

    @Override public String toString(){ return name; }
    public String formatted() {
        return name + ": " + date + " (" + timeStart + "-" + timeEnd + "), " + participants + " pp.";
    }

    public void addTask(Task t){ tasks.add(t); }

    public void removeTask(Task t){ tasks.remove(t); }

    // STATIC METHODS FOR PERSISTENCE

    public static ObservableList<ServiceInfo> loadServiceInfoForEvent(int eventId) {
        ObservableList<ServiceInfo> result = FXCollections.observableArrayList();
        String query = "SELECT id, name, service_date, time_start, time_end, expected_participants " +
                "FROM Services WHERE event_id = " + eventId;
        PersistenceManager.executeQuery(query, (rs) -> {
            ServiceInfo serv = new ServiceInfo(rs.getString("name"));
            serv.id = rs.getInt("id");
            serv.date = rs.getDate("service_date");
            serv.timeStart = rs.getTime("time_start");
            serv.timeEnd = rs.getTime("time_end");
            serv.participants = rs.getInt("expected_participants");
            result.add(serv);
        });

        return result;
    }

    public static ServiceInfo loadServiceByID(int serviceId) {
        ServiceInfo res = new ServiceInfo("");
        String query = "SELECT * FROM Services WHERE id = " + serviceId;

        PersistenceManager.executeQuery(query, (rs) -> {
            res.name = rs.getString("name");
            res.id = rs.getInt("id");
            res.date = rs.getDate("service_date");
            res.timeStart = rs.getTime("time_start");
            res.timeEnd = rs.getTime("time_end");
            res.participants = rs.getInt("expected_participants");
        });

        return res;
    }

    public int getId() {
        return id;
    }
}
