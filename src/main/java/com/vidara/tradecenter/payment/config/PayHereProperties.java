package com.vidara.tradecenter.payment.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "payhere")
public class PayHereProperties {

    private String merchantId;
    private String merchantSecret;
    private boolean sandbox = true;
    private String frontendUrl;

    /**
     * Public base URL of this API (no trailing slash), used for PayHere {@code notify_url}.
     * PayHere cannot call localhost — set this to your deployed API or an ngrok URL (e.g. {@code https://abc.ngrok-free.app}).
     * If blank, the URL is taken from the HTTP request that initiated payment (often wrong behind proxies or for local dev).
     */
    private String notifyBaseUrl = "";

    /**
     * When {@link #sandbox} is true, allows {@code POST /api/membership/reconcile-sandbox} to mark a pending MS checkout
     * as paid after the client completes PayHere (for local/dev when notify_url is unreachable). Never enable in live mode.
     */
    private boolean membershipSandboxReconcileEnabled = false;

    /**
     * When {@link #sandbox} is true, allows {@code POST /api/payment/reconcile-sandbox-order} after PayHere closes in the
     * browser so the order is marked paid and the confirmation email is sent when server notify cannot reach localhost.
     */
    private boolean orderSandboxReconcileEnabled = false;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantSecret() {
        return merchantSecret;
    }

    public void setMerchantSecret(String merchantSecret) {
        this.merchantSecret = merchantSecret;
    }

    public boolean isSandbox() {
        return sandbox;
    }

    public void setSandbox(boolean sandbox) {
        this.sandbox = sandbox;
    }

    public String getFrontendUrl() {
        return frontendUrl;
    }

    public void setFrontendUrl(String frontendUrl) {
        this.frontendUrl = frontendUrl;
    }

    public String getNotifyBaseUrl() {
        return notifyBaseUrl;
    }

    public void setNotifyBaseUrl(String notifyBaseUrl) {
        this.notifyBaseUrl = notifyBaseUrl;
    }

    public boolean isMembershipSandboxReconcileEnabled() {
        return membershipSandboxReconcileEnabled;
    }

    public void setMembershipSandboxReconcileEnabled(boolean membershipSandboxReconcileEnabled) {
        this.membershipSandboxReconcileEnabled = membershipSandboxReconcileEnabled;
    }

    public boolean isOrderSandboxReconcileEnabled() {
        return orderSandboxReconcileEnabled;
    }

    public void setOrderSandboxReconcileEnabled(boolean orderSandboxReconcileEnabled) {
        this.orderSandboxReconcileEnabled = orderSandboxReconcileEnabled;
    }
}
