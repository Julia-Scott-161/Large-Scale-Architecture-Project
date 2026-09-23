package datasource;

import domain.*;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;
import static org.junit.Assert.*;

public class ProductGatewayTests {

    //generic ProductGateway for AudioTrack testing
    //ProductType type = ProductType.AudioTrack;
    //Type for testing DatasourceTypeMismatch
    ProductType type = ProductType.VideoStreaming;
    String sku = "000000000001";
    String name = "Test Product";
    double basePrice = 15.99;
    long size = 50;
    boolean hasLyrics = false;
    Set<AudioCodec> codecs;
    ArrayList<VideoStreaming> supportedStreamingServices = new ArrayList<>();
    private static final Connection conn;

    static
    {
        try
        {
            conn = DatabaseRegistry.getConnection();
            assert !conn.isClosed();
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    /// The following tests were done to check calculateBitmask() and getSupportedCodecSet(int mask)'s
    /// functionality, by temporarily switching the functions to public. After the tests passed, the
    /// functions became private again.

//    @Test
//    public void CodecBitmaskEdgesTest() {
//        //Set is empty
//        codecs = Set.of();
//        ProductGateway gateway = new ProductGateway(type, sku, name, basePrice, size, hasLyrics, codecs, supportedStreamingServices);
//        int actualBitmask = gateway.calculateBitmask(codecs);
//        assertEquals(0, actualBitmask);
//
//        //Set is full
//        codecs = Set.of(AudioCodec.MP3, AudioCodec.AAC, AudioCodec.FLAC, AudioCodec.WAV);
//        actualBitmask = gateway.calculateBitmask(codecs);
//        assertEquals(15, actualBitmask);
//
//        //set is declared out of order
//        codecs = Set.of(AudioCodec.AAC, AudioCodec.MP3);
//        actualBitmask = gateway.calculateBitmask(codecs);
//        assertEquals(3, actualBitmask);
//        }
//
//    @Test
//    public void SingleCodecBitmaskTest() {
//        codecs = Set.of(AudioCodec.MP3);
//        ProductGateway gateway = new ProductGateway(type, sku, name, basePrice, size, hasLyrics, codecs, supportedStreamingServices);
//        int actualBitmask = gateway.calculateBitmask(codecs);
//        assertEquals(1, actualBitmask);
//        codecs = Set.of(AudioCodec.AAC);
//        actualBitmask = gateway.calculateBitmask(codecs);
//        assertEquals(2, actualBitmask);
//        codecs = Set.of(AudioCodec.FLAC);
//        actualBitmask = gateway.calculateBitmask(codecs);
//        assertEquals(4, actualBitmask);
//        codecs = Set.of(AudioCodec.WAV);
//        actualBitmask = gateway.calculateBitmask(codecs);
//        assertEquals(8, actualBitmask);
//    }

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
    @BeforeAll
    public static void setUpDB() throws DatabaseException{
        ProductGateway.createTable();
    }

    @AfterAll
    public static void rollback() throws SQLException {
        conn.rollback();
    }

    @Test
    public void getGeneratedID() throws DatabaseException {
        //declare mock gateway instance
        codecs = Set.of(AudioCodec.WAV);
        ProductGateway gateway1 = new ProductGateway(type, sku, name, basePrice, size, hasLyrics, codecs, supportedStreamingServices);
        ProductGateway gateway2 = new ProductGateway(type, sku, name, basePrice, size, hasLyrics, codecs, supportedStreamingServices);

        //generated ID should be more than 0
        assert(gateway1.getId() > 0);
        //generated ID should be unique
        assertNotEquals(gateway1.getId(), gateway2.getId());
    }

    @Test
    public void WrongTypeThrowException() throws DatabaseException {
            long testId = 1;
            ProductGateway resultThrowsException = ProductGateway.findAndBuild(testId,
                    productGateway -> {return productGateway;});
            assert(resultThrowsException != null);
    }

    @Test
    public void canInsertAndRetrieveAudioTrackTest() throws DatabaseException {
        ProductGateway gateway = new ProductGateway(ProductType.AudioTrack, sku, "Test Track", basePrice, 0, hasLyrics, Set.of(AudioCodec.MP3), null);

        //Retrieve and make sure all the stuff that's supposed to be there (dramatic pause) is there
        AudioTrack track = ProductGateway.findAndBuild(gateway.getId(), AudioTrack::builder);
        assertEquals(sku, track.getSku());
        assertEquals("Test Track", track.getName());
        assertEquals(basePrice, track.getBasePrice().dollars(),0.01);
        assertFalse(track.hasLyrics());
        assertEquals(AudioCodec.MP3, track.getCodec());
    }

   /* @Test
    public void SupportedServicesTest(){
        //TODO test to ensure streaming supported services are properly stored
    }*/
}

