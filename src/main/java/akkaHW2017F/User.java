package akkaHW2017F;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Main class for your estimation actor system.
 *
 * @author abdusamed
 *
 */
public class User extends UntypedActor {

    public static ActorSystem system;
    public static ActorRef usNode;

    public static AtomicInteger nStep;
    //public static AtomicLong sSum;
    public static double sSum;

    public static void main(String[] args) throws InterruptedException {
        /*
		 * Create the Estimator Actor and send it the StartProcessingFolder
		 * message. Once you get back the response, use it to print the result.
		 * Remember, there is only one actor directly under the ActorSystem.
		 * Also, do not forget to shutdown the actorsystem
         */

        system = ActorSystem.create("EstimationSystem");
        nStep = new AtomicInteger(0);
        //sSum = new AtomicLong(0L);
        sSum = 0;

        Props userProps = Props.create(User.class);
        usNode = system.actorOf(userProps, "User_Node");
        usNode.tell(Messages.START, usNode);

        Thread.sleep(10000);
        system.terminate();

    }

    @Override
    public void onReceive(Object o) throws Throwable {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if (o instanceof String) {
            String[] msgList = ((String) o).split("&=");
            if (msgList[0].equals(Messages.RETURN_ERROR)) {
                double ut = Double.parseDouble(msgList[1]);
                
                int n = nStep.incrementAndGet();
                //long s = (long) (sSum.get() + ut);
                sSum +=ut; 
                double error = (sSum / n)*100;
                // Print Current Error 
                System.out.printf("%sCurrent Error at Time Step %d is %.2f%%\r\n",getName(), n, error);
//                System.out.println(getName()+"The current Error at Time Step "+n+" is "+error+"%");
            } else if(msgList[0].equals(Messages.START)){
                StringBuilder stringToSend = new StringBuilder();
                try {
                    for (int i = 1; i <=10; i++) {
                        FileReader fr = new FileReader(System.getProperty("user.dir") + File.separator + "data" + File.separator + "Akka" + i + ".txt");
                        BufferedReader br = new BufferedReader(fr);
                        while (br.readLine() != null) {
                            stringToSend.append(br.readLine());
                        }

                        // Send to Three Children for EACH file}
                        Props Estimator1Prop = Props.create(Estimator.class, stringToSend.toString());
                        Props Estimator2Prop = Props.create(Estimator.class, stringToSend.toString());
                        Props CounterProp = Props.create(FirstCounter.class);
                        ActorRef e1Node = system.actorOf(Estimator1Prop, "Estimator_1_Node_File"+i);
                        ActorRef e2Node = system.actorOf(Estimator2Prop, "Estimator_2_Node_File"+i);
                        ActorRef cunNode = system.actorOf(CounterProp, "Count_Node_File"+i);
                        // Send Payload
                        Thread.sleep(100);
                        cunNode.tell(Messages.START + "&=" + stringToSend.toString(), e1Node);
                        Thread.sleep(100);
                        cunNode.tell(Messages.START + "&=" + stringToSend.toString(), e2Node);
                        stringToSend.setLength(0);
                        

                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        }
    }
        private String getName(){
        return getSelf().path().name()+">";
    }

}
