package it.digitalhub.dremiorestserver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.TimeZone;

import javax.annotation.Nullable;

import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.support.JdbcUtils;

public class DremioRowMapper extends ColumnMapRowMapper{
    @Override
    @Nullable
	protected Object getColumnValue(ResultSet rs, int index) throws SQLException {
        Object obj = rs.getObject(index);
        if (obj instanceof java.sql.Timestamp) {
            Timestamp ts = rs.getTimestamp(index, Calendar.getInstance(TimeZone.getTimeZone("GMT")));

            ZonedDateTime local = ts.toInstant().atZone(ZoneId.systemDefault());
            int localOffsetSeconds = local.getOffset().getTotalSeconds();
            Instant corrected = ts.toInstant().plus(localOffsetSeconds, ChronoUnit.SECONDS);

            return Timestamp.from(corrected);
        } else {
            return JdbcUtils.getResultSetValue(rs, index);
        }
	}
}
