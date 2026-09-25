package domain;
import datasource.DatabaseException;
import datasource.ProductGateway;
import datasource.ProductType;

public class DigitalMedia extends Product{
    private long size;

    protected void getDataOutOfGateway(ProductGateway gateway) {
        super.getDataOutOfGateway(gateway);
        this.size = gateway.getSize();
    }

    public long getSize() {
        return size;
    }

    public DigitalMedia(String sku, String name, Cost basePrice, long size) throws DatabaseException {
        ProductGateway gateway = new ProductGateway(ProductType.DigitalMedia, sku, name, basePrice.dollars(), size, false, null, null);
        assignId(gateway.getId());
        getDataOutOfGateway(gateway);
    }

    /**
     * This is used by the builder and no one else.  It doesn't need anything
     */
    DigitalMedia() {
    }

    public static DigitalMedia builder(ProductGateway gateway) throws DatasourceTypeMismatch {
        if (gateway.getType() != ProductType.DigitalMedia) {
            throw new DatasourceTypeMismatch();
        }
        DigitalMedia media = new DigitalMedia();
        media.getDataOutOfGateway(gateway);
        return media;
    }
}
