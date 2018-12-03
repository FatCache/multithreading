package akkaHW2017F;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.pattern.Patterns;
import akka.util.Timeout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

/**
 * this actor reads the file, counts the vowels and sends the result to
 * Estimator.
 *
 * @author abdusamed
 *
 */
public class FirstCounter extends UntypedActor {


    public FirstCounter() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onReceive(Object msg) throws Throwable {
        if (msg instanceof String) {
            String[] msgList = ((String) msg).split("&=");
            if (msgList[0].equals(Messages.START)) {
                String pt = computePt(msgList[1]); //the STRING...
                String ct = "0.0";
                Future<Object> f = Patterns.ask(getSender(), Messages.GET_CT+"&="+"Plzz no printo", 5000); // NEED CT

                try {
                    Timeout timeout = new Timeout(Duration.create(100, "seconds"));
                    ct = (String) Await.result(f, timeout.duration());
                    System.out.printf("Value of C(T) is %s from Estimator...\n", ct);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println("Value of U(t) is " + computeUt(pt, ct));

                // Send Feedback BACK to Estimator
                String feedbackMessage = Messages.POST_FEEDBACK + "&=" + computeUt(pt, ct);
                getSender().tell(feedbackMessage, getSelf()); //send back to Estimator
                User.usNode.tell(Messages.RETURN_ERROR + "&=" + computeUt(pt,ct), getSelf()); // Tell User of U(t)
                System.out.printf("%s Reaced the END....\n",getName());
            } else {
                throw new IllegalAccessException("No Match Found");
            }

        }

    }

    public String computeUt(String pt, String ct) {
        double ut = Double.parseDouble(pt) - Double.parseDouble(ct);
        return Double.toString(ut);
    }

    public String computePt(String in) {
        double vt = vowelCounter(in);
        double lt = letterCounter(in);
        System.out.printf("%s v(t):%f l(t)%f\n",getName(),vt,lt);
        return Double.toString(vt / lt);
    }

    public void businessLogic() {

    }

    public int vowelCounter(String in) {
        int count = 0;
//        try {
//            FileReader fr = new FileReader(System.getProperty("user.dir") + File.separator + "data" + File.separator + "Akka1.txt");
//            BufferedReader br = new BufferedReader(fr);
//            while (br.readLine() != null) {
//                count += vowelCount(br.readLine());
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }

        return vowelCount(in);

    }

    public int letterCounter(final String in) {
//        int count = 0;
//        try {
//            FileReader fr = new FileReader(System.getProperty("user.dir") + File.separator + "data" + File.separator + "Akka1.txt");
//            BufferedReader br = new BufferedReader(fr);
//            while (br.readLine() != null) {
//                count += letterCount(br.readLine());
//            }
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
        return letterCount(in);
    }

    public int letterCount(final String in) {
        String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        HashSet<Character> vs = new HashSet<>();
        for (Character c : alpha.toCharArray()) {
            vs.add(c);
        }

        int count = 0;
        for (char c : in.toUpperCase().toCharArray()) {
            if (vs.contains(c)) {
                count++;
            }
        }
        return count;
    }

    public int vowelCount(final String in) {
        HashSet<Character> vs = new HashSet<Character>(Arrays.asList('A', 'E', 'I', 'O','U', 'Y'));
        int count = 0;
        for (char c : in.toUpperCase().toCharArray()) {
            if (vs.contains(c)) {
                count++;
            }
        }
        return count;
    }
    
        private String getName(){
        return getSelf().path().name()+">";
    }


}
