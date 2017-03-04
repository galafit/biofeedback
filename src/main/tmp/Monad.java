package main.tmp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Created by hdablin on 03.03.17.
 */
public class Monad {


    public static void main(String[] args) {
        ArrayList<Integer> l1 = new ArrayList();
        ArrayList<Integer> l2 = new ArrayList();

        for (int i = 1; i < 10; i++) {
            l1.add(i);
            l2.add(10-i);
        }

        Stream<List<Integer>> la = l1.stream().map(x -> Arrays.asList(x,x+1));


        Stream<Integer> lf = la.flatMap(x -> x.stream());
        lf.forEach(x -> System.out.println(x));

      /*  Stream<Integer> l3 = l1.stream().flatMap((x) -> x + 1);

        l3.forEach(x -> System.out.println(x)); */


    }
}
