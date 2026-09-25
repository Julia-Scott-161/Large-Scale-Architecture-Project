package domain;

import datasource.DatabaseException;
import datasource.ProductGateway;
import datasource.ProductType;
import org.junit.Test;

import static org.junit.Assert.*;

public class ElectronicsTests {
    String sku = "400000000004";
    String name = "Test Apparel";
    double basePrice = 15.99;
    Cost cost = new Cost(basePrice);
    double width = 0.8;
    double height = 0.5;
    double depth = 4.2;
    String voltage = "UNIVERSAL";

    ///starts with an Electronic and lets it build the gateway
    @Test
    public void CreateElectronicTest() throws DatabaseException {
        Cost cost = new Cost(basePrice);
        //creates an Electronic, Electronic creates the gateway
        Electronics electronic = new Electronics(sku, name, cost, width, height, depth, voltage);
        //makes sure other variables are set and retrieved correctly
        assertEquals(sku, electronic.getSku());
        assertEquals(name, electronic.getName());
        assertEquals(cost, electronic.getBasePrice());
        assertEquals(width, electronic.getWidth(), 0.1);
        assertEquals(height, electronic.getHeight(), 0.1);
        assertEquals(depth, electronic.getDepth(), 0.1);
        assertEquals(voltage, electronic.getVoltage());
    }

    /// starts with a gateway and calls Electronics.builder() on that gateway
    @Test
    public void CreateElectronicsFromGatewayTest() throws DatabaseException {
        ProductGateway gateway = new ProductGateway(ProductType.Electronics, sku, name, basePrice, 0, false, null,
                false, width, height, depth, null, voltage, null);
        //starts with a gateway, calls .builder() directly
        Electronics electronic = Electronics.builder(gateway);

        //make sure gateway and Electronics's IDs match
        assertEquals(gateway.getId(), electronic.getId());
        //make sure other variables are set and retrieved correctly
        assertEquals(sku, electronic.getSku());
        assertEquals(name, electronic.getName());
        assertEquals(basePrice, electronic.getBasePrice().dollars(), 0.01);
        assertEquals(width, electronic.getWidth(), 0.1);
        assertEquals(height, electronic.getHeight(), 0.1);
        assertEquals(depth, electronic.getDepth(), 0.1);
        assertEquals(voltage, electronic.getVoltage());
    }

    /// Starts with a gateway and calls findAndBuild on it's id
    @Test
    public void FindAndBuildElectronicsTest() throws DatabaseException {
        ProductGateway gateway = new ProductGateway(ProductType.Electronics, sku, name, basePrice, 0, false, null,
                false, width, height, depth, null, voltage, null);
        //starts with a gateway, calls find and build
        Electronics electronic = ProductGateway.findAndBuild(gateway.getId(), Electronics::builder);
        //make sure gateway and Electronics' IDs match
        assertEquals(gateway.getId(), electronic.getId());
        //make sure other variables are set and retrieved correctly
        assertEquals(sku, electronic.getSku());
        assertEquals(name, electronic.getName());
        assertEquals(basePrice, electronic.getBasePrice().dollars(), 0.01);
        assertEquals(width, electronic.getWidth(), 0.1);
        assertEquals(height, electronic.getHeight(), 0.1);
        assertEquals(depth, electronic.getDepth(), 0.1);
        assertEquals(voltage, electronic.getVoltage());
    }

    @Test
    public void WrongTypeThrowException() throws DatabaseException {
        ProductGateway gateway = new ProductGateway(ProductType.VideoStreaming, sku, name, basePrice, 0, false, null,
                false, width, height, depth, null, voltage, null);
        assertThrows(DatasourceTypeMismatch.class, () -> Electronics.builder(gateway));
    }
}
