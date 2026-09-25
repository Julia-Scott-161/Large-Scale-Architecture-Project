package datasource;

import domain.AudioCodec;
import domain.Cost;
import domain.VideoStreaming;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static datasource.DatabaseRegistry.getConnection;

public class ProductGateway {

    private long id;
    private ProductType type;
    private String sku;
    private String name;
    private double basePrice;
    //digital media
    private long size;
    private Boolean hasLyrics;
    private Set<AudioCodec> codecs;
    //TODO: Add To Table
    //VideoStreaming
    private boolean hasSubtitles;
    //TODO - how to store supported services? Was in table as multiple booleans. get supported codecs?
    //physical media
    private double width;
    private double height;
    private double depth;
    //apparel
    private int ApparelSize;
    //electronics
    private int Voltage;

    private ArrayList<VideoStreaming> supportedStreamingServices;

    //========================================================================================================
    // This is where the table driven gateway's static methods should go
    //========================================================================================================
    public static List<ProductGateway> findAllRows() {
        return null;
    }

    //right click, "change signature" it'll refactor and change everything
    static void createTable() throws DatabaseException {

        String sql = "CREATE TABLE IF NOT EXISTS ProductGateway (" + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " type TEXT,"
                + " sku TEXT," + " name TEXT," + " basePrice DOUBLE,"
                + " size INTEGER," + " hasLyrics BOOLEAN,"
                + " codecs INTEGER," + " hasSubtitles BOOLEAN" + ");";

        // Establish connection and execute statement
        Connection conn = getConnection();
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    private void insertNewRow() throws DatabaseException {
        String sql = "INSERT INTO ProductGateway (type, sku, name, basePrice, size, hasLyrics, codecs) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?);";

        Connection conn = getConnection();
        try (PreparedStatement insert = conn.prepareStatement(sql)) {
            insert.setString(1, String.valueOf(type));
            insert.setString(2, sku);
            insert.setString(3, name);
            insert.setDouble(4, basePrice);
            insert.setLong(5, size);
            insert.setBoolean(6, hasLyrics);
            insert.setInt(7, calculateBitmask(codecs));

            int affectedRows = insert.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = insert.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        this.id = generatedKeys.getLong(1);
                    }
                }
            }
            assert !conn.isClosed();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public long getId() {
        return id;
    }

    public ProductType getType() {
        return type;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public long getSize() {
        return size;
    }

    public boolean isHasLyrics() {
        return hasLyrics;
    }

    public Set<AudioCodec> getCodecs() {
        return codecs;
    }

    public boolean isHasSubtitles() {
        return hasSubtitles;
    }

    public ArrayList<VideoStreaming> getSupportedStreamingServices() {
        return supportedStreamingServices;
    }

    /**
     * Create constructor - used to put a new object into the db
     * @param type
     * @param sku
     * @param name
     * @param basePrice
     * @param size
     * @param hasLyrics
     * @param codecs
     * @param supportedStreamingServices
     */
    //hasSubtitles
    //supportedCodecs
    //width
    //height
    //depth
    //size
    //voltage

    public ProductGateway(ProductType type, String sku, String name, Double basePrice,
                          long size, boolean hasLyrics, Set<AudioCodec> codecs, ArrayList<VideoStreaming> supportedStreamingServices) throws DatabaseException {
        this.type = type;
        this.sku = sku;
        this.name = name;
        this.basePrice = basePrice;
        this.size = size;
        this.hasLyrics = hasLyrics;
        this.codecs = codecs;
        this.supportedStreamingServices = supportedStreamingServices;
        insertNewRow();
    }

    //TODO - now you have to store the many-to-many relationship
    public void insertSupportedServices(Connection connection) throws SQLException {
        //Separate Table necessary for many-to-many relationship
        String supportedServiceSql = "INSERT INTO SupportedServices (ElectronicsID, VideoStreamingID) VALUES (?, ?)";
        PreparedStatement insertService = connection.prepareStatement(supportedServiceSql);
        for(VideoStreaming service : supportedStreamingServices){
            insertService.setLong(1, this.getId());
            insertService.setLong(2, service.getId());
            insertService.executeUpdate();
        }
    }

    /**
     * A finder constructor that will be used by findAndBuild only
     * @param id
     */
    private ProductGateway(long id) throws DatabaseException {
        this.id = id;
        String sql = "SELECT * FROM ProductGateway Where id = ?";

        Connection conn = getConnection();
        try (PreparedStatement select = conn.prepareStatement(sql)) {
            select.setLong(1, id);
            ResultSet results = select.executeQuery();
            if (results.next()) {
                getDataOutOfResultSet(results);
            }
        }
        catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public static <T> T findAndBuild(long id, Function<ProductGateway, T> domainBuilder) throws DatabaseException {
        ProductGateway gateway = new ProductGateway(id);
        return domainBuilder.apply(gateway);
    }

    private static Connection getConnection() throws DatabaseException {
        Connection conn = null;
        try {
            conn = DatabaseRegistry.getConnection();
        }
        catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return conn;
    }

    private void getDataOutOfResultSet(ResultSet rs) throws SQLException {
        this.id = rs.getLong("id");
        this.type = ProductType.valueOf(rs.getString("type"));
        this.sku = rs.getString("sku");
        this.name = rs.getString("name");
        this.basePrice = rs.getDouble("basePrice");
        this.size = rs.getLong("size");
        this.hasLyrics = rs.getBoolean("hasLyrics");
        this.codecs = getSupportedCodecsSet(rs.getInt("codecs"));
    }

    private Set<AudioCodec> getSupportedCodecsSet(int mask) {
        Set<AudioCodec> codecs = new java.util.HashSet<>();
        for (AudioCodec codec : AudioCodec.values()) {
            int shift = 1 << codec.ordinal();
            if ((mask & shift) != 0) {
                codecs.add(codec);
            }
        }
        return codecs;
    }

    private int calculateBitmask(Set<AudioCodec> codecs) {
        if (codecs == null) { return 0;}
        int bitmask = 0;
        for (AudioCodec codec : codecs) {
            // ordinal takes the specific placement of codec in the enum
            // (so that it registers the different types)
            bitmask |= (1 << codec.ordinal());
        }
        return bitmask;
    }
}
