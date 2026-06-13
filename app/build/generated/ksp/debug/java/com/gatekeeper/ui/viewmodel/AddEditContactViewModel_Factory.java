package com.gatekeeper.ui.viewmodel;

import androidx.lifecycle.SavedStateHandle;
import com.gatekeeper.data.repository.ContactRepository;
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
public final class AddEditContactViewModel_Factory implements Factory<AddEditContactViewModel> {
  private final Provider<ContactRepository> repositoryProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public AddEditContactViewModel_Factory(Provider<ContactRepository> repositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.repositoryProvider = repositoryProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public AddEditContactViewModel get() {
    return newInstance(repositoryProvider.get(), savedStateHandleProvider.get());
  }

  public static AddEditContactViewModel_Factory create(
      Provider<ContactRepository> repositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new AddEditContactViewModel_Factory(repositoryProvider, savedStateHandleProvider);
  }

  public static AddEditContactViewModel newInstance(ContactRepository repository,
      SavedStateHandle savedStateHandle) {
    return new AddEditContactViewModel(repository, savedStateHandle);
  }
}
