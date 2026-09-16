package datasource;

import domain.AudioCodec;
import domain.VideoStreaming;
import org.junit.Test;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.*;

public class ProductGatewayTests {

    //generic ProductGateway for testing
    ProductType type = ProductType.AudioTrack;
    String sku = "000000000000";
    String name = "Test Product";
    double basePrice = 15.99;
    long size = 50;
    boolean hasLyrics = false;
    Set<AudioCodec> codecs;
    ArrayList<VideoStreaming> supportedStreamingServices = new ArrayList<>();


    /// The following tests were done to check calculateBitmask() and getSupportedCodecSet(int mask)'s
    /// functionality, by temporarily switching the functions to public. After the tests passed, the
    /// functions became private again.

    @Test
    public void CodecBitmaskEdgesTest() {
        //Set is empty
        codecs = Set.of();
        ProductGateway gateway = new ProductGateway(type, sku, name, basePrice, size, hasLyrics, codecs, supportedStreamingServices);
        int expectedBitmask = 0;
        int actualBitmask = gateway.calculateBitmask(codecs);
        assertEquals(expectedBitmask, actualBitmask);

        //Set is full
        codecs = Set.of(AudioCodec.MP3, AudioCodec.AAC, AudioCodec.FLAC, AudioCodec.WAV);
        expectedBitmask = 15;
        actualBitmask = gateway.calculateBitmask(codecs);
        assertEquals(expectedBitmask, actualBitmask);

    }

    @Test
    public void SingleCodecBitmaskTest() {
        codecs = Set.of(AudioCodec.MP3);
        ProductGateway gateway = new ProductGateway(type, sku, name, basePrice, size, hasLyrics, codecs, supportedStreamingServices);
        int expectedBitmask = 1;
        int actualBitmask = gateway.calculateBitmask(codecs);
        assertEquals(expectedBitmask, actualBitmask);
        codecs = Set.of(AudioCodec.AAC);
        expectedBitmask = 2;
        actualBitmask = gateway.calculateBitmask(codecs);
        assertEquals(expectedBitmask, actualBitmask);
        codecs = Set.of(AudioCodec.FLAC);
        expectedBitmask = 4;
        actualBitmask = gateway.calculateBitmask(codecs);
        assertEquals(expectedBitmask, actualBitmask);
        codecs = Set.of(AudioCodec.WAV);
        expectedBitmask = 8;
        actualBitmask = gateway.calculateBitmask(codecs);
        assertEquals(expectedBitmask, actualBitmask);

    }

/*
    @Test
    public void getSupportedCodecsTest() {
        codecs = Set.of();
        ProductGateway gateway = new ProductGateway(type, sku, name, basePrice, size, hasLyrics, codecs, supportedStreamingServices);
        //Mask 0 = Empty Set
        assertTrue(gateway.getSupportedCodecsSet(0).isEmpty());
        //Mask 3 = MP3 + AAC
        Set<AudioCodec> expectedSet = Set.of(AudioCodec.MP3, AudioCodec.AAC);
        Set<AudioCodec> actualSet = gateway.getSupportedCodecsSet(3);
        assertEquals(expectedSet, actualSet);
        //Mask 15 = MP3 + AAC + FLAV + WAV
        expectedSet = Set.of(AudioCodec.MP3, AudioCodec.AAC, AudioCodec.FLAC, AudioCodec.WAV);
        actualSet = gateway.getSupportedCodecsSet(15);
        assertEquals(expectedSet, actualSet);
    }
    */

//    @Test
//    public void getGeneratedID() {
//        //declare mock gateway instance
//        ProductGateway gateway1 = new ProductGateway(type, sku, name, basePrice, size, hasLyrics, codecs, supportedStreamingServices);
//        ProductGateway gateway2 = new ProductGateway(type, sku, name, basePrice, size, hasLyrics, codecs, supportedStreamingServices);
//
//        //generated ID should be more than 0
//        assert(gateway1.getId() > 0);
//        //generated ID should be unique
//        assertNotEquals(gateway1.getId(), gateway2.getId());
//    }

}
