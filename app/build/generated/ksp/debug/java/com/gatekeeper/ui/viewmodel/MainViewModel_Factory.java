package com.gatekeeper.ui.viewmodel;

import com.gatekeeper.data.preferences.AppPreferences;
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
public final class MainViewModel_Factory implements Factory<MainViewModel> {
  private final Provider<AppPreferences> prefsProvider;

  public MainViewModel_Factory(Provider<AppPreferences> prefsProvider) {
    this.prefsProvider = prefsProvider;
  }

  @Override
  public MainViewModel get() {
    return newInstance(prefsProvider.get());
  }

  public static MainViewModel_Factory create(Provider<AppPreferences> prefsProvider) {
    return new MainViewModel_Factory(prefsProvider);
  }

  public static MainViewModel newInstance(AppPreferences prefs) {
    return new MainViewModel(prefs);
  }
}
