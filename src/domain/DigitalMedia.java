package domain;
import datasource.ProductGateway;

public class DigitalMedia extends Product{
    private long size;

    protected void getDataOutOfGateway(ProductGateway gateway) {
        super.getDataOutOfGateway(gateway);
        this.size = gateway.getSize();
    }

    public long getSize() {
        return size;
    }
}
