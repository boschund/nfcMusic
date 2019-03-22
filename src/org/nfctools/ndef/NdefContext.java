/**
 * Copyright 2011-2012 Adrian Stabiszewski, as@org.nfctools.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.nfctools.ndef;

import org.nfctools.ndef.ext.*;
import org.nfctools.ndef.wkt.WellKnownRecordConfig;
import org.nfctools.ndef.wkt.WellKnownRecordPayloadDecoder;
import org.nfctools.ndef.wkt.WellKnownRecordPayloadEncoder;
import org.nfctools.ndef.wkt.decoder.*;
import org.nfctools.ndef.wkt.encoder.*;
import org.nfctools.ndef.wkt.handover.decoder.*;
import org.nfctools.ndef.wkt.handover.encoder.*;
import org.nfctools.ndef.wkt.handover.records.*;
import org.nfctools.ndef.wkt.records.*;

/**
 * Simple entry class for getting the different encoders and decoders.
 * 
 */
public class NdefContext {

	private static NdefRecordEncoder ndefRecordEncoder = new NdefRecordEncoder();
	private static NdefRecordDecoder ndefRecordDecoder = new NdefRecordDecoder();
	private static NdefMessageEncoder ndefMessageEncoder = new NdefMessageEncoder(ndefRecordEncoder);
	private static NdefMessageDecoder ndefMessageDecoder = new NdefMessageDecoder(ndefRecordDecoder);

	static {
		registerWellKnownRecord(new RecordType("act"), ActionRecord.class, new ActionRecordEncoder(),
				new ActionRecordDecoder());
		registerWellKnownRecord(new RecordType("U"), UriRecord.class, new UriRecordEncoder(), new UriRecordDecoder());
		registerWellKnownRecord(new RecordType("T"), TextRecord.class, new TextRecordEncoder(), new TextRecordDecoder());
		registerWellKnownRecord(new RecordType("Sp"), SmartPosterRecord.class, new SmartPosterRecordEncoder(),
				new SmartPosterRecordDecoder());
		registerWellKnownRecord(new RecordType("Gc"), GenericControlRecord.class, new GenericControlRecordEncoder(),
				new GenericControlRecordDecoder());
		registerWellKnownRecord(new RecordType("t"), GcTargetRecord.class, new GcTargetRecordEncoder(),
				new GcTargetRecordDecoder());
		registerWellKnownRecord(new RecordType("d"), GcDataRecord.class, new GcDataRecordEncoder(),
				new GcDataRecordDecoder());
		registerWellKnownRecord(new RecordType("a"), GcActionRecord.class, new GcActionRecordEncoder(),
				new GcActionRecordDecoder());
		registerWellKnownRecord(new RecordType("Sig"), SignatureRecord.class, new SignatureRecordEncoder(),
				new SignatureRecordDecoder());

		registerWellKnownRecord(new RecordType("ac"), AlternativeCarrierRecord.class,
				new AlternativeCarrierRecordEncoder(), new AlternativeCarrierRecordDecoder());
		registerWellKnownRecord(new RecordType("Hc"), HandoverCarrierRecord.class, new HandoverCarrierRecordEncoder(),
				new HandoverCarrierRecordDecoder());
		registerWellKnownRecord(new RecordType("Hr"), HandoverRequestRecord.class, new HandoverRequestRecordEncoder(),
				new HandoverRequestRecordDecoder());
		registerWellKnownRecord(new RecordType("Hs"), HandoverSelectRecord.class, new HandoverSelectRecordEncoder(),
				new HandoverSelectRecordDecoder());
		registerWellKnownRecord(new RecordType("err"), ErrorRecord.class, new ErrorRecordEncoder(),
				new ErrorRecordDecoder());
		registerWellKnownRecord(new RecordType("cr"), CollisionResolutionRecord.class,
				new CollisionResolutionRecordEncoder(), new CollisionResolutionRecordDecoder());

		// Known External Type Records
		registerExternalType("android.com:pkg", AndroidApplicationRecord.class, new AndroidApplicationRecordEncoder(),
				new AndroidApplicationRecordDecoder());
		registerExternalType("usingnfc.com:geo", GeoRecord.class, new GeoRecordEncoder(), new GeoRecordDecoder());

	}

	public static void registerWellKnownRecord(RecordType recordType, Class<? extends WellKnownRecord> recordClass,
			WellKnownRecordPayloadEncoder payloadEncoder, WellKnownRecordPayloadDecoder payloadDecoder) {
		WellKnownRecordConfig config = new WellKnownRecordConfig(recordType, recordClass, payloadEncoder,
				payloadDecoder);
		ndefRecordDecoder.registerWellKnownRecordConfig(config);
		ndefRecordEncoder.registerWellKnownRecordConfig(config);
	}

	public static void registerExternalType(String namespace, Class<? extends ExternalTypeRecord> recordClass,
			ExternalTypeContentEncoder payloadEncoder, ExternalTypeContentDecoder payloadDecoder) {
		ExternalTypeRecordConfig config = new ExternalTypeRecordConfig(namespace, recordClass, payloadEncoder,
				payloadDecoder);
		ndefRecordDecoder.registerExternalTypeRecordConfig(config);
		ndefRecordEncoder.registerExternalTypeRecordConfig(config);
	}

	public static NdefRecordDecoder getNdefRecordDecoder() {
		return ndefRecordDecoder;
	}

	public static NdefRecordEncoder getNdefRecordEncoder() {
		return ndefRecordEncoder;
	}

	public static NdefMessageDecoder getNdefMessageDecoder() {
		return ndefMessageDecoder;
	}

	public static NdefMessageEncoder getNdefMessageEncoder() {
		return ndefMessageEncoder;
	}

}
