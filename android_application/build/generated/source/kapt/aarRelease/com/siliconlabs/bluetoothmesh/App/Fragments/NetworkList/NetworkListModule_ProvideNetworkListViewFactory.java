// Generated by Dagger (https://google.github.io/dagger).
package com.siliconlabs.bluetoothmesh.App.Fragments.NetworkList;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class NetworkListModule_ProvideNetworkListViewFactory
    implements Factory<NetworkListView> {
  private final NetworkListModule module;

  private final Provider<NetworkListFragment> networkListFragmentProvider;

  public NetworkListModule_ProvideNetworkListViewFactory(
      NetworkListModule module, Provider<NetworkListFragment> networkListFragmentProvider) {
    this.module = module;
    this.networkListFragmentProvider = networkListFragmentProvider;
  }

  @Override
  public NetworkListView get() {
    return proxyProvideNetworkListView(module, networkListFragmentProvider.get());
  }

  public static NetworkListModule_ProvideNetworkListViewFactory create(
      NetworkListModule module, Provider<NetworkListFragment> networkListFragmentProvider) {
    return new NetworkListModule_ProvideNetworkListViewFactory(module, networkListFragmentProvider);
  }

  public static NetworkListView proxyProvideNetworkListView(
      NetworkListModule instance, NetworkListFragment networkListFragment) {
    return Preconditions.checkNotNull(
        instance.provideNetworkListView(networkListFragment),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
