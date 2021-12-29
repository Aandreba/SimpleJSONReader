package org.sjr.codec.defaults;

import org.sjr.JSONObj;
import org.sjr.codec.JSONCodec;
import java.time.LocalDate;

public class LocalDateCodec implements JSONCodec<LocalDate> {
    final public static LocalDateCodec INSTANCE = new LocalDateCodec();
    private LocalDateCodec () {}

    @Override
    public LocalDate decode (JSONObj json) {
        return LocalDate.of (
                json.getInt("year").get(),
                json.getInt("month").get(),
                json.getInt("day").get()
        );
    }

    @Override
    public JSONObj encode (LocalDate value) {
        JSONObj resp = new JSONObj();
        resp.put("year", value.getYear());
        resp.put("month", value.getMonthValue());
        resp.put("day", value.getDayOfMonth());

        return resp;
    }

    @Override
    public Class<LocalDate> getTargetClass() {
        return LocalDate.class;
    }
}
