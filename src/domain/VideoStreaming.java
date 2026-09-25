package domain;

import datasource.DatabaseException;
import datasource.ProductGateway;
import datasource.ProductType;

import java.util.Set;

public class VideoStreaming extends DigitalMedia { // Or a component/subclass
    private Set<AudioCodec> supportedCodecs;
    private boolean hasSubtitles;

    public VideoStreaming findVideoStreaming(long id) throws DatabaseException {
        return ProductGateway.findAndBuild(id, VideoStreaming::builder);
    }

    public boolean isHasSubtitles() {
        return hasSubtitles;
    }

    public VideoStreaming(String sku, String name, Cost basePrice, long size, boolean hasSubtitles) throws DatabaseException {
        ProductGateway gateway = new ProductGateway(ProductType.VideoStreaming, sku, name, basePrice.dollars(), size, false, null,
                hasSubtitles, 0.0, 0.0, 0.0, null, null, null);
        assignId(gateway.getId());
        getDataOutOfGateway(gateway);
    }

    protected void getDataOutOfGateway(ProductGateway gateway)
    {
        super.getDataOutOfGateway(gateway);
        this.hasSubtitles = gateway.isHasSubtitles();
    }

    private VideoStreaming() {
    }
    /**
     * This is the function injected into the datasource layer that allows it to build a VideoStreaming service
     * @param gateway
     * @return
     */
    public static VideoStreaming builder(ProductGateway gateway) throws DatasourceTypeMismatch {
        if (gateway.getType() != ProductType.VideoStreaming) {
            throw new DatasourceTypeMismatch();
        }
        VideoStreaming videoStreaming = new VideoStreaming();
        videoStreaming.getDataOutOfGateway(gateway);
        return videoStreaming;
    }


}