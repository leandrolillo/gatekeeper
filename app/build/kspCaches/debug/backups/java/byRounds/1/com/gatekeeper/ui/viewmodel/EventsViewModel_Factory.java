package com.gatekeeper.ui.viewmodel;

import com.gatekeeper.data.repository.EventRepository;
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
public final class EventsViewModel_Factory implements Factory<EventsViewModel> {
  private final Provider<EventRepository> repositoryProvider;

  public EventsViewModel_Factory(Provider<EventRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public EventsViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static EventsViewModel_Factory create(Provider<EventRepository> repositoryProvider) {
    return new EventsViewModel_Factory(repositoryProvider);
  }

  public static EventsViewModel newInstance(EventRepository repository) {
    return new EventsViewModel(repository);
  }
}
