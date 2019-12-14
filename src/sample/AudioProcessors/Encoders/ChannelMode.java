package sample.AudioProcessors.Encoders;

import org.jaudiotagger.tag.FieldKey;

public enum ChannelMode {

    MONO (1), STEREO(2), FIVE_POINT_ONE (6);



    int channels;

    ChannelMode(int channels) {
        this.channels = channels;
    }

    public int getChannels() {
        return channels;
    }

    public void setChannels(int channels) {
        this.channels = channels;
    }
}
