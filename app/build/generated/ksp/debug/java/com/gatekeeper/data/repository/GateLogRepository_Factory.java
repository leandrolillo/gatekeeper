package com.gatekeeper.data.repository;

import com.gatekeeper.data.db.GateLogDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class GateLogRepository_Factory implements Factory<GateLogRepository> {
  private final Provider<GateLogDao> daoProvider;

  public GateLogRepository_Factory(Provider<GateLogDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public GateLogRepository get() {
    return newInstance(daoProvider.get());
  }

  public static GateLogRepository_Factory create(Provider<GateLogDao> daoProvider) {
    return new GateLogRepository_Factory(daoProvider);
  }

  public static GateLogRepository newInstance(GateLogDao dao) {
    return new GateLogRepository(dao);
  }
}
