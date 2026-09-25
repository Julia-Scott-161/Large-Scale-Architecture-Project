package domain;

import datasource.DatabaseException;
import datasource.ProductGateway;
import datasource.ProductType;

import org.junit.Test;
import java.util.ArrayList;
import java.util.Set;
import static org.junit.Assert.*;

public class AudioTrackTests {
    String sku = "000000000001";
    String name = "Test Product";
    double basePrice = 15.99;
    long size = 50;
    boolean hasLyrics = false;

    ///starts with an audio track and lets it build the gateway
    @Test
    public void CreateAudioTrackTest() throws DatabaseException {
        Cost cost = new Cost(basePrice);
        //creates an AudioTrack, AudioTrack creates the gateway
        AudioTrack track = new AudioTrack(sku, name, cost, size, hasLyrics, Set.of(AudioCodec.FLAC));
        //makes sure other variables are set and retrieved correctly
        assertEquals(sku, track.getSku());
        assertEquals(name, track.getName());
        assertEquals(cost, track.getBasePrice());
        assertEquals(size, track.getSize());
        assertFalse(track.hasLyrics());
        assertEquals(AudioCodec.FLAC, track.getCodec());
    }

    /// starts with a gateway and calls AudioTrack.builder() on that gateway
    @Test
    public void CreateAudioTrackFromGatewayTest() throws DatabaseException {
        ProductGateway gateway = new ProductGateway(ProductType.AudioTrack, sku, name, basePrice, size, hasLyrics, Set.of(AudioCodec.MP3), null);
        //starts with a gateway, calls .builder() directly
        AudioTrack track = AudioTrack.builder(gateway);

        //make sure gateway and audiotrack's IDs match
        assertEquals(gateway.getId(), track.getId());
        //make sure other variables are set and retrieved correctly
        assertEquals(sku, track.getSku());
        assertEquals(name, track.getName());
        assertEquals(basePrice, track.getBasePrice().dollars(), 0.01);
        assertEquals(size, track.getSize());
        assertFalse(track.hasLyrics());
        assertEquals(AudioCodec.MP3, track.getCodec());
    }

    /// Starts with a gateway and calls findAndBuild on it's id
    @Test
    public void FindAndBuildAudioTrackTest() throws DatabaseException {
        ProductGateway gateway = new ProductGateway(ProductType.AudioTrack, sku, name, basePrice, size, hasLyrics, Set.of(AudioCodec.MP3), null);
        //starts with a gateway, calls find and build
        AudioTrack track = ProductGateway.findAndBuild(gateway.getId(), AudioTrack::builder);
        //make sure gateway and audiotrack's IDs match
        assertEquals(gateway.getId(), track.getId());
        //make sure other variables are set and retrieved correctly
        assertEquals(sku, track.getSku());
        assertEquals(name, track.getName());
        assertEquals(basePrice, track.getBasePrice().dollars(), 0.01);
        assertEquals(size, track.getSize());
        assertFalse(track.hasLyrics());
        assertEquals(AudioCodec.MP3, track.getCodec());
    }

    @Test
    public void WrongTypeThrowException() throws DatabaseException {
        ProductGateway gateway = new ProductGateway(ProductType.VideoStreaming, sku, name, basePrice, size, hasLyrics, Set.of(AudioCodec.MP3), null);
        assertThrows(DatasourceTypeMismatch.class, () -> AudioTrack.builder(gateway));
    }
}
