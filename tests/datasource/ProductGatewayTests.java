package datasource;

import domain.AudioCodec;
import domain.AudioTrack;
import domain.VideoStreaming;
import org.junit.Test;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;
import static org.junit.Assert.*;

public class ProductGatewayTests {

    @Test
    public void SetCodecTest() {
        ProductType type = ProductType.AudioTrack;
        String sku = "000000000000";
        String name = "Test Product";
        double basePrice = 15.99;
        long size = 50;
        boolean hasLyrics = false;
        Set<AudioCodec> codecs = EnumSet.of(AudioCodec.MP3, AudioCodec.AAC);
        ArrayList<VideoStreaming> supportedStreamingServices = new ArrayList<>();

        ProductGateway gateway = new ProductGateway(type, sku, name, basePrice, size, hasLyrics, codecs, supportedStreamingServices);
        //create the expected bitmask for testing purposes
        int expectedBitmask = 3; //MP3 (1) + AAC (2)

        //call the set function that is in ProductGateway
        int actualBitmask = gateway.setCodecs(codecs);

        //Test that the actualBitmask matches the expectedBitmask
        assertEquals(expectedBitmask, actualBitmask);

    }

    // Tests ran with temporary visibility change to test the calculateBitmask()
    // and getSupportedCodecsSet() methods directly
    public void calculateBitmaskLogicTest() {
        Set<AudioCodec> codecs = EnumSet.of(AudioCodec.MP3, AudioCodec.AAC);
        //testing with 1 enum supported
        Set<AudioCodec> minimalCodecs = EnumSet.of(AudioCodec.AAC);
        //testing with all enums supported
        Set<AudioCodec> maxCodecs = EnumSet.of(AudioCodec.MP3, AudioCodec.AAC, AudioCodec.FLAC, AudioCodec.WAV);
    }
}
