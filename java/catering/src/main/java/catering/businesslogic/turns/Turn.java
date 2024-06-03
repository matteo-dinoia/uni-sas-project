package catering.businesslogic.turns;

import catering.persistence.PersistenceManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Turn {
    private int id;
    private Date date;

    private Turn(int turnId, Date date){
        // Creation of new turn is yet to be allowed
        // This is part of another UC
        this.id = turnId;
        this.date = date;
    }

    public int getId(){ return id; }

    @Override public String toString() {
        return "turn " + this.id;
    }
    public String formatted() {
        return "turn" + this.id + " at " + this.date;
    }


    public static Turn getTurnByID(int turnId) {
        Turn res = new Turn(0, null);
        String query = "SELECT * FROM catering.Turns WHERE id = " + turnId;

        PersistenceManager.executeQuery(query, (rs) -> {
            res.id = rs.getInt("id");
            res.date = rs.getDate("date");
        });

        return res;
    }

    public static List<Turn> getAllTurns() {
        List<Turn> res = new ArrayList<>();
        String query = "SELECT * FROM catering.Turns";

        PersistenceManager.executeQuery(query, (rs) -> {
            final int id = rs.getInt("id");
            final Date date = rs.getDate("date");

            res.add(new Turn(id, date));
        });

        return res;
    }

    public Date getDate() {
        return date;
    }


}
