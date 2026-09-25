package domain;
import datasource.DatabaseException;
import datasource.ProductGateway;
import datasource.ProductType;

public class PhysicalProduct extends Product {
    private double width;
    private double height;
    private double depth;

    protected void getDataOutOfGateway(ProductGateway gateway) {
        super.getDataOutOfGateway(gateway);
        this.width = gateway.getWidth();
        this.height = gateway.getHeight();
        this.depth = gateway.getDepth();
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getDepth() {
        return depth;
    }
}
