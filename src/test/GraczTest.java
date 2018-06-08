import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GraczTest {




    @Test
    public void compareTo() {
        Gracz g1=new Gracz("g1",100,1);
        Gracz g2=new Gracz("g2",1000,1);
        String s = new String("dd");

        assertEquals(-900,g2.compareTo(g1));
        assertEquals(900,g1.compareTo(g2));
        assertEquals(-1,g2.compareTo(s));
    }
}