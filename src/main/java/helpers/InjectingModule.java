package helpers;

import com.google.inject.AbstractModule;
import storage.BankStorage;
import storage.BankStorageImpl;

public final class InjectingModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(BankStorage.class).to(BankStorageImpl.class);
    }
}
