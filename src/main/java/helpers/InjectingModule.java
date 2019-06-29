package helpers;

import com.google.inject.AbstractModule;
import storage.BankStorage;
import storage.SafeBankStorage;

public final class InjectingModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(BankStorage.class).to(SafeBankStorage.class);
    }
}
