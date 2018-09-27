package org.jasig.cas.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.Assert;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.*;
import java.net.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author SxL
 * Created on 9/25/2018 5:14 PM.
 */
public final class SimpleHttpClient implements HttpClient, Serializable, DisposableBean {
    private static final long serialVersionUID = -5306738686476129516L;
    private static final int[] DEFAULT_ACCEPTABLE_CODES = new int[]{200, 304, 302, 301, 202};
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleHttpClient.class);
    private static ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(100);
    @NotNull
    @Size(
            min = 1
    )
    private int[] acceptableCodes;
    @Min(0L)
    private int connectionTimeout;
    @Min(0L)
    private int readTimeout;
    private boolean followRedirects;
    private SSLSocketFactory sslSocketFactory;
    private HostnameVerifier hostnameVerifier;

    public SimpleHttpClient() {
        this.acceptableCodes = DEFAULT_ACCEPTABLE_CODES;
        this.connectionTimeout = 5000;
        this.readTimeout = 5000;
        this.followRedirects = true;
        this.sslSocketFactory = null;
        this.hostnameVerifier = null;
    }

    public void setExecutorService(@NotNull ExecutorService executorService) {
        Assert.notNull(executorService);
        EXECUTOR_SERVICE = executorService;
    }

    @Override
    public boolean sendMessageToEndPoint(String url, String message, boolean async) {
        Future<Boolean> result = EXECUTOR_SERVICE.submit(new SimpleHttpClient.MessageSender(url, message, this.readTimeout, this.connectionTimeout, this.followRedirects));
        if (async) {
            return true;
        } else {
            try {
                return (Boolean)result.get();
            } catch (Exception var6) {
                return false;
            }
        }
    }

    @Override
    public boolean isValidEndPoint(String url) {
        try {
            URL u = new URL(url);
            return this.isValidEndPoint(u);
        } catch (MalformedURLException var3) {
            LOGGER.error(var3.getMessage(), var3);
            return false;
        }
    }

    @Override
    public boolean isValidEndPoint(URL url) {
        HttpURLConnection connection = null;
        InputStream is = null;

        try {
            connection = (HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(this.connectionTimeout);
            connection.setReadTimeout(this.readTimeout);
            connection.setInstanceFollowRedirects(this.followRedirects);
            if (connection instanceof HttpsURLConnection) {
                HttpsURLConnection httpsConnection = (HttpsURLConnection)connection;
                if (this.sslSocketFactory != null) {
                    httpsConnection.setSSLSocketFactory(this.sslSocketFactory);
                }

                if (this.hostnameVerifier != null) {
                    httpsConnection.setHostnameVerifier(this.hostnameVerifier);
                }
            }

            connection.connect();
            int responseCode = connection.getResponseCode();
            int[] var5 = this.acceptableCodes;
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                int acceptableCode = var5[var7];
                if (responseCode == acceptableCode) {
                    LOGGER.debug("Response code from server matched {}.", responseCode);
                    boolean var9 = true;
                    return var9;
                }
            }

            LOGGER.debug("Response Code did not match any of the acceptable response codes. Code returned was {}", responseCode);
            if (responseCode == 500) {
                is = connection.getInputStream();
                String value = IOUtils.toString(is);
                LOGGER.error("There was an error contacting the endpoint: {}; The error was:\n{}", url.toExternalForm(), value);
            }

            return false;
        } catch (IOException var13) {
            LOGGER.error(var13.getMessage(), var13);
            return false;
        } finally {
            IOUtils.closeQuietly(is);
            if (connection != null) {
                connection.disconnect();
            }

        }
    }

    public void setAcceptableCodes(int[] acceptableCodes) {
        this.acceptableCodes = acceptableCodes;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void setFollowRedirects(boolean follow) {
        this.followRedirects = follow;
    }

    public void setSSLSocketFactory(SSLSocketFactory factory) {
        this.sslSocketFactory = factory;
    }

    public void setHostnameVerifier(HostnameVerifier verifier) {
        this.hostnameVerifier = verifier;
    }

    @Override
    public void destroy() throws Exception {
        EXECUTOR_SERVICE.shutdown();
    }

    private static final class MessageSender implements Callable<Boolean> {
        private String url;
        private String message;
        private int readTimeout;
        private int connectionTimeout;
        private boolean followRedirects;

        public MessageSender(String url, String message, int readTimeout, int connectionTimeout, boolean followRedirects) {
            this.url = url;
            this.message = message;
            this.readTimeout = readTimeout;
            this.connectionTimeout = connectionTimeout;
            this.followRedirects = followRedirects;
        }

        public Boolean call() throws Exception {
            HttpURLConnection connection = null;
            BufferedReader in = null;

            Boolean var4;
            try {
                SimpleHttpClient.LOGGER.debug("Attempting to access {}", this.url);
                URL logoutUrl = new URL(this.url);
                String output = "logoutRequest=" + URLEncoder.encode(this.message, "UTF-8");
                connection = (HttpURLConnection)logoutUrl.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setReadTimeout(this.readTimeout);
                connection.setConnectTimeout(this.connectionTimeout);
                connection.setInstanceFollowRedirects(this.followRedirects);
                connection.setRequestProperty("Content-Length", Integer.toString(output.getBytes().length));
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                DataOutputStream printout = new DataOutputStream(connection.getOutputStream());
                printout.writeBytes(output);
                printout.flush();
                printout.close();
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                for(boolean readInput = true; readInput; readInput = StringUtils.isNotBlank(in.readLine())) {
                    ;
                }

                SimpleHttpClient.LOGGER.debug("Finished sending message to {}", this.url);
                Boolean var7 = true;
                return var7;
            } catch (SocketTimeoutException var12) {
                SimpleHttpClient.LOGGER.warn("Socket Timeout Detected while attempting to send message to [{}]", this.url);
                var4 = false;
                return var4;
            } catch (Exception var13) {
                SimpleHttpClient.LOGGER.warn("Error Sending message to url endpoint [{}]. Error is [{}]", this.url, var13.getMessage());
                var4 = false;
            } finally {
                IOUtils.closeQuietly(in);
                if (connection != null) {
                    connection.disconnect();
                }

            }

            return var4;
        }
    }
}
