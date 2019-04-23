// Generated by Dagger (https://google.github.io/dagger).
package com.siliconlabs.bluetoothmesh.App;

import android.content.Context;
import com.siliconlabs.bluetoothmesh.App.Logic.BluetoothStateReceiver;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class AppModule_ProvideBluetoothStateReceiverFactory
    implements Factory<BluetoothStateReceiver> {
  private final AppModule module;

  private final Provider<Context> contextProvider;

  public AppModule_ProvideBluetoothStateReceiverFactory(
      AppModule module, Provider<Context> contextProvider) {
    this.module = module;
    this.contextProvider = contextProvider;
  }

  @Override
  public BluetoothStateReceiver get() {
    return provideInstance(module, contextProvider);
  }

  public static BluetoothStateReceiver provideInstance(
      AppModule module, Provider<Context> contextProvider) {
    return proxyProvideBluetoothStateReceiver(module, contextProvider.get());
  }

  public static AppModule_ProvideBluetoothStateReceiverFactory create(
      AppModule module, Provider<Context> contextProvider) {
    return new AppModule_ProvideBluetoothStateReceiverFactory(module, contextProvider);
  }

  public static BluetoothStateReceiver proxyProvideBluetoothStateReceiver(
      AppModule instance, Context context) {
    return Preconditions.checkNotNull(
        instance.provideBluetoothStateReceiver(context),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
