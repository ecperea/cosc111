package edu.java.cosc111.samples.concurrency;


public class GreetingRunnableTest1 {
    public static void main(String[] args){
        String msg = "This message is just a test. Try to change me.";
        System.out.print("Your message: ");
        for(String s:msg.split(" ")) {
            new Thread(new GreetingRunnable(s + " ")).start();
        }
    }
}
