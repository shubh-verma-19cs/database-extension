package org.example;

import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.interceptor.publish.PublishInboundInterceptor;
import com.hivemq.extension.sdk.api.interceptor.publish.parameter.PublishInboundInput;
import com.hivemq.extension.sdk.api.interceptor.publish.parameter.PublishInboundOutput;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbutils.QueryRunner;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
//import java.nio.charset.CharsetDecoder;
//import java.nio.charset.CharsetDecoder;
import java.sql.SQLException;
//import java.util.Arrays;
//import java.util.Optional;
import java.util.function.Function;

public class DatabasePublishInterceptor implements PublishInboundInterceptor {

    private static final @NotNull String QUERY = "INSERT INTO messages(id, topic, publisher, qos, payload) VALUES(?, ?, ?, ?, ?)";

//    public static final @NotNull Function<ByteBuffer, byte[]> EXTRACT_BYTES_FROM_BUFFER = byteBuffer -> {
//        final byte[] bytes = new byte[byteBuffer.remaining()];
//        byteBuffer.get(bytes);
//        return bytes;
//    };

    public static final @NotNull Function<ByteBuffer, String> EXTRACT_STRING_FROM_BUFFER = byteBuffer ->{
//        final String string = Arrays.toString(new String[java.lang.StringBuffer.capacity()]);
        final byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        String payloadContent = new String(bytes, Charset.defaultCharset());
        return payloadContent;
    };

    private final @NotNull QueryRunner queryRunner;
    public DatabasePublishInterceptor(final @NotNull HikariDataSource ds){
        queryRunner = new QueryRunner(ds);

    }

    @Override
    public void onInboundPublish(final @NotNull PublishInboundInput publishInboundInput, final @NotNull PublishInboundOutput publishInboundOutput) {

        final String topic = publishInboundInput.getPublishPacket().getTopic();
        final int qos = publishInboundInput.getPublishPacket().getQos().getQosNumber();
//        String getUser = publishInboundInput.getPublishPacket().get
//        byte[] payload = publishInboundInput.getPublishPacket().getPayload().map(EXTRACT_BYTES_FROM_BUFFER).orElse(null);
//        payload = (String[])payload;
        String payloadString = publishInboundInput.getPublishPacket().getPayload().map(EXTRACT_STRING_FROM_BUFFER).orElse(null);
//        ByteBuffer payload = publishInboundInput.getPublishPacket().getPayload().orElse(null);
//        ByteBuffer newPayload = payload;
//        String payloadString = new String(newPayload.array(), Charset.defaultCharset());
//        if (payload.hasArray()){
//            payloadString = new String(payload.array(), Charset.defaultCharset());
//        } else {
//            payloadString = " ";
//        }

//        String payloadString =


//        final String user = publishInboundInput.getClientInformation().
        final String user = publishInboundInput.getClientInformation().toString();
        final String publisher = publishInboundInput.getClientInformation().getClientId();
        long timestamp = publishInboundInput.getPublishPacket().getTimestamp();
        timestamp = (int)timestamp;
        try {
            queryRunner.insert(QUERY, (rs) -> null, timestamp, topic, user, qos, payloadString);
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }
//
//    public String convertByteBufferToString()
}
