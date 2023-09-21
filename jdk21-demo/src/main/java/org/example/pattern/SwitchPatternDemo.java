package org.example.pattern;

import org.example.model.Circle;
import org.example.model.Graph;
import org.example.model.Square;

/**
 * @author hzhq
 * @version 1.0
 * @since 2023/9/21 上午2:14
 */
public class SwitchPatternDemo {

    private static double getPerimeter(Graph graph){
        return switch (graph){
            case Square(int x, int y)-> x*2 + y *2;
            case Circle(int r) -> 2 * 3.14 * r;
        };
    }

    private static void printObject(Object object){
        switch (object){
            case null-> {}
            case String s when "a".equals(s) -> System.out.println("get string a ");
            case String s -> System.out.println("string="+s);
            case Integer i -> System.out.println("int="+i);
            default -> throw new IllegalStateException("Unexpected value: " + object);
        }
    }



    public static void main(String[] args) {
        // test record switch
        Graph square = new Square(3,4);
        Graph circle = new Circle(5);
        System.out.println("square perimeter is " + getPerimeter(square));
        System.out.printf("circle perimeter is %.2f\n", getPerimeter(circle));

        // test object switch
        Object a = "a";
        Object s = "s";
        Object i = 1;
        printObject(a);
        printObject(s);
        printObject(i);

    }
}
