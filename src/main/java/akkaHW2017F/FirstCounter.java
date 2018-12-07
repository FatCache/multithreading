package akkaHW2017F;

import akka.actor.UntypedActor;
import akka.pattern.Patterns;
import akka.util.Timeout;
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
public class FirstCounter extends UntypedActor implements Message {

    @Override
    public void onReceive(Object msg) throws Throwable {
        if (msg instanceof String) {
            String[] msgList = ((String) msg).split("&=");
            if (msgList[0].equals(START)) {
                final String pt = computePt(msgList[1]); //the STRING...
                String ct = "0.0";
                Future<Object> f = Patterns.ask(getSender(), GET_CT + "&=", 5000); // Get CT

                try {
                    Timeout timeout = new Timeout(Duration.create(100, "seconds"));
                    ct = (String) Await.result(f, timeout.duration());
                    System.out.printf("Value of C(T) is %s from Estimator...\n", ct);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println(getName() + "Value of U(t) is " + computeUt(pt, ct));

                // Send Feedback BACK to Estimator
                String feedbackMessage = POST_FEEDBACK + "&=" + computeUt(pt, ct);
                getSender().tell(feedbackMessage, getSelf()); //send FeedBack to Estimator
                User.usNode.tell(RETURN_ERROR + "&=" + computeUt(pt, ct), getSelf()); // Tell User of U(t)

            } else {
                throw new IllegalAccessException("No Match Found");
            }

        }

    }

    private String computeUt(final String pt, final String ct) {
        final double ut = Double.parseDouble(pt) - Double.parseDouble(ct);
        return Double.toString(ut);
    }

    private String computePt(final String in) {
        double vt = vowelCounter(in);
        double lt = letterCounter(in);
        System.out.printf("%s v(t):%f l(t)%f => %f\n", getName(), vt, lt, vt / lt);
        return Double.toString(vt / lt);
    }
    
    private int letterCounter(final String in) {
        return letterCount(in);
    }

    private int letterCount(final String in) {
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

    private int vowelCount(final String in) {
        HashSet<Character> vs = new HashSet<Character>(Arrays.asList('A', 'E', 'I', 'O', 'U', 'Y'));
        int count = 0;
        for (char c : in.toUpperCase().toCharArray()) {
            if (vs.contains(c)) {
                count++;
            }
        }
        return count;
    }
    
   private int vowelCounter(final String in) {
        return vowelCount(in);
    }

    @Override
    public String getName() {
        return getSelf().path().name() + ">";
    }



}
