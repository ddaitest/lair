package com.daivp.api_worker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by ning.dai on 14-9-22.
 */
public class HttpUtil {

    private static void read(InputStream in, OutputStream out, DownloadListener
            callback, long length)
            throws IOException {
        byte[] buffer = new byte[4 * 1024];
        int b = -1;
        if (length > 0 && callback != null) {
            int last = 0;
            long loaded = 0;
            int tmp;
            while ((b = in.read(buffer)) != -1) {
                out.write(buffer, 0, b);
                loaded += b;
                tmp = (int) ((loaded * 100) / length);
                if (last < tmp) {
                    last = tmp;
                    callback.onProgress(last);
                }
            }

        } else {
            while ((b = in.read(buffer)) != -1) {
                out.write(buffer, 0, b);
            }
        }

    }

    public static void download(final String url, final File file,
                                final DownloadListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient.Builder().connectTimeout(1, TimeUnit.MINUTES).readTimeout(10, TimeUnit.MINUTES).build();
                Request request = new Request.Builder().url(url).get().build();
                client.newCall(request).enqueue(new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        if (listener != null) {
                            listener.onError();
                        }
                    }

                    @Override
                    public void onResponse(Call call, okhttp3.Response response) throws IOException {
                        FileOutputStream out = null;
                        //save response content into file.
                        if (!file.exists()) {
                            file.getParentFile().mkdirs();
                        }
                        try {
                            out = new FileOutputStream(file);
                            okhttp3.ResponseBody body = response.body();
                            if (body != null) {
                                InputStream is = body.byteStream();
                                read(is, out, listener, body.contentLength());
                                file.setLastModified(System.currentTimeMillis());
                                if (listener != null) {
                                    listener.onFinish();
                                }
                            } else {
                                if (listener != null) {
                                    listener.onError();
                                }
                            }
                        } catch (IOException e) {
                            if (listener != null) {
                                listener.onError();
                            }
                        } finally {
                            if (out != null) {
                                try {
                                    out.close();
                                } catch (IOException ignored) {
                                }
                            }
                        }
                    }
                });
            }
        }).start();
    }


    public interface DownloadListener {
        void onProgress(int i);

        void onFinish();

        void onError();
    }


}
