package kr.ac.han.pf;


import lombok.Data;

@Data
public class RequestMessageVO
{
	//메시지를 발송한 유저 식별 
	private String user_key;
	//text, photo
	private String type;
	//자동 응답 명령어의 메시지 또는 미디어 파일 url
	private String content;
	
}

