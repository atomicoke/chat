package org.atomicoke.logic.msg.sync.model;

import lombok.Data;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/4/23 17:18
 */
@Data
public class MessageSyncReq {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 需要同步消息的开始seq(不包含)
     */
    private String startSeq;

    /**
     * 需要同步消息的结束seq(包含)
     */
    private int limit;

    private static final int maxLimit = 1000;

    public Query toQuery() {
        final var criteria = Criteria.where("boxOwnerId").is(this.userId).and("boxOwnerSeq").gt(this.startSeq);
        final var query = Query.query(criteria);

        query.with(Sort.by(
                Sort.Order.asc("boxOwnerSeq")
        )).limit(this.limit);
        return query;
    }

    public void check() {
        if (this.userId == null) {
            throw new IllegalArgumentException("userId is null");
        }
        if (this.startSeq == null) {
            throw new IllegalArgumentException("startSeq is null");
        }
        if (this.limit == 0) {
            this.limit = maxLimit;
        } else if (this.limit > maxLimit) {
            this.limit = maxLimit;
        }
    }
}