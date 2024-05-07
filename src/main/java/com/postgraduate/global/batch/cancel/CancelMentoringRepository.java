package com.postgraduate.global.batch.cancel;

import com.postgraduate.global.batch.BatchRepository;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CancelMentoringRepository extends BatchRepository<CancelMentoring> {
    private static final String AUTO_CANCEL = "자동취소";
    private static final String UPDATE_SQL = "update mentoring set status = 'CANCEL' where mentoring_id = :mentoringId";
    private static final String INSERT_SQL = "insert into refuse(" +
            "mentoring_mentoring_id, " +
            "reason" +
            ") values(" +
            ":mentoringId, :reason" +
            ")";
    public CancelMentoringRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, INSERT_SQL, UPDATE_SQL);
    }

    @Override
    protected Map<String, Object> generateEntityParams(CancelMentoring cancelMentoring) {
        HashMap<String, Object> parameter = new HashMap<>();
        parameter.put("mentoringId", cancelMentoring.mentoringId());
        parameter.put("reason", AUTO_CANCEL);
        return parameter;
    }
}
