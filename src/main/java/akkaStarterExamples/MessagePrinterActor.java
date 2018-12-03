package akkaStarterExamples;

import akka.actor.UntypedActor;

public class MessagePrinterActor extends UntypedActor {

	@Override
	public void onReceive(Object msg) throws Exception {
		if (msg instanceof String) {
			System.out.printf("Message is: %s%n", msg);
		}
	}
}
