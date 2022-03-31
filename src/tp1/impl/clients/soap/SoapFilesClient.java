package tp1.impl.clients.soap;

import java.net.URI;

import javax.xml.namespace.QName;

import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.Service;
import tp1.api.service.java.Files;
import tp1.api.service.java.Result;
import tp1.api.service.soap.SoapFiles;
import tp1.api.service.soap.SoapUsers;
import util.Url;

public class SoapFilesClient extends SoapClient implements Files {

	private static final String SOAP_PORT = "SoapUsersWebServicePort";

	public SoapFilesClient(URI serverURI) {
		super(serverURI);
	}

	private SoapFiles impl;

	synchronized private SoapFiles impl() {
		if (impl == null) {
			QName QNAME = new QName(SoapUsers.NAMESPACE, SoapUsers.NAME);

			Service service = Service.create(Url.from(super.uri + WSDL), QNAME);

			QName port = new QName(SoapFiles.NAMESPACE, SOAP_PORT);
			this.impl = service.getPort(port, tp1.api.service.soap.SoapFiles.class);
			super.setTimeouts((BindingProvider) impl);
		}
		return impl;
	}

	@Override
	public Result<byte[]> getFile(String fileId, String token) {
		return super.tryCatchResult(() -> impl().getFile(fileId, token));
	}

	@Override
	public Result<Void> deleteFile(String fileId, String token) {
		return super.tryCatchVoid(() -> impl().deleteFile(fileId, token));
	}

	@Override
	public Result<Void> writeFile(String fileId, byte[] data, String token) {
		return super.tryCatchVoid(() -> impl().writeFile(fileId, data, token));
	}

	@Override
	public Result<Void> deleteUserFiles(String userId, String token) {
		return super.tryCatchVoid(() -> impl().deleteUserFiles(userId, token));
	}

	@Override
	public Result<byte[]> getUrl(String url, String token) {
		return null;
	}
}
