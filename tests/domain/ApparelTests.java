package domain;

import datasource.DatabaseException;
import datasource.ProductGateway;
import datasource.ProductType;
import org.junit.Test;

import static org.junit.Assert.*;

public class ApparelTests {
    String sku = "400000000004";
    String name = "Test Apparel";
    double basePrice = 15.99;
    Cost cost = new Cost(basePrice);
    double width = 0.8;
    double height = 0.5;
    double depth = 4.2;
    String label = "S";

    ///starts with an Apparel and lets it build the gateway
    @Test
    public void CreateApparelTest() throws DatabaseException {
        Cost cost = new Cost(basePrice);
        //creates an Apparel, Apparel creates the gateway
        Apparel apparel = new Apparel(sku, name, cost, width, height, depth, label);
        //makes sure other variables are set and retrieved correctly
        assertEquals(sku, apparel.getSku());
        assertEquals(name, apparel.getName());
        assertEquals(cost, apparel.getBasePrice());
        assertEquals(width, apparel.getWidth(), 0.1);
        assertEquals(height, apparel.getHeight(), 0.1);
        assertEquals(depth, apparel.getDepth(), 0.1);
        assertEquals(label, apparel.getApparelSize());
    }

    /// starts with a gateway and calls Apparel.builder() on that gateway
    @Test
    public void CreateApparelFromGatewayTest() throws DatabaseException {
        ProductGateway gateway = new ProductGateway(ProductType.Apparel, sku, name, basePrice, 0, false, null,
                false, width, height, depth, label, null, null);
        //starts with a gateway, calls .builder() directly
        Apparel apparel = Apparel.builder(gateway);

        //make sure gateway and Apparel's IDs match
        assertEquals(gateway.getId(), apparel.getId());
        //make sure other variables are set and retrieved correctly
        assertEquals(sku, apparel.getSku());
        assertEquals(name, apparel.getName());
        assertEquals(basePrice, apparel.getBasePrice().dollars(), 0.01);
        assertEquals(width, apparel.getWidth(), 0.1);
        assertEquals(height, apparel.getHeight(), 0.1);
        assertEquals(depth, apparel.getDepth(), 0.1);
        assertEquals(label, apparel.getApparelSize());
    }

    /// Starts with a gateway and calls findAndBuild on it's id
    @Test
    public void FindAndBuildApparelTest() throws DatabaseException {
        ProductGateway gateway = new ProductGateway(ProductType.Apparel, sku, name, basePrice, 0, false, null,
                false, width, height, depth, label, null, null);
        //starts with a gateway, calls find and build
        Apparel apparel = ProductGateway.findAndBuild(gateway.getId(), Apparel::builder);
        //make sure gateway and Apparel's IDs match
        assertEquals(gateway.getId(), apparel.getId());
        //make sure other variables are set and retrieved correctly
        assertEquals(sku, apparel.getSku());
        assertEquals(name, apparel.getName());
        assertEquals(basePrice, apparel.getBasePrice().dollars(), 0.01);
        assertEquals(width, apparel.getWidth(), 0.1);
        assertEquals(height, apparel.getHeight(), 0.1);
        assertEquals(depth, apparel.getDepth(), 0.1);
        assertEquals(label, apparel.getApparelSize());
    }

    @Test
    public void WrongTypeThrowException() throws DatabaseException {
        ProductGateway gateway = new ProductGateway(ProductType.VideoStreaming, sku, name, basePrice, 0, false, null,
                false, width, height, depth, label, null, null);
        assertThrows(DatasourceTypeMismatch.class, () -> Apparel.builder(gateway));
    }
}
