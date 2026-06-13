package com.gatekeeper.di;

import com.gatekeeper.data.db.AppDatabase;
import com.gatekeeper.data.db.GateLogDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class AppModule_ProvideGateLogDaoFactory implements Factory<GateLogDao> {
  private final Provider<AppDatabase> dbProvider;

  public AppModule_ProvideGateLogDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public GateLogDao get() {
    return provideGateLogDao(dbProvider.get());
  }

  public static AppModule_ProvideGateLogDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new AppModule_ProvideGateLogDaoFactory(dbProvider);
  }

  public static GateLogDao provideGateLogDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideGateLogDao(db));
  }
}
