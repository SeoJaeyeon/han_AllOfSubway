package kr.ac.han.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ibm.watson.developer_cloud.assistant.v1.Assistant;
import com.ibm.watson.developer_cloud.assistant.v1.model.InputData;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.assistant.v1.model.RuntimeIntent;

@Service
public class WatsonAPIConverterServiceImpl implements WatsonAPIConverterService {
	@Value("${ibm.username}")
	private String username;	
	@Value("${ibm.password}")
	private String password;
	@Value("${ibm.workspace}")
	private String workspaceId;
	
	Logger logger=LoggerFactory.getLogger(WatsonAPIConverterServiceImpl.class);
	
	@Override
	public String converterJy(String query) {

	    Assistant service = new Assistant("2018-08-06");
	    service.setUsernameAndPassword(username,password); 
	    
	    MessageOptions options = new MessageOptions.Builder(workspaceId).build();
	    
	    InputData input = new InputData.Builder(query).build();
	    options = new MessageOptions.Builder(workspaceId).input(input).build();
	     
	    MessageResponse mes_response = service.message(options).execute();      
	    String responseText = mes_response.getOutput().getText().get(0);
	    List<RuntimeIntent> responseIntents = mes_response.getIntents();

	    if(responseIntents.size() > 0) {
	    logger.info("Detected intent: #" + responseIntents.get(0).getIntent());
	    }
	    
	    String station= mes_response.getOutput().getText().get(0);
	    
	    logger.info(station);
		return station;
	}

}
