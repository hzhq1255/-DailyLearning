package com.example.threadDemo.thread;

/**
 * @author hzhq1255
 * @version 1.0
 * @since 2022-08-03 12:31 AM
 */
public class LiftOff implements Runnable{

    protected int countDown = 10;

    private static int taskCount = 0;

    private final int id = taskCount++;

    public LiftOff() {
    }

    public String status(){
        return "#" + id + "(" + (countDown > 0 ? countDown: "LiftOff!") + "),";
    }


    @Override
    public void run() {
        while (countDown-- > 0){
            System.out.print(status());
            Thread.yield();
        }
    }

    public static void main(String[] args) {
//        LiftOff liftOff = new LiftOff();
//        liftOff.run();
        for (int i = 0; i < 5; i++){
            Thread thread =  new Thread(new LiftOff());
            thread.start();
            System.out.println();
        }
        System.out.println("waiting for lift off");

    }
}
