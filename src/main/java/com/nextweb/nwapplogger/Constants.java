package com.nextweb.nwapplogger;


/**
 * @author nextweb
 * 
 */
public interface Constants {
	static boolean IS_DEBUG_MODE = NWLogger.isDebug;
	static final String UnknownDevice = "UnknownDevice";
	static final String UnknownAdId = "UnknownAdId";
	static final String DATABASE_NAME = "appEvent_NWLogger.db";
	static final String TABLE_NAME = "tb_usage_logger";
	static final int DATABASE_VERSION = 2;
	static final String XtractorKey = "XtractorKey";
	static final String HEXES = "0123456789abcdef";
	static final String PROTOCOL_HTTP = "HTTP";
	static final String PROTOCOL_HTTPS = "HTTPS";
	static final String OS_TYPE = "android";

	public static final String _APP_START_ = "APP_START";
	public static final String _APP_FINISH_ = "APP_FINISH";
	public static final String _APP_PAUSE_ = "APP_PAUSE";
	public static final String _APP_RESUME_ = "APP_RESUME";
	public static final String _SEARCH_ = "SEARCH";
	public static final String _HOME_ = "HOME";
	public static final String _CONFIG_ = "CONFIG";
	public static final String _PREV_BTN_ = "PREV_BTN";
	public static final String _BTN_CLICK_ = "BTN_CLICK";
	public static final String _SCROLL_ = "SCROLL";
	public static final String _ZOOM_ = "ZOOM";
	public static final String _MENU_CLICK_ = "MENU_CLICK";
	public static final String _AV_PLAY_ = "AV_PLAY";

	interface params {
		static final String ct = "ct";								// 로그생성시간
		static final String st = "st";								// 로그전송시간
		static final String osType = "osType";						// OS 종류
		static final String osVersion = "osVersion";				// OS 버전
		static final String apiKey = "apiKey";						// api 키
		static final String appName = "appName";					// 앱이름
		static final String activityName = "activityName";			// 액티비티명
		static final String packageName = "packageName";			// 패키지명
		static final String className = "className";				// 클래스명
		static final String xtVid = "xtVid";						// ADID
		static final String xtDid = "xtDid";						// UUID
		static final String xtUid = "xtUid";						// 서비스 로그인 아이디
		static final String ssId = "ssId";							// 세션 아이디
		static final String evtType = "evtType";					// 이벤트 형태
		static final String evtDesc = "evtDesc";					// 이벤트 설명
		static final String cateId = "cateId";						// 카테고리
		
		static final String macAddress = "macAddress";				// 맥주소
		static final String resolution = "resolution";				// 해상도
		static final String appRunCount = "appRunCount";			// 앱실행수
		static final String referrer = "referrer";					// referrer

		static final String buildBoard = "buildBoard";
		static final String buildBrand = "buildBrand";
		static final String buildDevice = "buildDevice";
		static final String buildDisplay = "buildDisplay";
		static final String buildFingerprint = "buildFingerprint";
		static final String buildHost = "buildHost";
		static final String buildId = "buildId";
		static final String buildManufacturer = "buildManufacturer";
		static final String buildModel = "buildModel";
		static final String buildProduct = "buildProduct";
		static final String buildSerial = "buildSerial";
		static final String buildTags = "buildTags";
		static final String buildTime = "buildTime";
		static final String buildType = "buildType";
		static final String buildUser = "buildUser";
		static final String buildVersionRelease = "buildVersionRelease";
		static final String buildVersionSdkInt = "buildVersionSdkInt";
		static final String buildVersionCodename = "buildVersionCodename";
		static final String buildHardware = "buildHardware";

		// 추가된 항목
		static final String countryCd = "countryCd";				// 국가코드
		static final String language = "language";					// 언어
		static final String carrier = "carrier";					// 통신사
	}
}
