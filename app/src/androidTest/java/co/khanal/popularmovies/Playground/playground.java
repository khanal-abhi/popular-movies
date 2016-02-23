package co.khanal.popularmovies.Playground;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhi on 2/22/16.
 */
public class playground extends TestCase{

    public void testPersistenceOfLists(){

        List<Dummy> dummies = new ArrayList<>();
        dummies.add(new Dummy(1,2));
        dummies.add(new Dummy(3,4));

        for(Dummy dummy : dummies){
            dummy.x++;
        }

        assertEquals(2, dummies.get(0).x);
        assertEquals(4, dummies.get(1).x);

    }

    public class Dummy{
        public int x;
        public int y;

        public Dummy(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
}
