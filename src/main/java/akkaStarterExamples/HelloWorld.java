package akkaStarterExamples;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;


public class HelloWorld {
	public static void main(String[] args) {
		// System, Actor names must not contain spaces
        ActorSystem actorSystem = ActorSystem.create("Message_Printer");
        Props mpProps = Props.create(MessagePrinterActor.class);
        ActorRef mpNode = actorSystem.actorOf(mpProps, "MP_Node");
//        mpNode.tell("Hello World", ActorRef.noSender());
        mpNode.tell("Hello World", null);
        mpNode.tell("akka is fab!", null);
        mpNode.tell("Is this an infinite loop?", null);
        actorSystem.terminate();
	}
}
