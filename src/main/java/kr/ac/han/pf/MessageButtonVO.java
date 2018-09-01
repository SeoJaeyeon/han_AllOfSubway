package kr.ac.han.pf;

import lombok.Data;

@Data
public class MessageButtonVO
{
	//링크버튼의 타이틀, required
	private String label;
	//링크버튼의 연결 링크 주소, required
	private String url;
}
