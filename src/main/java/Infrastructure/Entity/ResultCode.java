package Infrastructure.Entity;


public enum ResultCode {
    ok("ok",1),warning("warning",2),error("error",3);

    private String resultState;
    private int resultCode;

    ResultCode(String state, int code) {
        this.resultState = state;
        this.resultCode = code;
    }

    public int getResultCode(){
        return this.resultCode;
    }
}
