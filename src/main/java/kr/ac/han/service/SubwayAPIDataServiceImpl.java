package kr.ac.han.service;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ibm.watson.developer_cloud.http.HttpStatus;

@Service
public class SubwayAPIDataServiceImpl implements SubwayAPIDataService {
	
	@Value("${subway.key}")
	private String appKey;
	
	@Override
	public ResponseEntity realAccessTimeData(String station) throws UnsupportedEncodingException {
		String encodeStation=URLEncoder.encode(station,"UTF-8");
		HttpEntity entity = new HttpEntity("parameters", new HttpHeaders()); 
		RestTemplate restTemplate = new RestTemplate(); 
		URI url=URI.create("http://swopenapi.seoul.go.kr/api/subway/"+appKey+"/json/realtimeStationArrival/0/5/"+encodeStation); 
		ResponseEntity response= restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		return response;
	}
	

}
