package akkaHW2017F;

import java.io.File;

import akka.actor.UntypedActor;

/**
 * This is the main actor and the only actor that is created directly under the
 * {@code ActorSystem} This actor creates more child actors
 * {@code WordCountInAFileActor} depending upon the number of files in the given
 * directory structure
 *
 * @author abdusamed
 *
 */
public class Estimator extends UntypedActor {

    private static double currentEstimator = 0.7;
    private static double oldEstimator = 0.7;
    private final double g =0.33; 

    public final static Object ctEstimatorLock = new Object();

    public Estimator() {
    }

    @Override
    public void onReceive(Object msg) throws Throwable {
        if (msg instanceof String) {
            String[] msgList = ((String) msg).split("&=");
            if (msgList[0].equals(Messages.POST_FEEDBACK)) {
                // Use feedback to adjust
                setctEstimator(Double.parseDouble(msgList[1]));
            } else if (msgList[0].equals(Messages.GET_CT)) {
                getSender().tell(Double.toString(currentEstimator), getSelf());
            } else{
                System.out.println(getName() + " No match Found in String");
            }

        } else {
            System.out.println(getName() + " Invalid Pattern");

        }
    }

    private void setctEstimator(final double in) {
        synchronized (ctEstimatorLock) {
            System.out.printf("%s Updating values from %f ...",getName(),currentEstimator);
            oldEstimator = currentEstimator; // C(T)
            currentEstimator = currentEstimator + in; // Cprime(T)
            //C'(t+1) = g * C(t) + (1 - g ) * C'(t)
            currentEstimator = g * oldEstimator + (1 - g) * currentEstimator;
            System.out.printf("to ... %f\n",currentEstimator);
        }
        
    }
    
    private String getName(){
        return getSelf().path().name()+">";
    }

}
