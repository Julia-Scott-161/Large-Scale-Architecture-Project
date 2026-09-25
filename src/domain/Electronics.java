package domain;

import datasource.DatabaseException;
import datasource.ProductGateway;
import datasource.ProductType;

public class Electronics extends PhysicalProduct {
    private String voltage;

    public Electronics findElectronics(long id) throws DatabaseException {
        return ProductGateway.findAndBuild(id, Electronics::builder);
    }

    public String getVoltage(){
        return voltage;
    }

    public Electronics(String sku, String name, Cost basePrice, double width, double height, double depth, String voltage) throws DatabaseException {
        ProductGateway gateway = new ProductGateway(ProductType.Apparel, sku, name, basePrice.dollars(), 0, false, null,
                false, width, height, depth, null, voltage, null);
        assignId(gateway.getId());
        getDataOutOfGateway(gateway);
    }

    protected void getDataOutOfGateway(ProductGateway gateway)
    {
        super.getDataOutOfGateway(gateway);
        this.voltage = gateway.getVoltage();
    }

    private Electronics() {
    }

    public static Electronics builder(ProductGateway gateway) throws DatasourceTypeMismatch {
        if (gateway.getType() != ProductType.Electronics) {
            throw new DatasourceTypeMismatch();
        }
        Electronics electronics = new Electronics();
        electronics.getDataOutOfGateway(gateway);
        return electronics;
    }
}
