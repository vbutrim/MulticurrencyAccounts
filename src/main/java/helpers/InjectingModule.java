package helpers;

import com.google.inject.AbstractModule;
import storage.BankStorage;
import storage.SafeBankStorageProxy;

public final class InjectingModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(BankStorage.class).to(SafeBankStorageProxy.class);
    }
}
