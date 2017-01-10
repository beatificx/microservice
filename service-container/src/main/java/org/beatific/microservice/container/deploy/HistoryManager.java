package org.beatific.microservice.container.deploy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.beatific.microservice.container.environment.Server;
import org.beatific.microservice.container.utils.JPickle;
import org.springframework.stereotype.Component;

@Component
public class HistoryManager {

	private Map<Server, List<DeployHistory>> histories = new HashMap<>();
	
	private final String filename = "c:/temp/history.ser";
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void load() {
		
		try {
			histories = (Map<Server, List<DeployHistory>>)JPickle.load(filename);
		} catch (ClassNotFoundException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Map<Server, List<DeployHistory>> get() {
		return histories;
	}
	
	public void writeDownHistory(Deployment deployment) {
		
		DeployHistory history = new DeployHistory();
		
		history.setVersion(deployment.getVersion());
		history.setTimeStamp(new Date().getTime());
		
		List<DeployHistory> deployHistories = histories.get(deployment.getServer());
		if(deployHistories == null) {
			deployHistories = new ArrayList<DeployHistory>();
		}
		deployHistories.add(history);

		histories.put(deployment.getServer(), deployHistories);

		try {
			JPickle.dump(filename, histories);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
