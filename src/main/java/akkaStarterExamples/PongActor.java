package akkaStarterExamples;

import akka.actor.UntypedActor;

public class PongActor extends UntypedActor {

	@Override
	public void onReceive(Object msg) throws Exception {
		if (msg instanceof String) {
			String payload = (String)msg;
			if (payload.equals("stop")) { // Game over
				System.out.println(getSelf().path().name() +  ": OK");
			}
			else if (payload.equals("start")) {
				System.out.println(getSelf().path().name() +  ": Let's do it.");
				getSender().tell("go", getSelf());
			}
			else { // Next stroke
				System.out.println("Pong");
				getSender().tell("go", getSelf());
			}
		}
	}

}
