package com.gatekeeper.ui.viewmodel;

import com.gatekeeper.data.repository.GateLogRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class LogViewModel_Factory implements Factory<LogViewModel> {
  private final Provider<GateLogRepository> repositoryProvider;

  public LogViewModel_Factory(Provider<GateLogRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public LogViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static LogViewModel_Factory create(Provider<GateLogRepository> repositoryProvider) {
    return new LogViewModel_Factory(repositoryProvider);
  }

  public static LogViewModel newInstance(GateLogRepository repository) {
    return new LogViewModel(repository);
  }
}
