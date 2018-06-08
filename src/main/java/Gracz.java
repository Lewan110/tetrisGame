
public class Gracz implements Comparable{
    String name;
    int score;
    int level;



    @Override
    public int compareTo(Object o) {
        if (o instanceof Gracz) {
            return ((Gracz)o).score-this.score;
        }
        else return -1;
    }

    public Gracz() {}

    public Gracz(String name, int score, int level) {
        this.name = name;
        this.score = score;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }


    
    
}
