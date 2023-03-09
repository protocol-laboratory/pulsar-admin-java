package io.github.protocol.pulsar;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InnerHttpClientTest {

    @Test
    public void mapToParamsCaseEmptyMap(){
        Assertions.assertEquals("", InnerHttpClient.mapToParams());
    }

    @Test
    public void mapToParamsCaseSpecialChars(){
        Assertions.assertEquals("?a=b&a%2F=b%5C&%25=%26&%3D=%3F", InnerHttpClient.mapToParams(
                        "a", "b",
                        "a/", "b\\",
                        "%", "&",
                        "=", "?"));
    }

}
