package com.appota.app.spinmachine.network;

public class API {
	public static final String HOST = "id.appota.com";
	public static final String ACCESS_TOKEN_URL = "https://id.appota.com/app/access_token";
	public static final String LOGIN_URL = "https://api.appota.com/user/login";
	public static final String GET_USER_DATA_URL = "https://api.appota.com/user/game_status?";
	public static final String GET_GAME_PLAY_URL = "https://api.appota.com/user/game_play";
	public static final String GET_GIFT_BOX_LIST_URL = "https://api.appota.com/user/get_gift_box_list?";
	public static final String OPEN_GIFT_BOX_URL = "https://api.appota.com/user/open_gift_box?";
	public static final String REQUESTTOKEN = "https://id.appota.com/app/request_token?response_type=code";
	public static final String THEME_DOMAIN = "http://static.appota.com/";
	public static final String CLIENT_ID = "client_id";
	public static final String ACCESS_TOKEN = "access_token";
	public static final String REDIRECT_URI = "REDIRECT_URI";
	public static final String REDIRECT_URI_VALUE = "http://localhost";
	public static final String SCOPE = "scope=user.info";
	public static final String STATE = "state";
	public static final String STATUS = "status";
	public static final String USER_NAME = "username";
	public static final String PASSWORD = "password";
	public static final String DEVICE_ID = "device_id";
	public static final String DEVICE_OS = "device_os";
	public static final String DEVICE_OS_VERSION = "device_os_version";
	public static final String VENDOR = "vendor";
	public static final String PHONE_NUMBER = "phone_number";
	public static final String CLIENT_VERSION = "client_version";

	public static final String TOKEN_TYPE = "token_type";
	public static final String EXPRIES_IN = "expires_in";
	public static final String LANG = "lang";
	public static final String LANG_VALUE = "vni";
	public static final String REQUEST_TOKEN = "request_token";
	public static final String USER_INFO = "user.info";
	public static final String STATE_VALUE = "test";
	public static final String CLIENT_ID_VALUE = "f6bcef37aed92ba35d8a3a26875b594704fc4780d";
	public static final String CLIENT_SECRET_VALUE = "c0d8541f06d17e3d9062e79436a5ebea04fc4780d";
	public static final String GRANT_TYPE = "grant_type";
	public static final String GRANT_TYPE_VALUE = "authorization_code";
	public static final String CLIENT_SECRET = "client_secret";
	public static final String REQUEST_TOKEN_URI = REQUESTTOKEN + "&"
			+ CLIENT_ID + "=" + CLIENT_ID_VALUE + "&" + SCOPE + "&"
			+ REDIRECT_URI + "=" + REDIRECT_URI_VALUE + "&" + STATE + "="
			+ STATE_VALUE + "&" + LANG + "=" + LANG_VALUE;
}
