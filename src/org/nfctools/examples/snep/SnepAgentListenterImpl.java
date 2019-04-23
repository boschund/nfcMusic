package org.nfctools.examples.snep;

import org.nfctools.ndef.Record;
import org.nfctools.ndef.wkt.records.UriRecord;
import org.nfctools.snep.PutResponseListener;
import org.nfctools.snep.SnepAgent;
import org.nfctools.snep.SnepAgentListener;
import static ch.bod.nfcMusic.Logger.*;


import java.util.ArrayList;
import java.util.List;

public class SnepAgentListenterImpl implements SnepAgentListener, PutResponseListener {


	private List<Record> records = new ArrayList<Record>();

	public SnepAgentListenterImpl(String url) {
		records.add(new UriRecord(url));
	}

	@Override
	public void onSnepConnection(SnepAgent snepAgent) {
		if (!records.isEmpty()) {
			info("SNEP connection available, sending message...");
			snepAgent.doPut(new ArrayList<Record>(records), this);
			records.clear();
		}
	}

	@Override
	public boolean hasDataToSend() {
		return !records.isEmpty();
	}

	@Override
	public void onSuccess() {
		info("SNEP succeeded");
		records.clear();
	}

	@Override
	public void onFailed() {
		info("SNEP failed");
	}

}
