// Generated by Dagger (https://google.github.io/dagger).
package com.siliconlabs.bluetoothmesh.App;

import android.content.Context;
import com.siliconlabs.bluetoothmesh.App.Logic.BluetoothScanner;
import com.siliconlabs.bluetoothmesh.App.Logic.MeshLogic;
import com.siliconlabs.bluetoothmesh.App.Logic.NetworkConnectionLogic;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class AppModule_ProvideNetworkConnectionLogicFactory
    implements Factory<NetworkConnectionLogic> {
  private final AppModule module;

  private final Provider<Context> contextProvider;

  private final Provider<MeshLogic> meshLogicProvider;

  private final Provider<BluetoothScanner> bluetoothLeScanLogicProvider;

  public AppModule_ProvideNetworkConnectionLogicFactory(
      AppModule module,
      Provider<Context> contextProvider,
      Provider<MeshLogic> meshLogicProvider,
      Provider<BluetoothScanner> bluetoothLeScanLogicProvider) {
    this.module = module;
    this.contextProvider = contextProvider;
    this.meshLogicProvider = meshLogicProvider;
    this.bluetoothLeScanLogicProvider = bluetoothLeScanLogicProvider;
  }

  @Override
  public NetworkConnectionLogic get() {
    return provideInstance(
        module, contextProvider, meshLogicProvider, bluetoothLeScanLogicProvider);
  }

  public static NetworkConnectionLogic provideInstance(
      AppModule module,
      Provider<Context> contextProvider,
      Provider<MeshLogic> meshLogicProvider,
      Provider<BluetoothScanner> bluetoothLeScanLogicProvider) {
    return proxyProvideNetworkConnectionLogic(
        module, contextProvider.get(), meshLogicProvider.get(), bluetoothLeScanLogicProvider.get());
  }

  public static AppModule_ProvideNetworkConnectionLogicFactory create(
      AppModule module,
      Provider<Context> contextProvider,
      Provider<MeshLogic> meshLogicProvider,
      Provider<BluetoothScanner> bluetoothLeScanLogicProvider) {
    return new AppModule_ProvideNetworkConnectionLogicFactory(
        module, contextProvider, meshLogicProvider, bluetoothLeScanLogicProvider);
  }

  public static NetworkConnectionLogic proxyProvideNetworkConnectionLogic(
      AppModule instance,
      Context context,
      MeshLogic meshLogic,
      BluetoothScanner bluetoothLeScanLogic) {
    return Preconditions.checkNotNull(
        instance.provideNetworkConnectionLogic(context, meshLogic, bluetoothLeScanLogic),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
