package kr.ac.han.vo;

import lombok.Data;

@Data
public class RealTimeArrivalList {
	private String beginRow;
	private String endRow;
	private String curPage;
	private String pageRow;
	private Integer totalCount;
	private Integer selectedCount; 
	private Integer subwayId;
	private String subwayNm;
	private String updnLine;
	private String trainLineNm;
	private String subwayHeading;
	private Integer statnFid;
	private Integer statnTid;
	private Integer statnId;
	private String statnNM;
	private String trainCo;
	private String ordKey;
}
