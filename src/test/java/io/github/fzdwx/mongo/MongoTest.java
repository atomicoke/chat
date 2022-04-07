package io.github.fzdwx.mongo;

import com.mongodb.client.MongoClients;
import io.github.fzdwx.inf.common.util.Json;
import io.github.fzdwx.logic.msg.ws.packet.ChatMessagePacket;
import org.bson.BsonDocument;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Date;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/4/6 18:18
 */
public class MongoTest {

    MongoTemplate mongoTemplate;

    @Test
    void test() {
        final var mongoClient = MongoClients.create("mongodb://114.132.249.192:27017");
        mongoTemplate = new MongoTemplate(mongoClient, "test");
        final var seqUid = getSeqUid(1509824278907011072L, 100123131L);
        if (mongoTemplate.collectionExists("test")) {
            mongoTemplate.dropCollection("test");
        }
        final var test = mongoTemplate.createCollection("test");

        final var vo = new ChatMessagePacket();
        vo.setContent("hello");
        vo.setContentType(1);
        vo.setMsgFrom(1);
        vo.setFromId(111111111111111111L);
        vo.setFromAvatar("这是头像");
        vo.setFromUname("这是名字");
        final var sMsg = new MsgInfo();
        sMsg.sendTime = new Date();
        sMsg.msg = vo;

        test.updateOne(BsonDocument.parse("""
                        { "userId" : "%s"}
                        """.formatted(seqUid)),
                new Document("$push", new Document("msg", Json.toJson(sMsg))));

        final var userChat = new UserChat();
        userChat.userId = seqUid;
        userChat.msg[0] = sMsg;
        test.insertOne(new Document(userChat.userId, Json.toJson(userChat.msg)));
    }

    static class MsgInfo {

        Date sendTime;
        ChatMessagePacket msg;
    }

    static class UserChat {

        String userId;
        MsgInfo[] msg = new MsgInfo[1];
    }

    static Long singleGocMsgNum = 5000L;


    String getSeqUid(Long userId, Long seq) {
        var seqSuffix = seq / singleGocMsgNum;
        return indexGen(userId, seqSuffix);
    }

    private String indexGen(final Long userId, final long seqSuffix) {
        return userId + ":" + seqSuffix;
    }
}