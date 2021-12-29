package org.sjr.codec.defaults;

import org.sjr.JSONObj;
import org.sjr.codec.JSONCodec;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LocalDateTimeCodec implements JSONCodec<LocalDateTime> {
    final public static LocalDateTimeCodec INSTANCE = new LocalDateTimeCodec();
    private LocalDateTimeCodec () {}

    @Override
    public LocalDateTime decode (JSONObj json) {
        return LocalDateTime.of (
                json.getInt("year").get(),
                json.getInt("month").get(),
                json.getInt("day").get(),
                json.getInt("hour").get(),
                json.getInt("minute").get(),
                json.getInt("second").get(),
                json.getInt("nano").get()
        );
    }

    @Override
    public JSONObj encode (LocalDateTime value) {
        JSONObj resp = new JSONObj();
        resp.put("year", value.getYear());
        resp.put("month", value.getMonthValue());
        resp.put("day", value.getDayOfMonth());
        resp.put("hour", value.getHour());
        resp.put("minute", value.getMinute());
        resp.put("second", value.getSecond());
        resp.put("nano", value.getNano());

        return resp;
    }

    @Override
    public Class<LocalDateTime> getTargetClass() {
        return LocalDateTime.class;
    }
}
