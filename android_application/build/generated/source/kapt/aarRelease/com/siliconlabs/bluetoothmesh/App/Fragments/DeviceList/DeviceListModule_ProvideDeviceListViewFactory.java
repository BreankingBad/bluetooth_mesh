// Generated by Dagger (https://google.github.io/dagger).
package com.siliconlabs.bluetoothmesh.App.Fragments.DeviceList;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class DeviceListModule_ProvideDeviceListViewFactory
    implements Factory<DeviceListView> {
  private final DeviceListModule module;

  private final Provider<DeviceListFragment> deviceListFragmentProvider;

  public DeviceListModule_ProvideDeviceListViewFactory(
      DeviceListModule module, Provider<DeviceListFragment> deviceListFragmentProvider) {
    this.module = module;
    this.deviceListFragmentProvider = deviceListFragmentProvider;
  }

  @Override
  public DeviceListView get() {
    return proxyProvideDeviceListView(module, deviceListFragmentProvider.get());
  }

  public static DeviceListModule_ProvideDeviceListViewFactory create(
      DeviceListModule module, Provider<DeviceListFragment> deviceListFragmentProvider) {
    return new DeviceListModule_ProvideDeviceListViewFactory(module, deviceListFragmentProvider);
  }

  public static DeviceListView proxyProvideDeviceListView(
      DeviceListModule instance, DeviceListFragment deviceListFragment) {
    return Preconditions.checkNotNull(
        instance.provideDeviceListView(deviceListFragment),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
