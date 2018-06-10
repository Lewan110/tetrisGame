import java.util.Arrays;
import java.util.prefs.Preferences;


public class Wyniki {

    public static final int dlugosc_tablica_wynikow = 7;      //max długość tablicy najlepszych graczy
    private Gracz[] graczs;
    
    Preferences userPreferences;

    public Wyniki() {
        userPreferences = Preferences.userRoot().node("/tetris/asd2");           //tutaj znajdują się wyniki

        graczs =new Gracz[dlugosc_tablica_wynikow];
        for (int i = 0; i< graczs.length; ++i) {
            graczs[i]=new Gracz();
        }
    }
    
    public Gracz[] getTopGamers(){
        for (int i = 0; i < dlugosc_tablica_wynikow; i++) {
            graczs[i].setScore( userPreferences.getInt("GAMER_"+(i+1) +"_SCORE", 0));
            graczs[i].setName( userPreferences.get("GAMER_"+(i+1) +"_NAME", ""));
            graczs[i].setLevel( userPreferences.getInt("GAMER_"+(i+1) +"_LEVEL", 0));
        }
        
        Arrays.sort(graczs);
        return graczs;
    }
    
    public  int znajdzPozycjeGraczaWTabeli(int score){
        Gracz[] graczs = getTopGamers();
        for (int i = 0; i < graczs.length; i++) {
            if (graczs[i].getScore()<=score) {
                return i;
            }
        }
        return -1;
    }
    
    
    public void sortujGraczy(){
        Arrays.sort(graczs);
        for (int i = 1; i <= dlugosc_tablica_wynikow; i++) {
            userPreferences.putInt("GAMER_"+i+"_SCORE", graczs[i-1].getScore());
            userPreferences.put("GAMER_"+i+"_NAME", graczs[i-1].getName());
            userPreferences.putInt("GAMER_"+i+"_LEVEL", graczs[i-1].getLevel());
        }       
   }

    public Gracz[] getGraczs() {
        return graczs;
    }

    public void setGraczs(Gracz[] graczs) {
        this.graczs = graczs;
    }
    
}
