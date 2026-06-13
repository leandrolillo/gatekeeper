package com.gatekeeper.ui.viewmodel;

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
public final class ContactsViewModel_Factory implements Factory<ContactsViewModel> {
  private final Provider<ContactRepository> repositoryProvider;

  public ContactsViewModel_Factory(Provider<ContactRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ContactsViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static ContactsViewModel_Factory create(Provider<ContactRepository> repositoryProvider) {
    return new ContactsViewModel_Factory(repositoryProvider);
  }

  public static ContactsViewModel newInstance(ContactRepository repository) {
    return new ContactsViewModel(repository);
  }
}
