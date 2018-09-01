package kr.ac.han.pf;

import lombok.Data;

@Data
public class MessageVO
{
	//사용자에게 발송될 텍스트("최대 100자")
	private String text;
	//말풍선에 들어갈 이미지 정보. 1장 제한,JPEG/PNG 포맷
	private PhotoVO photo;
	//말풍선에 붙는 링크버튼 정보.
	private MessageButtonVO message_button;

}