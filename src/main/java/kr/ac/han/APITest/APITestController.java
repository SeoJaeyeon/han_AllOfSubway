package kr.ac.han.APITest;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.List;
import java.util.logging.LogManager;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.watson.developer_cloud.assistant.v1.Assistant;
import com.ibm.watson.developer_cloud.assistant.v1.model.InputData;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.assistant.v1.model.RuntimeIntent;

import kr.ac.han.vo.RealTimeArrivalList;

//Watson, 공공데이터 서버 확인을 위한 컨트롤러 
//Test - SubwayTest, WatsonTest 

@Controller
public class APITestController {
	
	Logger logger=LoggerFactory.getLogger(APITestController.class);
	
	@Value("${ibm.username}")
	private String username;	
	@Value("${ibm.password}")
	private String password;
	@Value("${ibm.workspace}")
	private String workspaceId;
	@Value("${subway.key}")
	private String appKey;
	//테스트
		@RequestMapping(value="/watson", method=RequestMethod.GET ,produces = "application/json;charset=UTF-8")
		public String watson(HttpServletRequest req){
			LogManager.getLogManager().reset();

		    // Set up Assistant service.
		    Assistant service = new Assistant("2018-08-06");
		    service.setUsernameAndPassword(username, // replace with service username
		                                   password); // replace with service password

		    // Start assistant with empty message.
		    MessageOptions options = new MessageOptions.Builder(workspaceId).build();
		    
		        String inputText = req.getParameter("query");
		        InputData input = new InputData.Builder(inputText).build();
		        options = new MessageOptions.Builder(workspaceId).input(input).build();
		     
		        // Send message to Assistant service.
		        MessageResponse response = service.message(options).execute();      
		        String responseText = response.getOutput().getText().get(0);
		        List<RuntimeIntent> responseIntents = response.getIntents();

		        // If an intent was detected, print it to the console.
		        if(responseIntents.size() > 0) {
		          System.out.println("Detected intent: #" + responseIntents.get(0).getIntent());
		        }

		    return response.getOutput().getText().get(0);
		    
		}
		
		//테스트
		@RequestMapping(value="/subway", method=RequestMethod.GET,produces = "application/json;charset=UTF-8")
		public JsonNode test(HttpServletRequest req) throws JsonParseException, JsonMappingException, IOException{
			RestTemplate restTemplate = new RestTemplate(); 
			 
			logger.info(appKey);
			HttpHeaders headers = new HttpHeaders(); 
			headers.add("Content-type", "application/json; charset=UTF-8");
			
			 
			HttpEntity entity = new HttpEntity("parameters", headers); 

	
			URI url=URI.create("http://swopenapi.seoul.go.kr/api/subway/"+appKey+"/json/realtimeStationArrival/0/5/"+URLEncoder.encode(req.getParameter("region"),"UTF-8")); 
			//x -> x좌표, y -> y좌표 
			 
			ResponseEntity response= restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
			//String 타입으로 받아오면 JSON 객체 형식으로 넘어옴
		
			ObjectMapper obj=new ObjectMapper();
			RealTimeArrivalList rl=new RealTimeArrivalList();
			//d=obj.readValue(response.getBody(), JSONMapper.class);
			
			JsonNode node=obj.readValue(response.getBody().toString(), JsonNode.class);
			JsonNode realtimeArrivalList=node.get("realtimeArrivalList");		
			return realtimeArrivalList.get(0);
			
		}  


		
		
}
