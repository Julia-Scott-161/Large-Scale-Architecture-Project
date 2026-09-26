package datasource;

import domain.*;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.fail;

public class ProductGatewayTests {

    ProductType type = ProductType.VideoStreaming;
    String sku = "000000000001";
    String name = "Test Product";
    double basePrice = 15.99;
    Cost cost = new Cost(basePrice);
    //other values default to null, zero, or false
    long size = 0;
    boolean hasLyrics = false;
    Set<AudioCodec> codecs = null;
    boolean hasSubtitles = false;
    double width = 0;
    double height = 0;
    double depth = 0;
    String apparelSize = null;
    String voltage = null;
    //ArrayList<VideoStreaming> supportedStreamingServices = new ArrayList<>();
    ArrayList<VideoStreaming> supportedStreamingServices = null;
    private static final Connection conn;

    static {
        try {
            conn = DatabaseRegistry.getConnection();
            assert !conn.isClosed();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /// The following tests were done to check calculateBitmask() and getSupportedCodecSet(int mask)'s
    /// functionality, by temporarily switching the functions to public. After the tests passed, the
    /// functions became private again.
//
//    @Test
//    public void CodecBitmaskEdgesTest() {
//        //Set is empty
//        codecs = Set.of();
//        ProductGateway gateway = new ProductGateway(type, sku, name, basePrice, size, hasLyrics, codecs, supportedStreamingServices);
//        int actualBitmask = gateway.calculateBitmask(codecs);
//        assertEquals(0, actualBitmask);
//
//        //Set is full
//        codecs = Set.of(AudioCodec.MP3, AudioCodec.AAC, AudioCodec.FLAC, AudioCodec.WAV);
//        actualBitmask = gateway.calculateBitmask(codecs);
//        assertEquals(15, actualBitmask);
//
//        //set is declared out of order
//        codecs = Set.of(AudioCodec.AAC, AudioCodec.MP3);
//        actualBitmask = gateway.calculateBitmask(codecs);
//        assertEquals(3, actualBitmask);
//        }
//
//    @Test
//    public void SingleCodecBitmaskTest() {
//        codecs = Set.of(AudioCodec.MP3);
//        ProductGateway gateway = new ProductGateway(type, sku, name, basePrice, size, hasLyrics, codecs, supportedStreamingServices);
//        int actualBitmask = gateway.calculateBitmask(codecs);
//        assertEquals(1, actualBitmask);
//        codecs = Set.of(AudioCodec.AAC);
//        actualBitmask = gateway.calculateBitmask(codecs);
//        assertEquals(2, actualBitmask);
//        codecs = Set.of(AudioCodec.FLAC);
//        actualBitmask = gateway.calculateBitmask(codecs);
//        assertEquals(4, actualBitmask);
//        codecs = Set.of(AudioCodec.WAV);
//        actualBitmask = gateway.calculateBitmask(codecs);
//        assertEquals(8, actualBitmask);
//    }

//    @Test
//    public void getSupportedCodecsTest() throws DatabaseException {
//        codecs = Set.of();
//        ProductGateway gateway = new ProductGateway(type, sku, name, basePrice, size, hasLyrics, codecs, supportedStreamingServices);
//        //Mask 0 = Empty Set
//        assertTrue(gateway.getSupportedCodecsSet(0).isEmpty());
//        //Mask 3 = MP3 + AAC
//        Set<AudioCodec> expectedSet = Set.of(AudioCodec.MP3, AudioCodec.AAC);
//        Set<AudioCodec> actualSet = gateway.getSupportedCodecsSet(3);
//        assertEquals(expectedSet, actualSet);
//        //Mask 15 = MP3 + AAC + FLAV + WAV
//        expectedSet = Set.of(AudioCodec.MP3, AudioCodec.AAC, AudioCodec.FLAC, AudioCodec.WAV);
//        actualSet = gateway.getSupportedCodecsSet(15);
//        assertEquals(expectedSet, actualSet);
//    }

    @BeforeAll
    public static void setUpDB() throws DatabaseException {
        ProductGateway.createTable();
    }

    @AfterAll
    public static void rollback() throws SQLException {
        conn.rollback();
    }

    /**
     * Tests the CreateTable() function in ProductGateway
        * Checks that there is a table with the name "ProductGateway"
        * Checks that there is a table with the name "SupportedServices"
     * @throws DatabaseException
     */
    @Test
    @Disabled("Fix and run this only when you change the structure of the table")
    public void canCreateTable() throws DatabaseException {
        ProductGateway.createTable();
        try(Statement stmt = conn.createStatement())
        {
            // Create the table
            ResultSet tables = stmt.executeQuery("SELECT name FROM sqlite_master " + "WHERE " + "type='table' AND name NOT LIKE 'sqlite_%'");

            Set<String> tableNames = new HashSet<>();
            while (tables.next()) {
                tableNames.add(tables.getString("name"));
            }
            assertTrue(tableNames.contains("ProductGateway"));
            assertTrue(tableNames.contains("SupportedServices"));

            stmt.execute("COMMIT;");
        } catch(SQLException e){
            fail("SQL Exception" + e.getMessage());
        }
    }

    @Test
    public void getGeneratedID() throws DatabaseException {
        //declare mock gateway instance
        codecs = Set.of(AudioCodec.WAV);
        ProductGateway gateway1 = new ProductGateway(type, sku, name, basePrice, size, hasLyrics, codecs,
                hasSubtitles, width, height, depth, apparelSize, voltage, supportedStreamingServices);
        ProductGateway gateway2 = new ProductGateway(type, sku, name, basePrice, size, hasLyrics, codecs,
                hasSubtitles, width, height, depth, apparelSize, voltage, supportedStreamingServices);

        //generated ID should be more than 0
        assert (gateway1.getId() > 0);
        //generated ID should be unique
        assertNotEquals(gateway1.getId(), gateway2.getId());
    }

    /** Test formated to match the canCreateTable() test that was provided in class
     * Creates a table and tests that the SupportedServices table was set up
     * correctly and works as intended.
     * @throws DatabaseException
     */
    @Test
    @Disabled("Run this only when you run the CanCreateTable() test")
    public void canCreateSupportedServicesTable() throws DatabaseException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS SupportedServices");
        }
        catch (SQLException e) {
            fail("Could not drop table" + e.getMessage());
        }
        ProductGateway.createTable();
        try (Statement stmt = conn.createStatement()) {
            ResultSet table = stmt.executeQuery("SELECT name From sqlite_master" +
                    " WHERE " + "type='table' AND name = 'SupportedServices'");
            assertTrue(table.next());
        }
        catch(SQLException e) {
            fail("SQL Exception" + e.getMessage());
        }
    }
}

