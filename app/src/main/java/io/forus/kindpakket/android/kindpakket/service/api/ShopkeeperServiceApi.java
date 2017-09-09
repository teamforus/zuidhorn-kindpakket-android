package io.forus.kindpakket.android.kindpakket.service.api;

import io.forus.kindpakket.android.kindpakket.model.Device;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface ShopkeeperServiceApi {
    @POST(ApiParams.URL_SHOPKEEPER_DEVICE_TOKEN)
    Call<Device> getDeviceToken(@Header(value = ApiParams.AUTHORIZATION) String token);
}