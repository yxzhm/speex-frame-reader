package com.nuance.speex;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SpeexReader {
    List<SpeexFrame> speexFrames;

    public SpeexReader() {
        initFrameTypes();
    }

    private void initFrameTypes() {
        speexFrames = new ArrayList<SpeexFrame>();
        //Speex doc 9.3
        speexFrames.add(new SpeexFrame(0, 0, 5));
        speexFrames.add(new SpeexFrame(0, 1, 43));
        speexFrames.add(new SpeexFrame(0, 2, 119));
        speexFrames.add(new SpeexFrame(0, 3, 160));
        speexFrames.add(new SpeexFrame(0, 4, 220));
        speexFrames.add(new SpeexFrame(0, 5, 300));
        speexFrames.add(new SpeexFrame(0, 6, 364));
        speexFrames.add(new SpeexFrame(0, 7, 492));
        speexFrames.add(new SpeexFrame(0, 8, 79));
        //Speex doc 10.4
        speexFrames.add(new SpeexFrame(1, 0, 4));
        speexFrames.add(new SpeexFrame(1, 1, 36));
        speexFrames.add(new SpeexFrame(1, 2, 112));
        speexFrames.add(new SpeexFrame(1, 3, 192));
        speexFrames.add(new SpeexFrame(1, 4, 352));
    }

    public List<SpeexFrame> read(byte[] data) throws Exception {
        List<SpeexFrame> result = new ArrayList<SpeexFrame>();
        byte[] rawData = data;
        int globalBitIndex = 0;
        int n = 0;
        boolean isTerminator = false;
        if (rawData != null && rawData.length > 0) {
            while (globalBitIndex < data.length * 8) {
                n++;
                final int byteIndex = globalBitIndex / 8;
                final int bitIndex = globalBitIndex % 8;
                short tempData = 0;
                if (byteIndex < rawData.length - 1) {
                    tempData = (short) (((rawData[byteIndex] << 8) & 0xFF00) | (((rawData[byteIndex + 1])) & 0x00FF));
                } else {
                    tempData = (short) (((rawData[byteIndex] << 8) & 0xFF00) | 0x00);
                }
                final int modeType = tempData >> (16 - bitIndex - 1) & 0x01;
                int modeId = 0;
                if (modeType == 0) {
                    modeId = tempData >> (16 - bitIndex - 5) & 0x0F;
                } else {
                    modeId = tempData >> (16 - bitIndex - 4) & 0x07;
                }
                int finalModeId = modeId;
                Optional<SpeexFrame> speexFrame = speexFrames.stream().filter(x -> x.getModeID() == finalModeId && x.getModeType() == modeType).findFirst();
                if (speexFrame.isPresent()) {
                    globalBitIndex += speexFrame.get().getLength();
                    result.add(speexFrame.get());
                    System.out.println(n + ":" + speexFrame);
                    isTerminator = false;
                } else {
                    if (modeType == 0 && !isTerminator) {
                        //Maybe terminator
                        for (int i = bitIndex; i < 8; i++) {
                            globalBitIndex++;
                        }
                        System.out.println("terminator");
                        isTerminator = true;
                        continue;
                    }
                    throw new Exception("Invalid Frame");
                }
            }
        }
        return result;
    }
}
