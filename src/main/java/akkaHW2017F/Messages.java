package akkaHW2017F;

/**
 * Messages that are passed around the actors are usually immutable classes.
 * Think how you go about creating immutable classes:) Make them all static
 * classes inside the Messages class.
 * 
 * This class should have all the immutable messages that you need to pass
 * around actors. You are free to add more classes(Messages) that you think is
 * necessary
 * 
 * @author abdusamed
 *
 */
public class Messages {
    public static final String START = "Start";
    public static final String STOP = "Stop";
    public static final String COUNT = "Count";
    public static final String FEEDBACK = "Feedback";
    public static final String CT = "Ct";
    
    // Estimator => Counter
    public static final String GET_CT = "getCt";
    
    // Counter => Estimator
    public static final String POST_FEEDBACK = "postfeedback";
    public static final String ADJUST_CT = "adjustct";
    
    // Counter => User
    public static final String RETURN_ERROR = "returnError";

}