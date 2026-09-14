package datasource;

import domain.AudioCodec;
import org.junit.jupiter.api.Test;
import java.util.EnumSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class ProductGatewayTests {

    @Test
    public void testCodecBitmask() {
        //create the set of AudioCodecs for testing purposes
        /*
         * Expected bitmask for reference
         * MP3 = 1
         * AAC = 2
         * FLAC = 3
         * WAV = 4
         */
        //Set<AudioCodec> codecs = ProductGateway.getCodecs();
        Set<AudioCodec> codecs = EnumSet.of(AudioCodec.MP3, AudioCodec.AAC);
        int expectedBitmask = 3; //MP3 (1) + AAC (2)

        //call the set function that is in ProductGateway
        int actualBitmask = ProductGateway.setSupportedCodecs(expectedBitmask);

        //Test that the actualBitmask matches the expectedBitmask
        assertEquals(expectedBitmask, actualBitmask);

    }
}
