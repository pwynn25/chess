package handler;

import result.ClearResult;
import service.ClearService;

public class ClearHandler {
//    convert to an object
    // clear request most likely
    ClearRequest request = (ClearRequest)gson.fromJson(reqData, LoginRequest.class);

    ClearService service = new ClearService();
    ClearResult result = service.login(request);

return gson.toJson(result);


}
