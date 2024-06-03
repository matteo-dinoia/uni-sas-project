package catering.businesslogic.turns;

import catering.persistence.PersistenceManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Turn {
    private int id;
    private Date date;
    private boolean isKitchenTurn;

    private Turn() {}
    private Turn(int turnId, Date date, boolean isKitchenTurn){
        // Creation of new turn is yet to be allowed
        // This is part of another UC
        this.id = turnId;
        this.date = date;
        this.isKitchenTurn = isKitchenTurn;
    }

    public int getId(){ return id; }

    @Override public String toString() {
        return (isKitchenTurn ? "kitchen" : "service")
                + " turn " + this.id;
    }
    public String formatted() {
        return this + " at " + this.date;
    }


    public static Turn getTurnByID(int turnId) {
        Turn res = new Turn();
        String query = "SELECT * FROM catering.Turns WHERE id = " + turnId;

        PersistenceManager.executeQuery(query, (rs) -> {
            res.id = rs.getInt("id");
            res.date = rs.getDate("date");
            res.isKitchenTurn = rs.getBoolean("is_kitchen_related");
        });

        return res;
    }

    public static List<Turn> getAllTurns() {
        List<Turn> res = new ArrayList<>();
        String query = "SELECT * FROM catering.Turns";

        PersistenceManager.executeQuery(query, (rs) -> {
            final int id = rs.getInt("id");
            final Date date = rs.getDate("date");
            final boolean isKitchenTurn = rs.getBoolean("is_kitchen_related");

            res.add(new Turn(id, date, isKitchenTurn));
        });

        return res;
    }

    public Date getDate() {
        return date;
    }


    public boolean getIsKitchenTurn() {
        return isKitchenTurn;
    }
}
