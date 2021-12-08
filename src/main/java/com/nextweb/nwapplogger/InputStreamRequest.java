package com.nextweb.nwapplogger;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@Deprecated
public class InputStreamRequest extends Request<JSONObject> {

    private final Map<String, String> mHeaders;

    private final Map<String, String> mParams;

    private final Response.ErrorListener mErrorListener;

    private final Response.Listener<JSONObject> mListener;

    //======================================================================
    // Constructor
    //======================================================================

    public InputStreamRequest(@NonNull final int method,
                              @NonNull final String url,
                              @NonNull final Map<String, String> headers,
                              @NonNull final Map<String, String> params,
                              @NonNull final Response.ErrorListener errorListener,
                              @NonNull final Response.Listener<JSONObject> listener) {
        super(method, url, errorListener);
        // this request would never use cache since you are fetching the file content from server
        setShouldCache(false);

        this.mHeaders = headers;
        this.mParams = params;

        this.mErrorListener = errorListener;
        this.mListener = listener;
    }

    //======================================================================
    // Override Methods
    //======================================================================

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (mHeaders != null && mHeaders.size() > 0) {
            return mHeaders;
        }
        return super.getHeaders();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if (mParams != null && mParams.size() > 0) {
            return mParams;
        }
        return super.getParams();
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        mErrorListener.onErrorResponse(error);
    }


    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            return (Response<JSONObject>) Response.success
                    (
                            new JSONObject(json),
                            HttpHeaderParser.parseCacheHeaders(response)
                    );
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        }
    }
}
