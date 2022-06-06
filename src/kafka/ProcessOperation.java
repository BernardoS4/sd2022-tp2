package kafka;

import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import kafka.sync.SyncPoint;
import tp1.api.FileInfo;
import tp1.api.service.rest.RestDirectory;
import tp1.impl.servers.common.JavaDirectory.ExtendedFileInfo;
import tp1.impl.servers.common.JavaRepDirectory;
import util.JSON;


public class ProcessOperation implements RecordProcessor {

	private JavaRepDirectory rep;
	

	public ProcessOperation(JavaRepDirectory rep) {
		this.rep = rep;
	}

	@Override
	public void onReceive(ConsumerRecord<String, String> r) {

		Map<String, String> opParams = JSON.decode(r.value());

		String filename = JSON.decode(opParams.get(RestDirectory.FILENAME), String.class);
		String userId = JSON.decode(opParams.get(RestDirectory.USER_ID), String.class);
		String userIdShare = JSON.decode(opParams.get(RestDirectory.USER_ID_SHARE), String.class);
		URI[] uris = JSON.decode(opParams.get("uris"), URI[].class);
		String fileId = JSON.decode(opParams.get("fileId"), String.class);
		FileInfo info = JSON.decode(opParams.get("info"), FileInfo.class);
		ExtendedFileInfo file = new ExtendedFileInfo(Arrays.asList(uris), fileId, info);

		switch (r.key()) {
		case RestDirectory.WRITE_FILE:
			SyncPoint.getInstance().setResult(r.offset(), rep.writeFileSec(filename, userId, file));
			break;
		case RestDirectory.DELETE_FILE:
			SyncPoint.getInstance().setResult(r.offset(), rep.deleteFileSec(filename, userId));
			break;
		case RestDirectory.SHARE_FILE:
			SyncPoint.getInstance().setResult(r.offset(), rep.shareFileSec(filename, userId, userIdShare));
			break;
		case RestDirectory.UNSHARE_FILE:
			SyncPoint.getInstance().setResult(r.offset(), rep.unshareFileSec(filename, userId, userIdShare));
			break;
		default:
			break;
		}
	}

}
