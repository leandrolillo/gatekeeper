package com.gatekeeper.ui.viewmodel;

import androidx.lifecycle.SavedStateHandle;
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
public final class AddEditEventViewModel_Factory implements Factory<AddEditEventViewModel> {
  private final Provider<EventRepository> repositoryProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public AddEditEventViewModel_Factory(Provider<EventRepository> repositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.repositoryProvider = repositoryProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public AddEditEventViewModel get() {
    return newInstance(repositoryProvider.get(), savedStateHandleProvider.get());
  }

  public static AddEditEventViewModel_Factory create(Provider<EventRepository> repositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new AddEditEventViewModel_Factory(repositoryProvider, savedStateHandleProvider);
  }

  public static AddEditEventViewModel newInstance(EventRepository repository,
      SavedStateHandle savedStateHandle) {
    return new AddEditEventViewModel(repository, savedStateHandle);
  }
}
