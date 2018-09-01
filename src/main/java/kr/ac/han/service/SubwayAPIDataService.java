package kr.ac.han.service;

import java.io.UnsupportedEncodingException;

import org.springframework.http.ResponseEntity;

public interface SubwayAPIDataService {
	public ResponseEntity realAccessTimeData(String station) throws UnsupportedEncodingException;
}
