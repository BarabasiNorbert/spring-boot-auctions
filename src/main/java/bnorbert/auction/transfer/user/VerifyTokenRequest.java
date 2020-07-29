package bnorbert.auction.transfer.user;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class VerifyTokenRequest {
    @NotNull
    @NotEmpty
    private String verificationToken;

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    @Override
    public String toString() {
        return "VerifyTokenRequest{" +
                "verificationToken='" + verificationToken + '\'' +
                '}';
    }
}
