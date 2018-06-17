package pl.przemek.security;


public class SignContent {
    private Object signedContent;
    private String signature;

    public Object getSignedContent() {
        return signedContent;
    }

    public void setSignedContent(Object signedContent) {
        this.signedContent = signedContent;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

}
