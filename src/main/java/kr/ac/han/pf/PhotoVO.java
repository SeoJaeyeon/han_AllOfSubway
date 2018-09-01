package kr.ac.han.pf;

import lombok.Data;

@Data
public class PhotoVO
{
	//이미지 url, required
	private String url;
	//이미지 width, required
	private int width;
	//이미지 height, required
	private int height;
	
	@Override
	public String toString() {
		return "PhotoVO [url=" + url + ", width=" + width + ", height=" + height + "]";
	}
	
	
}