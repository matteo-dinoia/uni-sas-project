package catering.businesslogic.turns;

public class Turn {
    private int id;

    private Turn(int turnId){
        this.id = turnId;
    }

    public static Turn getTurnByID(int turnId) {
        // Turn still doesn't exist in the database
        return new Turn(turnId);
    }

    public int getId(){
        return id;
    }

    @Override
    public String toString() {
        return "turn " + this.id;
    }
}
