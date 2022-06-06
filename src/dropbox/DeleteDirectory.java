package dropbox;

import org.pac4j.scribe.builder.api.DropboxApi20;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.google.gson.Gson;

import dropbox.msgs.DeleteDirectoryV2Args;

public class DeleteDirectory {

	//private static final String apiKey = "nyekq63jvq28jsq";
	//private static final String apiSecret = "cbsndqm28jogmbp";
	//private static final String accessTokenStr = "sl.BIUwns933jo6Urn9ISbENp_2qgCYta9CRWbXH0q4dTFFXwjI8cdfVcEX5BJlMQ5JdZI3_Ps89DdPmXO3sPbr-4k4JJMQ9ucQoFvu_Z0TMfj9rErKi-MEKG4VbKY5wYJs3Sif1jtGJ8jA";
	
	private static final String DELETE_DIR_V2_URL = "https://api.dropboxapi.com/2/files/delete_v2";
	
	private static final int HTTP_SUCCESS = 200;
	private static final String CONTENT_TYPE_HDR = "Content-Type";
	private static final String JSON_CONTENT_TYPE = "application/json; charset=utf-8";
	
	private final Gson json;
	private final OAuth20Service service;
	private final OAuth2AccessToken accessToken;
		
	public DeleteDirectory() {
		json = new Gson();
		accessToken = new OAuth2AccessToken(DropboxArguments.getAccessTokenStr());
		service = new ServiceBuilder(DropboxArguments.getApiKey()).apiSecret(DropboxArguments.getApiSecret()).build(DropboxApi20.INSTANCE);
	}
	
	public void execute( String directoryName ) throws Exception {
		
		var deleteDir = new OAuthRequest(Verb.POST, DELETE_DIR_V2_URL);
		deleteDir.addHeader(CONTENT_TYPE_HDR, JSON_CONTENT_TYPE);

		deleteDir.setPayload(json.toJson(new DeleteDirectoryV2Args(directoryName)));

		service.signRequest(accessToken, deleteDir);
		
		Response r = service.execute(deleteDir);
		if (r.getCode() != HTTP_SUCCESS) 
			throw new RuntimeException(String.format("Failed to delete directory/file: %s, Status: %d, \nReason: %s\n", directoryName, r.getCode(), r.getBody()));
		
	}
}
