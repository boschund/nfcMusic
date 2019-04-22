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
package ch.sky.web.test;

import org.nfctools.api.ApduTag;
import org.nfctools.api.NfcTagListener;
import org.nfctools.api.Tag;
import org.nfctools.api.TagType;
import org.nfctools.mf.MfConstants;
import org.nfctools.mf.MfLoginException;
import org.nfctools.mf.block.DataBlock;
import org.nfctools.mf.block.MfBlock;
import org.nfctools.mf.block.TrailerBlock;
import org.nfctools.mf.classic.*;
import org.nfctools.spi.acs.AcrMfClassicReaderWriter;
import org.nfctools.utils.NfcUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class CardCleaner implements NfcTagListener {

	private Collection<byte[]> knownKeys = new ArrayList<byte[]>();

	public void addKnownKey(byte[] key) {
		knownKeys.add(key);
	}

	@Override
	public boolean canHandle(Tag tag) {
		return tag.getTagType().equals(TagType.MIFARE_CLASSIC_1K) || tag.getTagType().equals(TagType.MIFARE_CLASSIC_4K);
	}

	@Override
	public void handleTag(Tag tag) {
		MemoryLayout memoryLayout = tag.getTagType().equals(TagType.MIFARE_CLASSIC_1K) ? MemoryLayout.CLASSIC_1K
				: MemoryLayout.CLASSIC_4K;
		MfClassicReaderWriter readerWriter = new AcrMfClassicReaderWriter((ApduTag)tag, memoryLayout);
		try {
			doWithReaderWriter(readerWriter);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private TrailerBlock readTrailerBlock(MfClassicReaderWriter readerWriter, int sectorId) throws IOException {
		MemoryLayout memoryLayout = readerWriter.getMemoryLayout();
		for (byte[] key : knownKeys) {
			try {
				MfClassicAccess access = new MfClassicAccess(new KeyValue(Key.A, key), sectorId,
						memoryLayout.getTrailerBlockNumberForSector(sectorId));
				MfBlock[] readBlock = readerWriter.readBlock(access);
				return (TrailerBlock)readBlock[0];
			}
			catch (MfLoginException e) {
			    e.printStackTrace();
			}
		}
		return null;
	}

	public void doWithReaderWriter(MfClassicReaderWriter readerWriter) throws IOException {
		DataBlock emptyDataBlock = new DataBlock();
		MemoryLayout memoryLayout = readerWriter.getMemoryLayout();

		for (int sectorId = 0; sectorId < memoryLayout.getSectors(); sectorId++) {
			TrailerBlock trailerBlock = readTrailerBlock(readerWriter, sectorId);
			if (trailerBlock != null) {
				Key keyToWrite = trailerBlock.isKeyBReadable() ? Key.A : Key.B;
				for (byte[] key : knownKeys) {
					try {
						initTransportConfig(readerWriter, sectorId, new KeyValue(keyToWrite, key));
						for (int blockId = 0; blockId < memoryLayout.getBlocksPerSector(sectorId); blockId++) {
							if (!memoryLayout.isTrailerBlock(sectorId, blockId) && !(blockId == 0 && sectorId == 0)) {
                                System.out.println("Cleaning S" + sectorId + "|B" + blockId);
								MfClassicAccess access = new MfClassicAccess(new KeyValue(Key.A,
										MfConstants.TRANSPORT_KEY), sectorId, blockId);
								readerWriter.writeBlock(access, emptyDataBlock);
							}
						}
                        System.out.println("Sector " + sectorId + " clear with key: " + NfcUtils.convertBinToASCII(key));
						break;
					}
					catch (MfLoginException e) {
                        System.out.println("Cannot clear sector: " + sectorId + " with key " + NfcUtils.convertBinToASCII(key));
					}
				}
			}
			else {
                System.out.println("Cannot read trailer block in sector: " + sectorId);
			}
		}
        System.out.println("Done!");
	}

	private static void initTransportConfig(MfClassicReaderWriter readerWriter, int sector, KeyValue keyValue)
			throws IOException {
		MemoryLayout memoryLayout = readerWriter.getMemoryLayout();
		TrailerBlock transportTrailer = new TrailerBlock();
		MfClassicAccess access = new MfClassicAccess(keyValue, sector,
				memoryLayout.getTrailerBlockNumberForSector(sector));
		readerWriter.writeBlock(access, transportTrailer);
	}

}
