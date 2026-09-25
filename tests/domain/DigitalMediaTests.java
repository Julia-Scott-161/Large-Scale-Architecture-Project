package domain;

import datasource.DatabaseException;
import datasource.ProductGateway;
import datasource.ProductType;

import org.junit.Test;
import java.util.Set;
import static org.junit.Assert.*;

public class DigitalMediaTests {
    String sku = "200000000002";
    String name = "Test Media";
    double basePrice = 15.99;
    Cost cost = new Cost(basePrice);
    long size = 50;

    ///starts with an audio track and lets it build the gateway
    @Test
    public void CreateDigitalMediaTest() throws DatabaseException {

        //creates a digital media, DigitalMedia creates the gateway
        DigitalMedia media = new DigitalMedia(sku, name, cost, size);
        //makes sure other variables are set and retrieved correctly
        assertEquals(sku, media.getSku());
        assertEquals(name, media.getName());
        assertEquals(cost, media.getBasePrice());
        assertEquals(size, media.getSize());
    }

    /// starts with a gateway and calls AudioTrack.builder() on that gateway
    @Test
    public void CreateDigitalMediaFromGatewayTest() throws DatabaseException {
        ProductGateway gateway = new ProductGateway(ProductType.DigitalMedia, sku, name, basePrice, size, false, null,
                false,0.0, 0.0, 0.0, null, null, null);
        //starts with a gateway, calls .builder() directly
        DigitalMedia media = DigitalMedia.builder(gateway);

        //make sure gateway and DigitalMedia's IDs match
        assertEquals(gateway.getId(), media.getId());
        //make sure other variables are set and retrieved correctly
        assertEquals(sku, media.getSku());
        assertEquals(name, media.getName());
        assertEquals(basePrice, media.getBasePrice().dollars(), 0.01);
        assertEquals(size, media.getSize());
    }

    /// Starts with a gateway and calls findAndBuild on it's id
    @Test
    public void FindAndBuildDigitalMediaTest() throws DatabaseException {
        ProductGateway gateway = new ProductGateway(ProductType.DigitalMedia, sku, name, basePrice, size, false, null,
                false,0.0, 0.0, 0.0, null, null, null);
        //starts with a gateway, calls find and build
        DigitalMedia media = ProductGateway.findAndBuild(gateway.getId(), DigitalMedia::builder);

        //make sure gateway and DigitalMedia's IDs match
        assertEquals(gateway.getId(), media.getId());

        //make sure other variables are set and retrieved correctly
        assertEquals(sku, media.getSku());
        assertEquals(name, media.getName());
        assertEquals(basePrice, media.getBasePrice().dollars(), 0.01);
        assertEquals(size, media.getSize());
    }

    @Test
    public void WrongTypeThrowException() throws DatabaseException {
        ProductGateway gateway = new ProductGateway(ProductType.VideoStreaming, sku, name, basePrice, size, false, null,
                false,0.0, 0.0, 0.0, null, null, null);
        assertThrows(DatasourceTypeMismatch.class, () -> DigitalMedia.builder(gateway));
    }
}
