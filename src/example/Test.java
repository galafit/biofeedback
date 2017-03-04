package example;

import javax.swing.*;

/**
 * Created by gala on 04/02/17.
 */
public class Test {
    public static void main(String[] args) {
        Object[] items1 = {"раз", "два", "три"};
        Object[] items2 = {"one", "two", "three"};
// создаем фасад-адаптер к доменным данным,
// который просто интерпретирует их нужным образом
        ListModel model = new AbstractListModel() {
            public int getSize() { return items1.length + items2.length; }
            public Object getElementAt(int index) {return index < items1.length ? items1[index] : items2[index - items1.length] ; }
        };
// передаем созданный фасад списку в качестве модели
        JList list = new JList(model);

       JFrame  frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(list);
        frame.pack();
        frame.setVisible(true);

    }
}
