package akkaStarterExamples;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class PingActor extends UntypedActor {
	
	private int numHitsLeft;
	private ActorRef partner;
	
	public PingActor(int numHits) {
		this.numHitsLeft = numHits;
	}

	@Override
	public void onReceive(Object msg) throws Exception {
		if (msg instanceof ActorRef) {
			partner = (ActorRef)msg;
			System.out.println(getSelf().path().name() +  ": Game on!");
			partner.tell("start", getSelf());
		}
		else if (msg instanceof String) {
			if (numHitsLeft == 0) { // stop game
				System.out.println(getSelf().path().name() +  ": Game over");
				partner.tell("stop", getSelf());
			}
			else { // keep playing 
				System.out.print("Ping ... ");
				partner.tell("go", getSelf());
				numHitsLeft--;
			}
		}
	}

}
