package org.beatific.microservice.container.service.event;

public interface EventHandler<T extends ServiceEvent> {

	public void handle(T event);
}
