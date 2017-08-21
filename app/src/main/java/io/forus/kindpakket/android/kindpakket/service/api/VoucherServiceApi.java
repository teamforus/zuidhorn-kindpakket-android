package io.forus.kindpakket.android.kindpakket.service.api;

import java.util.Map;

import io.forus.kindpakket.android.kindpakket.model.Voucher;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface VoucherServiceApi {
    @GET(ApiParams.URL_VOUCHER_ID)
    Call<Voucher> getVoucher(@Header(value = ApiParams.AUTHORIZATION) String token,
                             @Path("id") String voucherId);

    @PUT(ApiParams.URL_VOUCHER_ID)
    Call<Voucher> useVoucher(@Header(value = ApiParams.AUTHORIZATION) String token,
                             @Path("id") String voucherId,
                             @Body Map<String, Object> body);
}
