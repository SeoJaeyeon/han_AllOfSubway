package kr.ac.han;

import java.io.IOException;

import java.net.URI;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.watson.developer_cloud.assistant.v1.Assistant;
import com.ibm.watson.developer_cloud.assistant.v1.model.InputData;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.assistant.v1.model.RuntimeIntent;

import kr.ac.han.pf.KeyboardVO;
import kr.ac.han.pf.MessageVO;
import kr.ac.han.pf.RequestMessageVO;
import kr.ac.han.pf.ResponseMessageVO;
import kr.ac.han.service.SubwayAPIDataService;
import kr.ac.han.service.WatsonAPIConverterService;
import kr.ac.han.vo.RealTimeArrivalList;

@RestController
public class RealTimeAccessInfoController {
	
	private static final Logger logger = LoggerFactory.getLogger(RealTimeAccessInfoController.class);
	
	@Autowired
	private SubwayAPIDataService subwayAPIDataService;
	
	@Autowired
	private WatsonAPIConverterService watsonAPIConverterService;
	
	@RequestMapping(value = "/keyboard", method = RequestMethod.GET, produces="application/json; charset=UTF-8")
	public KeyboardVO keyboard()
	{
		KeyboardVO keyboard=new KeyboardVO();
		keyboard.setType("text");
		return keyboard;
	}
	
	//Open API 2.0 Spec, 스웨거
	@RequestMapping(value = "/message", method = RequestMethod.POST, produces="application/json; charset=UTF-8")
	@ResponseBody
	public ResponseMessageVO message(@RequestBody RequestMessageVO vo, HttpServletRequest req, Model model) throws JsonParseException, JsonMappingException, IOException, APIServerException
	{
		logger.info("/message");
	/*	if(req.getParameter("reaccess")!=null){
			//재시도 상태
			int count=0;
			if(req.getParameter("count")==null)
				count=0;
			else
				count=Integer.parseInt(req.getParameter("count"))+1;
			logger.info(count+"번째 호출");
			model.addAttribute("count",count);
		}
		
		if(req.getParameter("fail")!=null){
			ResponseMessageVO res_vo=new ResponseMessageVO();
			MessageVO mes_vo=new MessageVO();
			KeyboardVO keyboard=new KeyboardVO();
			keyboard.setType("text");
			res_vo.setKeyboard(keyboard);
			MessageVO ms=new MessageVO();
			ms.setText("잠시후 다시 시도해주세요");
			res_vo.setMessage(ms);
			return res_vo;
		}
		throw new APIServerException();	*/
		ResponseMessageVO res_vo=new ResponseMessageVO();
		MessageVO mes_vo=new MessageVO();
		KeyboardVO keyboard=new KeyboardVO();
		keyboard.setType("text");
		res_vo.setKeyboard(keyboard);
		if(!vo.getType().equals("text"))
		{
			//텍스트 타입만 허용할 것이기 때문에
			
			mes_vo.setText("텍스트 타입만 허용하고 있습니다");

			res_vo.setMessage(mes_vo);
			return res_vo;
		}
		
		//텍스트 요청 받기
		String query=vo.getContent();		
		
		String station= watsonAPIConverterService.converterJy(query);		

		try{
	
		ResponseEntity response= subwayAPIDataService.realAccessTimeData(station);

		ObjectMapper obj=new ObjectMapper();
		RealTimeArrivalList rl=new RealTimeArrivalList();

		
		JsonNode node=obj.readValue(response.getBody().toString(), JsonNode.class);
		JsonNode realtimeArrivalList=node.get("realtimeArrivalList");
		mes_vo.setText(realtimeArrivalList.get(0).get("trainLineNm")+" -> "+realtimeArrivalList.get(0).get("arvlMsg2"));
		res_vo.setMessage(mes_vo);
		
		}catch(NullPointerException exNull){
			mes_vo.setText("올바른 역 명을 입력해주세요\nex)건대 어디야? -> 건대입구인데 어디야?");
			res_vo.setMessage(mes_vo);
			return res_vo;
		}
		catch(Exception ex){
				throw new APIServerException();			
		}
		return res_vo;

	}

	@ExceptionHandler(APIServerException.class)
	@ResponseBody
	public RedirectView apiServerExceptionHandler(APIServerException ex,HttpServletRequest request,HttpServletResponse response){
		  String redirect = "/message?fail";
		  RedirectView rw = new RedirectView(redirect);
		  
		  request.setAttribute(
			      View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
		  RequestMessageVO vo=new RequestMessageVO();
		  request.setAttribute("vo", vo);
		  request.setAttribute("fail", "");
		  return rw;
	}
	
}
