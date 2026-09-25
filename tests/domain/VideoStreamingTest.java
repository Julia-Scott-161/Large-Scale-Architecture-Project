package domain;

import datasource.DatabaseException;
import datasource.ProductGateway;
import datasource.ProductType;

import org.junit.Test;
import java.util.Set;
import static org.junit.Assert.*;

public class VideoStreamingTest {
    String sku = "300000000003";
    String name = "Test Video";
    double basePrice = 15.99;
    long size = 50;

    ///starts with an VideoStreaming and lets it build the gateway
    @Test
    public void CreateVideoStreamingTest() throws DatabaseException {
        Cost cost = new Cost(basePrice);
    }

    /// starts with a gateway and calls VideoStreaming.builder() on that gateway
    @Test
    public void CreateVideoStreamingFromGatewayTest() throws DatabaseException {
        ProductGateway gateway = new ProductGateway(ProductType.VideoStreaming, sku, name, basePrice, size, false, null,
                true,0.0, 0.0, 0.0, null, null, null);
        //starts with a gateway, calls .builder() directly
        VideoStreaming videoStreaming = VideoStreaming.builder(gateway);

        //make sure gateway and videoStreaming's IDs match
        assertEquals(gateway.getId(), videoStreaming.getId());
        //make sure other variables are set and retrieved correctly
        assertEquals(sku, videoStreaming.getSku());
        assertEquals(name, videoStreaming.getName());
        assertEquals(basePrice, videoStreaming.getBasePrice().dollars(), 0.01);
        assertEquals(size, videoStreaming.getSize());
        //TODO- add isHasSubTitles
    }

    /// Starts with a gateway and calls findAndBuild on it's id
    @Test
    public void FindAndBuildVideoStreamingTest() throws DatabaseException {
        ProductGateway gateway = new ProductGateway(ProductType.VideoStreaming, sku, name, basePrice, size, false, null,
                true,0.0, 0.0, 0.0, null, null, null);
        //starts with a gateway, calls find and build
        VideoStreaming videoStreaming = ProductGateway.findAndBuild(gateway.getId(), VideoStreaming::builder);
        //make sure gateway and audiotrack's IDs match
        assertEquals(gateway.getId(), videoStreaming.getId());
        //make sure other variables are set and retrieved correctly
        assertEquals(sku, videoStreaming.getSku());
        assertEquals(name, videoStreaming.getName());
        assertEquals(basePrice, videoStreaming.getBasePrice().dollars(), 0.01);
        assertEquals(size, videoStreaming.getSize());
        assert(videoStreaming.isHasSubtitles());
    }

    @Test
    public void WrongTypeThrowException() throws DatabaseException {
        ProductGateway gateway = new ProductGateway(ProductType.AudioTrack, sku, name, basePrice, size, false, null,
                true,0.0, 0.0, 0.0, null, null, null);
        assertThrows(DatasourceTypeMismatch.class, () -> VideoStreaming.builder(gateway));
    }
}
