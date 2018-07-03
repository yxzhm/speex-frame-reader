package com.nuance.speex;

import lombok.Data;

@Data
public class SpeexFrame {
    private int modeType;
    private int modeID;
    private int length;

    public SpeexFrame(int modeType, int modeID, int length) {
        this.modeType = modeType;
        this.modeID = modeID;
        this.length = length;
    }

    @Override
    public String toString() {
        return "SpeexFrame{" +
                "modeType=" + (modeType > 0 ? "Wide Band" : "Narrow Band") +
                ", modeID=" + modeID +
                ", frame length=" + length +
                '}';
    }
}

