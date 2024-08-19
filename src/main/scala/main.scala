import akka.actor.{ ActorSystem, Actor, ActorRef, Props}

@main
def main(): Unit = {
	println("Start")

	val system = ActorSystem("pub-sub-mediator-spec-system")
	var ref = system.actorOf(Props(new PubSubMediator), "pub-sub-mediator")


}


object PubSubMediator
{
	case class Publish(topic: String, message: Any)

	case class Published(publish: Publish)

	case class Subscribe(topic: String, subscriber: ActorRef)

	case class Subscribed(subscribe: Subscribe)

	case class AlreadySubscribed(subscribe: Subscribe)

	case class Unsubscribe(topic: String, subscriber: ActorRef)

	case class Unsubscribed(unsubscribe: Unsubscribe)

	case class NotSubscribed(unsubscribe: Unsubscribe)

	case class GetSubscribers(topic: String)

	final val Name = "pub-sub-mediator"

	def props: Props = Props(new PubSubMediator)
}

class PubSubMediator extends Actor
{
	import PubSubMediator.*

	private var subscribers = Map.empty[String, Set[ActorRef]].withDefaultValue(Set.empty)

	override def receive =
	{
		case publish@Publish(topic, message) =>
			subscribers(topic).foreach(_ ! message)
			sender() ! Published(publish)

		case subscribe@Subscribe(topic, subscriber)
			if subscribers(topic).contains(subscriber) =>
			sender() ! AlreadySubscribed(subscribe)

		case subscribe@Subscribe(topic, subscriber) =>
			subscribers += topic -> (subscribers(topic) + subscriber)
			sender() ! Subscribed(subscribe)

		case unsubscribe@Unsubscribe(topic, subscriber)
			if !subscribers(topic).contains(subscriber) =>
			sender() ! NotSubscribed(unsubscribe)

		case unsubscribe@Unsubscribe(topic, subscriber) =>
			subscribers += topic -> (subscribers(topic) - subscriber)
			sender() ! Unsubscribed(unsubscribe)

		case GetSubscribers(topic) =>
			sender() ! subscribers(topic)
	}
}