package com.gatekeeper.service;

import com.gatekeeper.data.preferences.AppPreferences;
import com.gatekeeper.data.repository.ContactRepository;
import com.gatekeeper.data.repository.EventRepository;
import com.gatekeeper.data.repository.GateLogRepository;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class GateNotificationService_MembersInjector implements MembersInjector<GateNotificationService> {
  private final Provider<ContactRepository> contactRepositoryProvider;

  private final Provider<EventRepository> eventRepositoryProvider;

  private final Provider<AppPreferences> appPreferencesProvider;

  private final Provider<GateLogRepository> gateLogRepositoryProvider;

  public GateNotificationService_MembersInjector(
      Provider<ContactRepository> contactRepositoryProvider,
      Provider<EventRepository> eventRepositoryProvider,
      Provider<AppPreferences> appPreferencesProvider,
      Provider<GateLogRepository> gateLogRepositoryProvider) {
    this.contactRepositoryProvider = contactRepositoryProvider;
    this.eventRepositoryProvider = eventRepositoryProvider;
    this.appPreferencesProvider = appPreferencesProvider;
    this.gateLogRepositoryProvider = gateLogRepositoryProvider;
  }

  public static MembersInjector<GateNotificationService> create(
      Provider<ContactRepository> contactRepositoryProvider,
      Provider<EventRepository> eventRepositoryProvider,
      Provider<AppPreferences> appPreferencesProvider,
      Provider<GateLogRepository> gateLogRepositoryProvider) {
    return new GateNotificationService_MembersInjector(contactRepositoryProvider, eventRepositoryProvider, appPreferencesProvider, gateLogRepositoryProvider);
  }

  @Override
  public void injectMembers(GateNotificationService instance) {
    injectContactRepository(instance, contactRepositoryProvider.get());
    injectEventRepository(instance, eventRepositoryProvider.get());
    injectAppPreferences(instance, appPreferencesProvider.get());
    injectGateLogRepository(instance, gateLogRepositoryProvider.get());
  }

  @InjectedFieldSignature("com.gatekeeper.service.GateNotificationService.contactRepository")
  public static void injectContactRepository(GateNotificationService instance,
      ContactRepository contactRepository) {
    instance.contactRepository = contactRepository;
  }

  @InjectedFieldSignature("com.gatekeeper.service.GateNotificationService.eventRepository")
  public static void injectEventRepository(GateNotificationService instance,
      EventRepository eventRepository) {
    instance.eventRepository = eventRepository;
  }

  @InjectedFieldSignature("com.gatekeeper.service.GateNotificationService.appPreferences")
  public static void injectAppPreferences(GateNotificationService instance,
      AppPreferences appPreferences) {
    instance.appPreferences = appPreferences;
  }

  @InjectedFieldSignature("com.gatekeeper.service.GateNotificationService.gateLogRepository")
  public static void injectGateLogRepository(GateNotificationService instance,
      GateLogRepository gateLogRepository) {
    instance.gateLogRepository = gateLogRepository;
  }
}
