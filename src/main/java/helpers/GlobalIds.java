package helpers;

import java.util.concurrent.atomic.AtomicLong;

public final class GlobalIds {
    public static final AtomicLong ClientIdentifier = new AtomicLong(0);

    public static final AtomicLong AccountIdentifier = new AtomicLong(0);
}
