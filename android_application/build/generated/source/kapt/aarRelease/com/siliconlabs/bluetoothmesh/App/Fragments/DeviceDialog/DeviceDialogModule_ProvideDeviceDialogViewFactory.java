// Generated by Dagger (https://google.github.io/dagger).
package com.siliconlabs.bluetoothmesh.App.Fragments.DeviceDialog;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class DeviceDialogModule_ProvideDeviceDialogViewFactory
    implements Factory<DeviceDialogView> {
  private final DeviceDialogModule module;

  private final Provider<DeviceDialogFragment> deviceDialogFragmentProvider;

  public DeviceDialogModule_ProvideDeviceDialogViewFactory(
      DeviceDialogModule module, Provider<DeviceDialogFragment> deviceDialogFragmentProvider) {
    this.module = module;
    this.deviceDialogFragmentProvider = deviceDialogFragmentProvider;
  }

  @Override
  public DeviceDialogView get() {
    return proxyProvideDeviceDialogView(module, deviceDialogFragmentProvider.get());
  }

  public static DeviceDialogModule_ProvideDeviceDialogViewFactory create(
      DeviceDialogModule module, Provider<DeviceDialogFragment> deviceDialogFragmentProvider) {
    return new DeviceDialogModule_ProvideDeviceDialogViewFactory(
        module, deviceDialogFragmentProvider);
  }

  public static DeviceDialogView proxyProvideDeviceDialogView(
      DeviceDialogModule instance, DeviceDialogFragment deviceDialogFragment) {
    return Preconditions.checkNotNull(
        instance.provideDeviceDialogView(deviceDialogFragment),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}