package domain;

import datasource.DatabaseException;
import datasource.ProductGateway;
import datasource.ProductType;

public class Apparel extends PhysicalProduct {
    private String label;

    public Apparel findApparel(long id) throws DatabaseException {
        return ProductGateway.findAndBuild(id, Apparel::builder);
    }

    public String getApparelSize() {
        return label;
    }

    public Apparel(String sku, String name, Cost basePrice, double width, double height, double depth, String label) throws DatabaseException {
       ProductGateway gateway = new ProductGateway(ProductType.Apparel, sku, name, basePrice.dollars(), 0, false, null,
                false, width, height, depth, label, null, null);
        assignId(gateway.getId());
        getDataOutOfGateway(gateway);
    }

    protected void getDataOutOfGateway(ProductGateway gateway)
    {
        super.getDataOutOfGateway(gateway);
        this.label = gateway.getApparelSize();
    }

    private Apparel() {
    }

    public static Apparel builder(ProductGateway gateway) throws DatasourceTypeMismatch{
        if (gateway.getType() != ProductType.Apparel) {
            throw new DatasourceTypeMismatch();
        }
        Apparel apparel = new Apparel();
        apparel.getDataOutOfGateway(gateway);
        return apparel;
    }
}
