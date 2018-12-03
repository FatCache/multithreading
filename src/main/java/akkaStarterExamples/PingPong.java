package akkaStarterExamples;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class PingPong {

	public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("Ping_Pong");
        Props pingProps = Props.create(PingActor.class, 5);
        Props pongProps = Props.create(PongActor.class);
        ActorRef pingNode = actorSystem.actorOf(pingProps, "Ping_Node");
        ActorRef pongNode = actorSystem.actorOf(pongProps, "Pong_Node");
        pingNode.tell(pongNode, null);
        actorSystem.terminate();
	}

}
